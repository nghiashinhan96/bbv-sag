import { Injectable } from '@angular/core';
import { ITreeOptions, TreeNode } from 'angular-tree-component/dist/defs/api';
import { TreeModel } from 'angular-tree-component/dist/models/tree.model';
import { MultiLevelSelectedFilter } from '../models/multi-level-selected-filter.model';
import { Subject } from 'rxjs/internal/Subject';

@Injectable()
export class MultiLevelCategoryTreeService {
    clearAllMerkmaleValues = new Subject<MultiLevelSelectedFilter>();

    constructor() { }

    getTreeOptions(): ITreeOptions {
        return {
            animateExpand: true,
            useVirtualScroll: false,
            nodeHeight: 22,
            useTriState: false,
            idField: 'uuid',
            displayField: 'description'
        };
    }

    isSingleMerkmaleAndSingleValue(category: TreeNode) {
        if (category && category.data) {
            let singleMermale = 0;
            if (category.data.children.length && category.data.children[0].children) {
                singleMermale = category.data.children[0].children.length;
            }
            return category.data.children.length === 1 && singleMermale === 1;
        }
        return false;
    }

    checkSelectedCategoryHasChildren(selectedCategoryNodeId: string, treeModel: TreeModel) {
        const selectedCategory: TreeNode = treeModel.getNodeById(selectedCategoryNodeId);
        return selectedCategory.data.children.length > 0;
    }

    setClearAllMerkmaleValues(clearMerkmaleValues: MultiLevelSelectedFilter) {
        this.clearAllMerkmaleValues.next(clearMerkmaleValues);
    }
}
