import {
    Component, OnInit, ViewChild, Input, Output, EventEmitter, Renderer2,
    ChangeDetectorRef, SimpleChanges, ElementRef, OnChanges
} from '@angular/core';
import { TreeNode, ITreeOptions } from 'angular-tree-component/dist/defs/api';
import { isEmpty, isNil } from 'lodash';
import { TreeComponent } from 'angular-tree-component';
import { MultiLevelCategoryTreeService } from '../../services/multi-level-category-tree.service';
import { MultiLevelSelectedFilter } from '../../models/multi-level-selected-filter.model';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { MerkmaleGaids } from '../../models/merkmale-gaid.model';
import { ProjectId } from 'sag-common';

@Component({
    selector: 'sag-article-list-merkmale-filter',
    templateUrl: './merkmale-filter.component.html',
    styleUrls: ['./merkmale-filter.component.scss']
})
export class SagArticleListMerkmaleFilterComponent implements OnInit, OnChanges {

    @ViewChild('filterTree', { static: false }) filterTree: TreeComponent;

    @Input() title: string;

    @Input() set filterData(newData: any) {
        this.filterItems = newData;
    }
    @Input() set selectedCateFilter(selectedCateFilterData: any) {
        if (isEmpty(selectedCateFilterData)) {
            return;
        }

        if (!this.filterTree || !this.filterTree.treeModel) {
            return;
        }

        if (this.filterTree.treeModel.nodes.length) {
            const selectedNode: TreeNode = this.filterTree.treeModel.getNodeById(selectedCateFilterData.selectedNodeId);
            if (!this.categoryTreeService.isSingleMerkmaleAndSingleValue(selectedNode)) {
                if (selectedNode && selectedNode.hasChildren) {
                    const showMoreNode = selectedNode.data.children[selectedNode.data.children.length - 1];
                    if (!selectedNode.data.children.length) {
                        selectedNode.data.children = [...selectedCateFilterData.filter];
                    }
                    selectedNode.treeModel.update();
                    this.configService.spinner.stop();
                    if (showMoreNode) {
                        this.showTopCriteria(selectedNode.data.children, selectedNode, false, showMoreNode);
                    } else {
                        this.showTopCriteria(selectedNode.data.children, selectedNode);
                    }
                }
            }
            this.cdr.markForCheck();
        }
    }

    @Input() set resetAllFilters(isReset: boolean) {
        this.resetFilter();
    }

    @Input() set deselectMermaleValue(val: any) {
        if (this.filterTree) {
            this.clearSelectedConditionInCategory(val.value);
        }
    }
    @Input() showContent = true;
    @Input() brandFilterData;

    @Output() changeFilter = new EventEmitter<{ multipleLevelGaids: MerkmaleGaids[], filterBadge: MultiLevelSelectedFilter }>();
    @Output() changeRootCategoryEmitter = new EventEmitter();
    @Output() resetFilterEmitter = new EventEmitter();
    @Output() onFilterBrandChangeEmitter = new EventEmitter<string[]>();

    categoryTreeOptions: ITreeOptions;
    filterItems: any;
    newFilter: any;
    selectedBrandFilterData: string[] = [];
    isCategoryCollapsed = false;

    private multipleLevelGaids: MerkmaleGaids[] = [];
    private selectedCategory: TreeNode;
    private readonly SHOW_MORE_TEXT = 'showMore';
    private readonly TOP_MOST_IMPORTANT = 3;

    constructor(
        private categoryTreeService: MultiLevelCategoryTreeService,
        private renderer: Renderer2,
        private cdr: ChangeDetectorRef,
        private configService: ArticleListConfigService
    ) { }

    ngOnInit() {
        this.categoryTreeOptions = this.categoryTreeService.getTreeOptions();

        this.categoryTreeService.clearAllMerkmaleValues.subscribe(data => {
            this.clearAllConditionsInCategory(data.id.values().next().value);
        });
    }

    ngOnChanges(changes: SimpleChanges): void {
        this.resetMultipleGaid(changes);
    }

    /**
     * Expand or collapse the fourth item onward
     */
    showMoreItems(data: TreeNode) {
        const upper: TreeNode = data.parent;
        const showMoreNode = upper.data.children[upper.data.children.length - 1];
        showMoreNode.isExpanded = !showMoreNode.isExpanded;

        this.showTopCriteria(upper.data.children, upper, showMoreNode.isExpanded);
    }

