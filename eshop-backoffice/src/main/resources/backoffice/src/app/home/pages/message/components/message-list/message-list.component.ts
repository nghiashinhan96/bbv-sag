
import { Component, OnInit, ViewChild, TemplateRef, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

import { TranslateService } from '@ngx-translate/core';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { SagTableControl, SagTableColumn } from 'sag-table';

import { MessageService } from '../../services/message.service';
import { MessageSearchCriteriaModel } from '../../models/message-search-criteria.model';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { MessageDeleteModalComponent } from '../message-delete-modal/message-delete-modal.component';

@Component({
    selector: 'backoffice-message-list',
    styleUrls: ['./message-list.component.scss'],
    templateUrl: './message-list.component.html',
})
export class MessageListComponent implements OnInit, SagTableControl {
    messages: any;
    types: Array<any> = [];
    areas: Array<any> = [];
    // subAreas: Array<any>;
    selectedArea: any;
    deletingMessage: any;
    statusOptions: Array<any>;
    deleteRespondMessage: NotificationModel;
    searchModel: MessageSearchCriteriaModel;
    currentPage: number;
    previousSelectedAreaValue: string;

    columns = [];
    tableRequest: any;
    tableCallback: any;

    @ViewChild('colType', { static: true }) colType: TemplateRef<any>;
    @ViewChild('colArea', { static: true }) colArea: TemplateRef<any>;
    @ViewChild('colSubArea', { static: true }) colSubArea: TemplateRef<any>;
    @ViewChild('colStatus', { static: true }) colStatus: TemplateRef<any>;

    @ViewChild('colEdit', { static: true }) colEdit: TemplateRef<any>;
    @ViewChild('colDelete', { static: true }) colDelete: TemplateRef<any>;

    // @ViewChild('deletingModal', { static: true }) deletingModal: ElementRef;

    private deletingModalRef: BsModalRef;

    readonly sortField = {
        title: 'orderDescByTitle',
        type: 'orderDescByType',
        area: 'orderDescByArea',
        subArea: 'orderDescBySubArea',
        dateValidFrom: 'orderDescByDateValidFrom',
        dateValidTo: 'orderDescByDateValidTo'
    };

    constructor(
        private messageService: MessageService,
        private translateService: TranslateService,
        private modalService: BsModalService,
        private router: Router) { }

    ngOnInit() {
        this.getFilteringOptions();
    }

    searchTableData({ request, callback }): void {
        this.tableCallback = callback;
        this.tableRequest = request;
        this.getMessages();
    }

    setTableData() {
        if (!this.messages) {
            return;
        }
        if (!this.tableCallback || !this.tableRequest) {
            return;
        }

        const { content, pageable, size, totalPages, totalElements } = this.messages;
        this.tableCallback({
            rows: content,
            itemsPerPage: size,
            totalItems: totalElements,
            page: pageable.pageNumber + 1
        });
    }

    buildColumns() {
        const isShowSubAreasSelection = this.selectedArea && this.selectedArea.subAreas && this.selectedArea.subAreas.length > 2;
        if (!this.types || !this.areas || !this.statusOptions) {
            return;
        }

        this.columns = [
            {
                id: 'title',
                i18n: 'MESSAGE.TITLE',
                filterable: true,
                sortable: true,
                defaultValue: this.searchModel.title ? this.searchModel.title : '',
                width: '12%',
            },
            {
                id: 'type',
                i18n: 'MESSAGE.BLOCK_TYPE',
                filterable: true,
                sortable: true,
                type: 'select',
                selectSource: this.types,
                cellTemplate: this.colType,
                width: '12%',
            },
            {
                id: 'area',
                i18n: 'MESSAGE.PAGE',
                filterable: true,
                sortable: true,
                type: 'select',
                selectSource: this.areas,
                cellTemplate: this.colArea,
                cellClass: 'align-middle',
                width: '12%',
            },
            {
                id: 'subArea',
                i18n: 'MESSAGE.POSITION',
                cellClass: 'align-middle',
                type: 'select',
                filterable: isShowSubAreasSelection,
                selectSource: isShowSubAreasSelection ? this.selectedArea.subAreas : [],
                defaultValue: null,
                cellTemplate: this.colSubArea,
                width: '8%',
            },
            {
                id: 'locationValue',
                i18n: 'MESSAGE.LOCATION_VALUE',
                filterable: true,
                defaultValue: this.searchModel.locationValue ? this.searchModel.locationValue : '',
                cellClass: 'align-middle',
                width: '14%',
            },
            {
                id: 'dateValidFrom',
                i18n: 'MESSAGE.START_DATE',
                sortable: true,
                cellClass: 'align-middle',
                type: 'date',
                width: '11%',
            },
            {
                id: 'dateValidTo',
                i18n: 'MESSAGE.END_DATE',
                sortable: true,
                cellClass: 'align-middle',
                type: 'date',
                width: '11%',
            },
            {
                id: 'active',
                i18n: 'MESSAGE.STATUS',
                filterable: true,
                sortable: false,
                type: 'select',
                selectSource: this.statusOptions,
                cellTemplate: this.colStatus,
                cellClass: 'align-middle',
                width: '10%',
            },
            {
                id: 'edit',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colEdit,
                cellClass: 'align-middle',
            },
            {
                id: 'delete',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colDelete,
                cellClass: 'align-middle',
            },
        ] as SagTableColumn[];
    }

    sort(fieldName: string, sortDesc: boolean) {
        this.resetSortingCriterias();
        const sortField = this.sortField[fieldName];
        this.searchModel[sortField] = sortDesc;
    }

    filter() {
        this.searchModel = new MessageSearchCriteriaModel({
            title: this.searchModel.title,
            type: this.searchModel.type,
            area: this.searchModel.area,
            subArea: this.searchModel.subArea,
            locationValue: this.searchModel.locationValue,
            active: this.searchModel.active,
        });
    }

    // paging(page: number) {
    //     this.currentPage = this.tableRequest.page - 1;
    //     this.searchWithConditionsAndStayAtTheCurrentPage();
    // }

    createMessage() {
        this.router.navigate(['/home/message/create']);
    }

    getShowedText(key: string, isSpecialCase?: boolean) {
        if (isSpecialCase &&
            (key === 'LOGIN_PAGE_1' || key === 'SHOPPING_BASKET_PAGE_1' || key === 'ORDER_CONFIRMATION_PAGE_1' || key === 'ALL')) {
            return 'MESSAGE.EMPTY';
        }
        return 'MESSAGE.' + key;
    }

    getStatusText(active: boolean) {
        return active ? 'MESSAGE.ACTIVE' : 'MESSAGE.INACTIVE';
    }

    selectArea(subArea) {
        if (!this.areas) {
            return;
        }
        this.selectedArea = this.areas.find(item => item.value === this.searchModel.area);
        // If user select new area, then reset the subArea filter
        if (this.searchModel.area !== this.previousSelectedAreaValue) {
            this.searchModel.subArea = null;
            this.previousSelectedAreaValue = this.searchModel.area;
        } else {
            if (subArea) {
                this.searchModel.subArea = subArea.value;
            }
        }
    }

    // for deleting message
    deleteMessage(message: any) {
        this.messageService.delete(message.id).subscribe(res => {
            this.deleteRespondMessage = { messages: ['MESSAGE.DELETE_SUCCESSFUL'], status: true };
            setTimeout(() => {
                this.resetDeletingMessage();
                this.hideDeleteModal();
                this.getMessages();
            }, 2000);
        }, err => {
            this.deleteRespondMessage = { messages: ['MESSAGE.DELETE_FAIL'], status: false };
        });
    }

    handleDeleteEvent(message: any) {
        this.deletingMessage = message;
        setTimeout(() => {
            this.showDeleteModal();
        });
    }

    handleEditEvent(message: any) {
        this.router.navigate([`/home/message/edit/${message.id}`]);
    }

    resetDeletingMessage() {
        this.deleteRespondMessage = null;
    }

    hideDeleteModal() {
        this.deletingModalRef.content.closeModal.unsubscribe();
        this.deletingModalRef.content.confirmDelete.unsubscribe();
        this.deletingModalRef.hide();
    }

    private getMessages() {
        if (!this.searchModel) {
            this.searchModel = MessageSearchCriteriaModel.getEmptyModel();
        }

        this.currentPage = this.tableRequest.page - 1;
        const { filter, sort } = this.tableRequest;

        if (filter) {
            this.searchModel.title = filter.title;
            this.searchModel.type = filter.type ? filter.type.value : null;
            this.searchModel.area = filter.area ? filter.area.value : null;
            this.searchModel.locationValue = filter.locationValue ? filter.locationValue : null;
            this.searchModel.active = filter.active ? filter.active.value : null;

            this.selectArea(filter.subArea);
            this.buildColumns();
            this.filter();
        }

        if (sort) {
            this.sort(sort.field, sort.direction === 'desc');
        }

        this.search(this.searchModel, { page: this.currentPage, size: 10 });
    }

    private search(searchModel: MessageSearchCriteriaModel, page?) {
        this.messageService.search(searchModel, page).subscribe(res => {
            this.messages = res;
            this.setTableData();
        });
    }

    // private searchWithConditionsAndStayAtTheCurrentPage() {
    //     this.search(this.searchModel, { page: this.currentPage, size: 10 });
    // }

    private getFilteringOptions() {
        this.messageService.getFilteringOptions().subscribe(res => {
            const data: any = res;
            this.buildTypeOptions(data.types);
            this.buildAreaAndSubAreaOptions(data.areas);
            this.buildStatusOptions();
            this.buildColumns();
        });
    }

    private buildTypeOptions(types: Array<any>) {
        this.types = [];
        this.types.push({ value: null, label: this.getShowedText('ALL_OPTIONS') });
        types.forEach(item => {
            this.types.push({ value: item.type, label: this.getShowedText(item.type) });
        });
    }

    private buildAreaAndSubAreaOptions(areas: Array<any>) {
        this.areas = [];
        this.areas.push({ value: null, label: this.getShowedText('ALL_OPTIONS'), subAreas: null });
        let subAreas: Array<any> = [];
        areas.forEach(area => {
            subAreas.push({ value: null, label: this.getShowedText('ALL_OPTIONS') });
            area.subAreas.forEach(subArea => {
                subAreas.push({ value: subArea.subArea, label: this.getShowedText(subArea.subArea) });
            });
            area.area === 'ALL' ? this.areas.push({ value: area.area, label: this.getShowedText('ALL_PAGES'), subAreas })
                : this.areas.push({ value: area.area, label: this.getShowedText(area.area), subAreas });
            subAreas = [];
        });
    }

    private buildStatusOptions() {
        this.statusOptions = [];
        this.statusOptions.push({ value: null, label: this.getShowedText('ALL_OPTIONS') });
        this.statusOptions.push({ value: 1, label: this.getShowedText('ACTIVE') });
        this.statusOptions.push({ value: 0, label: this.getShowedText('INACTIVE') });
    }

    private showDeleteModal() {
        this.deletingModalRef = this.modalService.show(MessageDeleteModalComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState: {
                deletingMessage: this.deletingMessage,
                resMessage: this.deleteRespondMessage,
            }
        });

        this.deletingModalRef.content.closeModal.subscribe(res => {
            this.hideDeleteModal();
        });

        this.deletingModalRef.content.confirmDelete.subscribe(res => {
            this.deleteMessage(this.deletingMessage);
        });
        this.resetDeletingMessage();
    }

    private resetSortingCriterias() {
        this.searchModel.orderDescByTitle = null;
        this.searchModel.orderDescByType = null;
        this.searchModel.orderDescByArea = null;
        this.searchModel.orderDescByDateValidFrom = null;
        this.searchModel.orderDescByDateValidTo = null;
    }
}
