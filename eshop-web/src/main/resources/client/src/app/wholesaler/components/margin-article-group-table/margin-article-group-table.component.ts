import { Component, Input, OnInit, OnDestroy, EventEmitter, Output } from '@angular/core';
import { MarginArticleGroupModel } from '../../models/margin-article-group.model';
import { TablePage } from 'sag-table';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { MarginArticleGroupSearchRequestModel } from '../../models/margin-article-group-search-request.model';
import { MarginService } from '../../services/margin.service';
import { finalize } from 'rxjs/operators';
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app-lang-code.constant';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { SubSink } from 'subsink';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SagConfirmationBoxComponent } from 'sag-common';
import { isEmpty, values } from 'lodash';
import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';
import { MarginArticleGroupFormModalComponent } from '../margin-article-group-form-modal/margin-article-group-form-modal.component';
import { MarginImportModalComponent } from '../margin-import-modal/margin-import-modal.component';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { ARTGROUP_EXPORT_TYPE } from '../../services/constant';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'connect-margin-article-group-table',
    templateUrl: './margin-article-group-table.component.html',
    styleUrls: ['./margin-article-group-table.component.scss']
})
export class MarginArticleGroupTableComponent implements OnInit, OnDestroy {
    @Input() margins: number[] = [];
    @Input() currentLangCode: string = DEFAULT_LANG_CODE;

    @Output() editArtGroupEmit = new EventEmitter<any>();
    @Output() deleteArtGroupEmit = new EventEmitter<any>();

    articleGroups: MarginArticleGroupModel[] = [];
    searchModel: MarginArticleGroupSearchRequestModel = new MarginArticleGroupSearchRequestModel();

    page = new TablePage();
    isViewTree = true;
    numberOfElements: number;
    currentPage: number = 0;

    exportOptions = [
        {
            type: ARTGROUP_EXPORT_TYPE.ALL_ARTGROUP,
            text: 'MARGIN_MANAGE.ARTICLE_GROUP_EXPORT_TYPE.SAG_ARTICLE_GROUP'
        },
        {
            type: ARTGROUP_EXPORT_TYPE.MARGIN_DEF_AND_ALL_ARTGROUP,
            text: 'MARGIN_MANAGE.ARTICLE_GROUP_EXPORT_TYPE.MARGIN_SAG_ARTICLE_GROUP'
        },
        {
            type: ARTGROUP_EXPORT_TYPE.MARGIN_DEF,
            text: 'MARGIN_MANAGE.ARTICLE_GROUP_EXPORT_TYPE.MARGIN_DEF'
        },
    ];

    exportTypeProps = {
        [ARTGROUP_EXPORT_TYPE.ALL_ARTGROUP]: {
            filename: 'Mrg_ArtGrp_Template+ArtGrps',
            path: 'csv-template'
        },
        [ARTGROUP_EXPORT_TYPE.MARGIN_DEF_AND_ALL_ARTGROUP]: {
            path: 'export-all-csv',
            filename: 'Mrg_ArtGrp_Def+ArtGrps'
        },
        [ARTGROUP_EXPORT_TYPE.MARGIN_DEF]: {
            path: 'export-mapped-csv',
            filename: 'Mrg_ArtGrp_Def'
        },
    }

    private importModalRef: BsModalRef;

    private textSearchTimer;
    private bsModalRef: BsModalRef;
    private subs = new SubSink();
    private spinnerSelector = '.margin-article-group-table';

    constructor(
        private marginService: MarginService,
        private modalService: BsModalService,
        private appModal: AppModalService,
        private translateService: TranslateService
    ) {
        this.subs.sink = this.marginService.articleGroups$.subscribe(data => {
            this.articleGroups = data;
        });
    }

    ngOnInit() {
        this.page = new TablePage(<TablePage>{
            itemsPerPage: APP_DEFAULT_PAGE_SIZE,
            currentPage: 0,
            totalItems: 0
        });

        this.fireSearchData();
    }

    ngOnDestroy() {
        if (this.bsModalRef) {
            this.bsModalRef.hide();
        }

        if (this.importModalRef) {
            this.importModalRef.hide();
        }

        this.subs.unsubscribe();
    }

    onTextInputChange($event) {
        if (this.textSearchTimer) {
            clearTimeout(this.textSearchTimer);
        }
        this.textSearchTimer = setTimeout(() => {
            this.filterData();
        }, 1000);
    }

    pageChanged(currentPage: number) {
        this.page.currentPage = currentPage;
        this.fireSearchData();
    }