    hoverShowMore(ref: ElementRef, desc: string) {
        if (desc === this.SHOW_MORE_TEXT) {
            this.renderer.addClass(ref, 'show-more-multi-filter');
        }
    }

    leaveShowMore(ref: ElementRef, desc: string) {
        if (desc === this.SHOW_MORE_TEXT) {
            this.renderer.removeClass(ref, 'show-more-multi-filter');
        }
    }

    resetFilter() {
        if (!this.multipleLevelGaids.length && this.selectedBrandFilterData.length === 0) {
            return;
        }
        if (this.multipleLevelGaids.length) {
            this.updateResetFilterUI();
        }
        if (this.selectedBrandFilterData.length > 0) {
            this.selectedBrandFilterData = [];
        }
        this.resetFilterEmitter.emit();
    }

    selectFilterValue(selectedNode: TreeNode, isRemovedFromArticleResult?: boolean) {
        const currentNode: TreeNode = this.filterTree.treeModel.getNodeById(selectedNode.id);
        if (currentNode.isRoot && (currentNode.isExpanded || !currentNode.hasChildren)) {
            if (this.multipleLevelGaids.length && this.multipleLevelGaids[0]['nodeId'] !== currentNode.id) {
                const previousNode: TreeNode = this.filterTree.treeModel.getNodeById(this.multipleLevelGaids[0]['nodeId']);
                if (previousNode) {
                    this.deselectPreviousSelectedCategory(previousNode);
                }
            }

            this.multipleLevelGaids = []; // Reset filter condition for each new selected category
            this.selectedCategory = selectedNode;
            this.selectCategory(currentNode, isRemovedFromArticleResult);
            return;
        }

        if (currentNode.isLeaf) {
            this.selectCriteriaValue(currentNode, isRemovedFromArticleResult);
            return;
        }

        if (selectedNode.isCollapsed) {
            selectedNode.expand();
            selectedNode.data.isExpanded = true;
            this.openFilterValue(selectedNode);
        } else {
            selectedNode.collapse();
            selectedNode.data.isExpanded = false;
        }
    }

    openFilterValue(currentNode: TreeNode, isRemovedFromArticleResult?: boolean) {
        if (this.categoryTreeService.isSingleMerkmaleAndSingleValue(currentNode)) {
            this.selectFilterValue(currentNode, isRemovedFromArticleResult);
            currentNode.collapseAll();
            return;
        }
        if (currentNode.isRoot && currentNode.isExpanded) {
            if (currentNode.data.isSelected) {
                currentNode.expand();
                return;
            }
            this.selectFilterValue(currentNode, isRemovedFromArticleResult);
            return;
        }
        if (currentNode.isLeaf) {
            this.selectFilterValue(currentNode, isRemovedFromArticleResult);
            return;
        }
        if (currentNode.isExpanded) {
            if (currentNode.data.children.length > this.TOP_MOST_IMPORTANT) {
                currentNode.data.children.map(criteriaValue => {
                    const selectedCriteriavaValue: TreeNode = this.filterTree.treeModel.getNodeById(criteriaValue.uuid);
                    if (!selectedCriteriavaValue.data.isShown) {
                        selectedCriteriavaValue.setIsHidden(true);
                    }

                    // Reset the showMore icon state
                    if (selectedCriteriavaValue.data.isExpanded) {
                        selectedCriteriavaValue.data.isExpanded = false;
                    }
                });
            }
            currentNode.setIsExpanded(true);
        } else {
            currentNode.setIsExpanded(false);
        }
    }

    private selectCategory(currentNode: TreeNode, isRemovedFromArticleResult?: boolean) {
        const spinner = this.configService.spinner.start('sag-article-list-merkmale-filter .filter-container');
        // Handle the case collapsed category with selected state is deselected, it will not collapse other expanded category
        if (!currentNode.isCollapsed && !currentNode.data.isSelected) {
            this.filterTree.treeModel.collapseAll();
        }
        // Wait for collapse all
        setTimeout(() => {
            currentNode.data.isSelected = !currentNode.data.isSelected;
            if (currentNode.isCollapsed) {
                this.showTopCriteria(currentNode.data.children, currentNode);
            } else {
                currentNode.collapseAll();
            }
            this.multipleLevelGaids = this.handleCategorySelection(currentNode.data.isSelected,
                currentNode.data.id,
                currentNode.data,
                currentNode.data.uuid);
            this.notifyChangedFilter();
            const isSelectedCategoryCalled = this.categoryTreeService
                .checkSelectedCategoryHasChildren(currentNode.id, this.filterTree.treeModel);
            if (!isSelectedCategoryCalled) {
                this.changeRootCategoryEmitter.next(
                    {
                        multipleLevelGaids: [],
                        gaId: currentNode.data.id,
                        nodeId: currentNode.id
                    }
                );
            } else {
                this.configService.spinner.stop(spinner);
            }
        });
    }

