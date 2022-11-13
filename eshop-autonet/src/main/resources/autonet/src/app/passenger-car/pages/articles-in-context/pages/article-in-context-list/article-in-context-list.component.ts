import { Component, OnDestroy, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { uniqBy } from 'lodash';
import { ArticleModel, BarFilter } from 'sag-article-detail';
import { ARTICLE_LIST_TYPE, BrandFilterItem, CategoryTreeService } from 'sag-article-list';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { ArticleInContextService } from '../../services/articles-in-context.service';
import { SubSink } from 'subsink';
import { CommonModalService } from 'src/app/shared/autonet-common/services/common-modal.service';
@Component({
    selector: 'autonet-article-in-context-list',
    templateUrl: './article-in-context-list.component.html',
    styleUrls: ['./article-in-context-list.component.scss']
})
export class ArticleInContextListComponent implements OnInit, OnDestroy {
    articleListType = ARTICLE_LIST_TYPE.IN_CONTEXT;

    brandsData;
    barFilterData;

    private subs = new SubSink();

    constructor(
        private modalService: BsModalService,
        public articleInContextService: ArticleInContextService,
        private categoryTreeService: CategoryTreeService,
        public storage: AppStorageService,
        private commonModalService: CommonModalService
    ) { }

    ngOnInit() {
        this.categoryTreeService.emitSearchRequest();

        this.subs.sink = this.articleInContextService.brandsData$.subscribe(data => {
            this.brandsData = data;
        })

        this.subs.sink = this.articleInContextService.barFilterData$.subscribe(data => {
            this.barFilterData = data;
        })
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    onArticleNumberClick(article: ArticleModel) {
        this.commonModalService.showReplaceModal(article);
    }

    onShowAccessories(data: any) {
        this.commonModalService.showAccessoriesModal(data);
    }

    onShowPartsList(data: any) {
        this.commonModalService.showPartsListModal(data);
    }

    onShowCrossReference(data: any) {
        this.commonModalService.showCrossReferenceModal(data);
    }

    onBrandsItemsChange(data: BrandFilterItem) {
        const userSetting: any = this.storage.libUserSetting;
        let isBrandFilterEnable = userSetting.isSalesOnBeHalf ? userSetting.salesBrandFilterEnabled : userSetting.customerBrandFilterEnabled;

        if (!isBrandFilterEnable) {
            return;
        }

        const brandsData = [...this.articleInContextService.brandsData];
        const results = brandsData.filter(item => item.key !== data.key || item.gaID !== data.gaID);
        this.articleInContextService.emitBrandFilterData([...results, data]);
    }

    onBrandsStateChange(data: BrandFilterItem) {
        const brandsData = [...this.brandsData] || [];

        brandsData.forEach(item => {
            if (item.key === data.key) {
                item.brands.forEach(brand => {
                    brand.checked = data.brands.some(b => b.name === brand.name && b.checked);
                });
            }
        });

        this.articleInContextService.emitBrandFilterData(brandsData);
    }

    onBarFilterItemsChange(data) {
        const barFilterData = [...this.articleInContextService.barFilterData];
        const results = barFilterData.filter(item => item.key !== data.key);

        let currentFilter = barFilterData.filter(item => item.key === data.key)[0];
        if (currentFilter) {
            currentFilter.options = uniqBy([...currentFilter.options, ...data.options], 'cvp');
        } else {
            currentFilter = data;
        }

        let options = [...currentFilter.options];
        const isAllNumber = !((options || []).some(op => !parseFloat(op.cvp)));
        if (isAllNumber) {
            options = (options || []).sort((opA, opB) => parseFloat(opA.cvp) - parseFloat(opB.cvp));
        } else {
            options = (options || []).sort((opA, opB) => opA.cvp > opB.cvp ? 1 : -1);
        }

        currentFilter.options = options;
        this.articleInContextService.emitBarFilterData([...results, currentFilter]);
    }

    onBarFilterStateChange(data: BarFilter) {
        if (this.articleListType === ARTICLE_LIST_TYPE.IN_CONTEXT) {
            const barFilterData = [...this.barFilterData] || [];
            const filterData = data && data[0];
            if (!filterData) {
                return;
            }
            barFilterData.forEach(item => {
                if (item.key === filterData.key) {
                    item.options.forEach(filter => {
                        filter.checked = (filterData.options || []).some(op => op.cvp === filter.cvp && op.checked);
                    })
                }
            });

            this.articleInContextService.emitBarFilterData(barFilterData);
            return;
        }
    }
}