    deleteItem(event) {
        const artGroup = event.artGroup || event;

        this.bsModalRef = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-art-group-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'MARGIN_MANAGE.DELETE_ART_GROUP',
                messageParams: { sagArticleGroup: artGroup.sagArticleGroup, customArticleGroup: artGroup.customArticleGroup },
                okButton: 'COMMON_LABEL.YES',
                cancelButton: 'COMMON_LABEL.NO',
                close: () => {
                    this.handleDelete(artGroup, event.callback);
                }
            }
        });

        this.appModal.modals = this.bsModalRef;
    }

    editArtGroup(event) {
        const obj = event.artGroup ? {
            artGroup: event.artGroup,
            callback: (data) => {
                if (event.artGroup && !event.artGroup.default) {
                    setTimeout(() => {
                        event.callback(data);
                    });
                } else {
                    event.callback();
                }
            }
        } :
            { // view list default
                artGroup: event, callback: () => {
                    this.page.currentPage = this.getPage();
                    this.fireSearchData();
                }
            }

        this.editArtGroupEmit.emit(obj);
    }

    createArtGrp() {
        this.bsModalRef = this.modalService.show(MarginArticleGroupFormModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                currentLangCode: this.currentLangCode,
                callback: (data) => {
                    const sub = this.modalService.onHidden.subscribe(() => {
                        SpinnerService.start(this.spinnerSelector);
                        sub.unsubscribe();
                        this.page.currentPage = this.getPage();
                        this.fireSearchData();

                        if (this.isViewTree) {
                            setTimeout(() => {
                                this.marginService.addSubject.next(data);
                            });
                        }
                    });
                }
            }
        });

        this.appModal.modals = this.bsModalRef;
    }

    expandArtGrp(event) {
        const id = event.parentId;

        if (!id) return;

        SpinnerService.start(this.spinnerSelector);

        this.subs.sink = this.marginService.searchChildById(id)
            .pipe(
                finalize(() => {
                    SpinnerService.stop(this.spinnerSelector);
                })
            )
            .subscribe((data: any) => {
                if (event.callback) {
                    const items = (data || []).map(item => {
                        item = new MarginArticleGroupModel(item);
                        item.sagArticleGroupDescDisplay = item.getSagArticleGroupDesc(this.currentLangCode);
                        return item;
                    });

                    event.callback(items);
                }
            });
    }

    openImportModal() {
        this.importModalRef = this.modalService.show(MarginImportModalComponent, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
            initialState: {
                importObserver: (file) => this.marginService.importArticleGroupData(file),
                onUploadComplete: () => this.reloadData(),
                isShowExport: false,
                isShowDownloadTemplate: false,
                title: `${this.translateService.instant('COMMON_LABEL.IMPORT')} (CSV)`
            },
        });

        this.appModal.modals = this.importModalRef;
    }

    reloadData() {
        this.filterData();
    }

    exportData({ type }) {
        let { path, filename } = this.exportTypeProps[type];
        this.marginService.exportArticleGroupData(path, filename).subscribe();
    }

    filterData(page?: TablePage) {
        this.page.currentPage = page && page.currentPage || 1;
        this.fireSearchData();
    }

    private fireSearchData() {
        this.searchModel.page = this.page.currentPage - 1;

        if (!this.isSearchEmpty()) {
            this.isViewTree = false;
            this.searchByQueries();
        } else {
            this.isViewTree = true;
            this.searchWithRoot();
        }
    }

    private handleDelete(articleGroup: MarginArticleGroupModel, callback?) {
        SpinnerService.start(this.spinnerSelector);
        let request;

        if (articleGroup.hasChild) {
            request = this.marginService.unMapArtGroup(articleGroup.id);
        } else {
            request = this.marginService.deleteArtGroup(articleGroup.id);
        }

        this.subs.sink = request
            .pipe(
                finalize(() => SpinnerService.stop(this.spinnerSelector))
            )
            .subscribe(data => {
                if (callback) {
                    callback(data);
                } else {
                    this.page.currentPage = this.getPage(true);
                    this.fireSearchData();
                }
            });
    }

    private searchByQueries() {
        SpinnerService.start(this.spinnerSelector);
        this.marginService.searchArticleGroup(this.searchModel)
            .pipe(
                finalize(() => {
                    SpinnerService.stop(this.spinnerSelector);
                })
            )
            .subscribe(data => {
                this.handleSearchSuccess(data);
            });
    }

    private searchWithRoot() {
        SpinnerService.start(this.spinnerSelector);
        this.marginService.searchArticleGroupByRoot({ page: this.searchModel.page || 0, size: this.searchModel.size || APP_DEFAULT_PAGE_SIZE })
            .pipe(
                finalize(() => {
                    SpinnerService.stop(this.spinnerSelector);
                })
            )
            .subscribe(data => {
                this.handleSearchSuccess(data);
            });
    }

    private handleSearchSuccess(data) {
        if (data) {
            const items = (data.content || []).map(item => {
                item = new MarginArticleGroupModel(item);
                item.sagArticleGroupDescDisplay = item.getSagArticleGroupDesc(this.currentLangCode);
                return item;
            });

            this.articleGroups = items;
            this.page.currentPage = (data.number || 0) + 1;
            this.page.totalItems = data.totalElements || 0;
            this.page.itemsPerPage = data.size || 10;

            this.numberOfElements = data.numberOfElements;
            this.currentPage = data.number;
        } else {
            this.articleGroups = [];
            this.numberOfElements = 0;
            this.currentPage = 0;
            this.page.totalItems = 0;
        }

        this.marginService.articleGroupsSubject.next(this.articleGroups);
    }

    private isSearchEmpty() {
        return values(this.searchModel).every(isEmpty);
    }

    private getPage(deleteMode = false) {
        if (this.numberOfElements === 1 && deleteMode) {
            const prevPage = this.page.currentPage - 1;
            return prevPage > 0 ? prevPage : 0;
        }

        return this.page.currentPage;
    }
}
