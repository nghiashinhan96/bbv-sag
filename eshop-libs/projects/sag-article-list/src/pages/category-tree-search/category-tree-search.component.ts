import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { debounceTime, distinctUntilChanged, tap, switchMap, catchError, map } from 'rxjs/operators';
import { Observable } from 'rxjs/internal/Observable';
import { Subject } from 'rxjs/internal/Subject';
import { CategoryTreeService } from '../../services/category-tree.service';
import { of } from 'rxjs/internal/observable/of';
import { MIN_SEARCH_TERM_LENGTH } from '../../consts/article-list.const';
import { Router } from '@angular/router';
import { SearchEventTarget } from '../../enums/search-event-target.enum';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { ProjectId } from 'sag-common';

@Component({
    selector: 'sag-article-list-category-tree-search',
    templateUrl: './category-tree-search.component.html',
    styleUrls: ['./category-tree-search.component.scss']
})
export class SagArticleListCategoryTreeSearchComponent implements OnInit {
    @Input() searchProductCategory = false;
    @Input() vehicleId: string;

    @Output() handleCategorySearch = new EventEmitter();

    data$: Observable<any>;
    isLoading = false;
    dataInput$ = new Subject<string>();
    selectedData;
    searchingTerm = '';

    constructor(
        private treeService: CategoryTreeService,
        private router: Router,
        private config: ArticleListConfigService
    ) {
    }

    ngOnInit() {
        this.loadData();
    }

    searchArticle(cate) {
        if (!cate) {
            return;
        }

        if(!this.searchProductCategory) {
            const cateDetail = this.treeService.getCateDetailById(cate.value);
            if (cateDetail && cateDetail.link) {
                window.open(cateDetail.link, '_blank');
                return;
            }
            if (this.config.projectId === ProjectId.AUTONET) {
                this.treeService.unCheckAllCate(false);
            }
            this.treeService.checkOnCategoryTreeCate(cate.value, true);
            this.treeService.emitSearchRequest(cate.value, true);
        } else {
            if (!cate.activeLink){
                this.clearSelected();
                return;
            }
             
            this.router.navigate(['wsp'], {
                queryParams: {
                    treeId: cate.treeId,
                    nodeId: cate.value,
                    target: SearchEventTarget.NAV_SEARCH_RESULTS
                }
            });
        }

        this.clearSelected();
    }

    clearSelected(){
        setTimeout(() => {
            this.selectedData = undefined;
        });
    }

    private loadData() {
        this.data$ = this.dataInput$.pipe(
            distinctUntilChanged(),
            tap(() => this.isLoading = true),
            debounceTime(600),
            switchMap(term => {
                this.searchingTerm = term;
                if (!term || term.length < MIN_SEARCH_TERM_LENGTH) {
                    this.isLoading = false;
                    return of([]); // empty list if the length of search term below minimum
                }

                let search = this.treeService.searchTree(this.vehicleId, term);

                if(this.searchProductCategory) {
                    // search product category
                    const body = {
                        options: ['product_category'],
                        keyword: this.searchingTerm.trim(),
                        fullrequest: false,
                        page: 0,
                        genericSearch: true
                    };

                    search = this.treeService.searchFreetext(body);
                }

                if(search) {
                    return search.pipe(
                        map((result: any) => {
                            if(this.vehicleId) {
                                return Object.keys(result || {}).map(k => {
                                    return { value: k, label: result[k], search: term, activeLink: true };
                                }).slice(0, 10);
                            }

                            if(this.searchProductCategory) {
                                const unitrees = result && result.unitreeData && result.unitreeData.unitrees || {};
                                this.handleCategorySearch.emit({ ...unitrees, keyword: this.searchingTerm.trim() });
                                if (result.unitreePresent) {
                                    const content = result.unitreeData.unitrees.content || [];
                                    return (content || []).map(k => {
                                        return { value: k.nodeId, label: k.nodeName, search: term, parentId: k.parentId, treeId: k.treeId, activeLink: k.activeLink };
                                    });
                                }

                                return [];
                            }
                        }),
                        catchError(() => of([])), // empty list on error
                        tap(() => this.isLoading = false)
                    );
                }
            })
        );
    }

}
