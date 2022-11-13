import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from "@angular/forms";
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { SubSink } from "subsink";
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { FavoriteItem, FavoriteRequest } from "../../models/favorite-item.model";
import { SagArticleFavoriteEditModalComponent } from "../article-favorite-edit-modal/article-favorite-edit-modal.component";
import { FavoriteService } from "../../services/favorite.service";
import { FAVORITE_BROADCAST_EVENT, FAVORITE_PROCESS_TYPE, FAVORITE_SEARCH_TYPE } from "../../consts/article-detail.const";
import { ArticleDetailConfigService } from "../../services/article-detail-config.service";
import { finalize } from 'rxjs/operators';
import { BrowserUtil } from 'sag-common';

@Component({
    selector: 'sag-article-favorite-list',
    templateUrl: './article-favorite-list.component.html'
})
export class SagArticleFavoriteListComponent implements OnInit, OnDestroy {
    @Input() customPopover: PopoverDirective;
    @Input() template: TemplateRef<any>;
    @Input() includeLeafNode = true;
    @Input() searchMode = 'ID_SAGSYS';

    @Output() onSelectItem = new EventEmitter();

    @ViewChild('pop', { static: false }) popover: PopoverDirective;
    @ViewChild('searchControl', { static: false }) input: any;

    containerClass: string;

    search: FormGroup;
    isSearching = false;

    items: FavoriteItem[] = [];
    searchRequest: FavoriteRequest;
    total: number;

    PAGE_SIZE = 20;
    bsModalRef: BsModalRef;
    currentSearchType = FAVORITE_SEARCH_TYPE.ALL;

    private subs = new SubSink();
    private spinnerSelector = '.favorites-popover .popover-content';
    private spinnerOptions = { containerMinHeight: 0, withoutText: true };

    constructor(
        private fb: FormBuilder,
        private favoritesService: FavoriteService,
        private bsModalService: BsModalService,
        private config: ArticleDetailConfigService
    ) {

    }

    ngOnInit() {
        this.search = this.fb.group({
            term: ['']
        });

        this.setSearchType();

        this.containerClass = !this.includeLeafNode ? 'article-list-search' : '';
    }

    ngOnDestroy() {
        if (this.search) {
            this.search.controls['term'].setValue('');
        }
        this.subs.unsubscribe();

        if (this.bsModalRef) {
            this.bsModalRef.hide();
        }
    }

    setSearchType(isAll = false) {
        if (this.includeLeafNode || isAll) {
            this.currentSearchType = FAVORITE_SEARCH_TYPE.ALL;
        } else {
            this.currentSearchType = FAVORITE_SEARCH_TYPE.ARTICLE;
        }
    }

    onShown = (isAll = false) => {
        this.setSearchType(isAll);
        this.search.controls['term'].setValue('');
        this.items = [];
        this.searchRequest = new FavoriteRequest({
            keySearch: '',
            size: this.PAGE_SIZE,
            page: 0,
            type: this.currentSearchType === FAVORITE_SEARCH_TYPE.ALL ? '' : FAVORITE_SEARCH_TYPE.ARTICLE

        })
        this.onSearch();
    }

    onItemClick(item: any) {
        this.onSelectItem.emit(item);
        if (this.popover) {
            this.popover.hide();
        }
    }

    removeFavorite(item: any) {
        this.favoritesService.removeFavortieItem(item, () => {
            this.items = this.items.filter(value => value.id !== item.id);
        })
    }

    editFavorite(item: FavoriteItem) {
        const mode = item.type === FAVORITE_PROCESS_TYPE.ARTICLE && FAVORITE_BROADCAST_EVENT.EDIT_ARTICLE || FAVORITE_BROADCAST_EVENT.EDIT_LEAF;
        this.bsModalRef = this.bsModalService.show(SagArticleFavoriteEditModalComponent, {
            class: 'modal-sm article-favorite-edit-modal',
            ignoreBackdropClick: true,
            animated: !BrowserUtil.isIE(),
            initialState: {
                mode,
                data: item,
                onSave: (item) => {
                    this.favoritesService.updateFavoriteItem(item).subscribe(() => {
                        const edittedItem = this.items.find(value => value.id === item.id);
                        if (edittedItem) {
                            edittedItem.comment = item.comment;
                        }
                    });
                },
                onRemove: (item) => {
                    this.removeFavorite(item);
                }
            }
        });
    }

    doSearch() {
        const data = this.search.value && this.search.value['term'] || '';
        if (data === this.searchRequest.keySearch) {
            return
        }
        this.items = [];
        this.searchRequest = new FavoriteRequest({
            keySearch: data,
            size: this.PAGE_SIZE,
            page: 0,
            type: this.currentSearchType === FAVORITE_SEARCH_TYPE.ALL ? '' : FAVORITE_SEARCH_TYPE.ARTICLE

        })
        this.onSearch();
    }

    onSearch() {
        const spinner = this.config.spinner.start(this.spinnerSelector, this.spinnerOptions);
        this.subs.sink = this.favoritesService.searchFavorite(this.searchRequest)
            .pipe(
                finalize(() => this.config.spinner.stop(spinner))
            )
            .subscribe((res) => {
                this.items = [...this.items, ...res.data];
                this.total = res.totalPages;
            });
    }

    clearValue(event?) {
        if (event) {
            event.stopPropagation();
        }
        this.search.controls['term'].setValue('');
        this.doSearch();
    }

    onScrollEnd() {
        if (this.searchRequest.page + 1 < this.total) {
            this.searchRequest.page += 1;
            this.onSearch();
        }
    }

    onOutsideClick() {
        if (this.popover) {
            this.popover.hide();
        }
        if (this.customPopover) {
            this.customPopover.hide();
        }
    }
}