    checkCriteriaText(node: TreeNode) {
        this.selectFilterValue(node);
    }

    private selectCriteriaValue(currentNode: TreeNode, isRemovedFromArticleResult?: boolean) {
        const rootParent = currentNode.realParent.realParent;
        const parent = currentNode.realParent;
        currentNode.data.isSelected = !currentNode.data.isSelected;
        this.handleCriteriaValueSelectionCase(rootParent, parent, currentNode.data.isSelected, currentNode.data.id, currentNode.id,
            isRemovedFromArticleResult);
    }

    private handleCriteriaValueSelectionCase(rootParent: TreeNode,
        parent: TreeNode,
        isSelected: boolean,
        id: string, nodeId?: string,
        isRemovedFromArticleResult?: boolean) {
        if (isSelected) {
            rootParent.data.isSelected = true;
            parent.data.isSelected = true;
        }

        if (!isSelected) {
            const cvp = parent.data.children.filter(child => child.isSelected);
            if (!cvp.length) {
                parent.data.isSelected = false;
                this.multipleLevelGaids = this.multipleLevelGaids.filter((gaid) => {
                    return gaid.subIds = [...gaid.subIds].filter((cid) => cid.id !== parent.data.id);
                });

            }
            this.handleCriteriaValueSelection(isSelected, rootParent.data.id, parent.data.id, id);
            this.notifyChangedFilter();
            return;
        }

        this.handleCriteriaValueSelection(isSelected, rootParent.data.id, parent.data.id, id, nodeId);
        this.notifyChangedFilter();
    }

    private handleCriteriaValueSelection(isSelected: boolean,
        selectedCateId: string,
        selectedCid: string,
        selectedCidValue: string,
        nodeId?: string) {
        return isSelected ? this.addSelectedCriteriaValue(selectedCateId, selectedCid, selectedCidValue, nodeId) :
            this.removeSelectedCriteriaValue(selectedCateId, selectedCid, selectedCidValue);
    }

    private addSelectedCriteriaValue(selectedCateId: string, selectedCid: string, selectedCidValue: string, nodeId?: string) {
        if (selectedCateId && selectedCid && selectedCidValue) {
            // In case the user select the leaf element first
            this.multipleLevelGaids = this.addSelectedCategory(selectedCateId);
            this.addSelectedCriteria(selectedCateId, selectedCid);
            this.multipleLevelGaids = this.multipleLevelGaids.map(gaid => {
                if (gaid.id === selectedCateId) {
                    const criteria = gaid.subIds.map(cid => {
                        if (cid.id === selectedCid) {
                            const isExisted = cid.subIds.some(cvp => cvp.id === selectedCidValue);
                            if (isExisted) {
                                return { ...cid };
                            }
                            const newCvpList = [...cid.subIds, { id: selectedCidValue, nodeId, subIds: [] }];
                            return { ...cid, subIds: newCvpList };
                        }
                        return { ...cid };
                    });
                    return { ...gaid, subIds: [...criteria] };
                }
                return { ...gaid };
            });
        }
    }

    private removeSelectedCriteriaValue(selectedCateId: string, selectedCid: string, selectedCidValue: string) {
        if (selectedCateId && selectedCid && selectedCidValue) {
            this.multipleLevelGaids = this.multipleLevelGaids.map(gaid => {
                const criteria = gaid.subIds.map(cid => {
                    if (cid.id === selectedCid) {
                        const crtValue = [...cid.subIds].filter((cvp) => cvp.id !== selectedCidValue);
                        return { ...cid, subIds: crtValue };
                    }
                    return { ...cid };
                });
                return { ...gaid, subIds: criteria };
            });
        }
    }

    private addSelectedCriteria(categoryId: string, criteriaId: string) {
        if (categoryId && criteriaId) {
            const isExisted = this.multipleLevelGaids.some(cate => cate.id === categoryId);
            if (isExisted) {
                this.multipleLevelGaids = this.multipleLevelGaids.map(gaid => {
                    if (gaid.id === categoryId) {
                        const isCriteriaExisted = gaid.subIds.some(cid => cid.id === criteriaId);
                        if (!isCriteriaExisted) {
                            const newCriteria = [...gaid.subIds, { id: criteriaId, subIds: [] }];
                            return { ...gaid, subIds: newCriteria };
                        }
                    }
                    return { ...gaid };
                });
            } else {
                this.multipleLevelGaids = this.addSelectedCategory(categoryId);
                this.addSelectedCriteria(categoryId, criteriaId);
            }
        }
    }

    /**
     * Handle the category selection type
     * @param isSelected Selectable state of item
     * @param selectedCategoryId The selected category id
     */
    private handleCategorySelection(isSelected: boolean, selectedCategoryId: string, cate: any, nodeId?: string) {
        if (!isSelected) {
            cate.children.forEach(cid => {
                cid.isSelected = false;
                if (cid.children) {
                    cid.children.forEach(cvp => {
                        if (cvp.isSelected) {
                            cvp.isSelected = false;
                        }
                    });
                }
            });
        }
        return isSelected ? this.addSelectedCategory(selectedCategoryId, nodeId) : this.removeSelectedCategory(selectedCategoryId);
    }

    /**
     * Adding the selected category to the array for building request to filter
     * @param selectedCategory The category will be selected
     */
    private addSelectedCategory(selectedCategoryId: any, nodeId?: string) {
        const isExisted = this.multipleLevelGaids.filter(cate => cate.id === selectedCategoryId);
        return isExisted.length ? [...this.multipleLevelGaids] :
            [...this.multipleLevelGaids, { id: selectedCategoryId, nodeId, subIds: [] }];
    }

    /**
     * Remove the selected category from the array for building request to filter
     * @param selectedCategory The category will be deselected
     */
    private removeSelectedCategory(selectedCategoryId: any) {
        return [...this.multipleLevelGaids].filter((gaid) => gaid.id !== selectedCategoryId);
    }

    /**
     * Reset the filter tree
     */
    private updateResetFilterUI() {
        this.filterTree.treeModel.nodes.forEach(node => {
            node.isSelected = false;
            if (node.children) {
                node.children.forEach(childNode => {
                    childNode.isSelected = false;
                    if (childNode.children) {
                        childNode.children.forEach(leaf => {
                            if (leaf.isSelected) {
                                leaf.isSelected = false;
                            }
                        });
                    }
                });
            }
        });
        this.filterTree.treeModel.collapseAll();
        this.multipleLevelGaids = [];
    }

    /**
     * Reset the array of selected category and its values when having a new search term
     * @param changes inputs are changed
     */
    private resetMultipleGaid(changes: SimpleChanges) {
        if (changes.freeTextSearchKeyWord) {
            this.multipleLevelGaids =
                changes.freeTextSearchKeyWord.currentValue === changes.freeTextSearchKeyWord.previousValue ? this.multipleLevelGaids : [];
        }
    }

    private deselectPreviousSelectedCategory(prev: TreeNode) {
        if (prev.data.isSelected) {
            prev.data.isSelected = false;
            prev.collapseAll();
            prev.data.children.forEach(cid => {
                cid.isSelected = false;
                cid.isExpanded = false;
                if (cid.children) {
                    cid.children.forEach(cvp => {
                        if (cvp.isSelected) {
                            cvp.isSelected = false;
                        }
                    });
                }
            });
        }
    }

    private deselectChildren(rootNode: TreeNode) {
        rootNode.data.children.forEach(cidNode => {
            cidNode.isSelected = false;
            if (cidNode.children) {
                cidNode.children.forEach(cvp => {
                    if (cvp.isSelected) {
                        cvp.isSelected = false;
                    }
                });
            }
        });
    }

    private notifyChangedFilter() {
        this.changeFilter.next({ multipleLevelGaids: this.multipleLevelGaids, filterBadge: this.createFilter() });
    }

    private createFilter(): MultiLevelSelectedFilter {
        const cateId = this.multipleLevelGaids.length ?
            new Map<string, string>().set(this.multipleLevelGaids[0].id, this.multipleLevelGaids[0]['nodeId']) :
            new Map();
        const cateName = this.multipleLevelGaids.length ? this.selectedCategory.data.description : '';
        const cvpId = this.multipleLevelGaids.length ? this.prepareCriteriaValueMap() : new Map();
        const state: MultiLevelSelectedFilter = {
            id: cateId,
            categoryName: cateName,
            criteriaIds: new Map(),
            criteriaValueIds: cvpId,
            deletedCriteriaVallue: []
        };

        return state;
    }

    private prepareCriteriaValueMap() {
        const values = new Map<string, string>();

        this.multipleLevelGaids.forEach((gaid) => {
            gaid.subIds.forEach((cid) => {
                cid.subIds.forEach(cvp => {
                    values.set(cvp.id, cvp['nodeId']);
                });
            });
        });

        return values;
    }

    private showTopCriteria(children = [], selectedNode?: TreeNode, isExpanded = false, showMoreNode?: TreeNode) {
        if (isNil(children) || !children.length || this.categoryTreeService.isSingleMerkmaleAndSingleValue(selectedNode)) {
            return;
        }
        this.hidePredefinedNode(children, children.length > this.TOP_MOST_IMPORTANT, isExpanded, showMoreNode);
        selectedNode.expand();
    }

    private hidePredefinedNode(nodes = [], hasMoreThreeItems: boolean, isExpanded: boolean, showMoreNode?: TreeNode) {
        nodes.map((criteria: any) => {
            const selectedCriteria: TreeNode = this.filterTree.treeModel.getNodeById(criteria.uuid);
            if (hasMoreThreeItems) {
                if (!selectedCriteria.data.isShown) {
                    if (showMoreNode && showMoreNode.isExpanded) {
                        selectedCriteria.setIsHidden(false);
                    } else {
                        if (isExpanded) {
                            selectedCriteria.setIsHidden(false);
                        } else {
                            selectedCriteria.setIsHidden(true);
                        }
                    }
                }
            }
            if (!selectedCriteria) {
                return;
            }
            if (!!selectedCriteria && selectedCriteria.data.isShown && selectedCriteria.data.description !== this.SHOW_MORE_TEXT) {
                this.showTopCriteriaValues(selectedCriteria.data.children);
            }

            if (hasMoreThreeItems) {
                if (selectedCriteria.data.isShown) {
                    selectedCriteria.setIsExpanded(true);
                }
            } else {
                selectedCriteria.setIsExpanded(true);
            }
        });
    }

    private showTopCriteriaValues(values = []) {
        if (isNil(values) || !values.length) {
            return;
        }

        if (values.length > this.TOP_MOST_IMPORTANT) {
            const lastNode = values[values.length - 1];
            values.forEach(v => {
                const criteriaValueNode: TreeNode = this.filterTree.treeModel.getNodeById(v.uuid);
                if (!criteriaValueNode.data.isShown) {
                    if (lastNode.isExpanded) {
                        criteriaValueNode.setIsHidden(false);
                    } else {
                        criteriaValueNode.setIsHidden(true);
                    }
                }
            });
        }
    }

    private clearAllConditionsInCategory(rootNode: string) {
        if (rootNode) {
            const remaindCategory: TreeNode = this.filterTree.treeModel.getNodeById(rootNode);
            remaindCategory.expand();
            remaindCategory.data.isSelected = true;
            this.deselectChildren(remaindCategory);
            this.multipleLevelGaids = this.multipleLevelGaids.map(gaid => ({ ...gaid, subIds: [] }));
            this.notifyChangedFilter();
        }
    }

    private clearSelectedConditionInCategory(childNodeId: string, rootNodeId?: string) {
        const deletedValue: TreeNode = this.filterTree.treeModel.getNodeById(childNodeId);

        if (deletedValue.isLeaf) {
            this.selectFilterValue(deletedValue, true);
        }
        // const selectedCategory: TreeNode = this.filterTree.treeModel.getNodeById(rootNodeId);
        // selectedCategory.expand();
        // selectedCategory.data.isSelected = true;
    }

    onFilterBrandChange(brandFilters) {
        if (brandFilters.type === 'suppliers') {
            if (brandFilters.isChecked) {
                this.selectedBrandFilterData = [...this.selectedBrandFilterData, brandFilters.filterId];
            } else {
                this.selectedBrandFilterData = this.selectedBrandFilterData.filter(supplier => supplier !== brandFilters.filterId);
            }
        }
        this.onFilterBrandChangeEmitter.emit(this.selectedBrandFilterData);
    }

}
