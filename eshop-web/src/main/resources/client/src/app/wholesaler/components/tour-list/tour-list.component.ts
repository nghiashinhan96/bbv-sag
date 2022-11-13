import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';
import { SagConfirmationBoxComponent } from 'sag-common';
import { SagTableColumn } from 'sag-table';
import { Constant } from 'src/app/core/conts/app.constant';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { TourModel, TourRequestModel } from '../../models/tour.model';
import { TourService } from '../../services/tour.service';
import { TourEditFormModalComponent } from '../tour-edit-form-modal/tour-edit-form-modal.component';
@Component({
    selector: 'connect-tour-list',
    templateUrl: './tour-list.component.html',
    styleUrls: ['./tour-list.component.scss']
})
export class TourListComponent implements OnInit {
    @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;
    @ViewChild('colTime', { static: true }) colTime: TemplateRef<any>;

    private readonly SIZE = 10;
    columns = [];
    tableCallback = null;
    tableRequest = null;
    tourList: TourModel[] = [];
    requestDto: TourRequestModel;
    numberOfElements: number;

    constructor(
        public tourService: TourService,
        public modalService: BsModalService,
        private appModal: AppModalService) { }

    ngOnInit() {
        this.buildColumns();
        this.tourService.generateWeekDayTrans();
    }

    getTourData() {
        SpinnerService.start();
        this.tourService.getTourList(this.requestDto, { page: this.tableRequest.page - 1, size: this.SIZE })
            .pipe(
                finalize(() => SpinnerService.stop())
            ).subscribe((data: any) => {
                this.tourList = data.content.map(tour => new TourModel(tour));
                this.numberOfElements = data.numberOfElements;

                if (this.tableCallback) {
                    this.tableCallback({
                        rows: this.tourList,
                        page: data.number + 1,
                        totalItems: data.totalElements,
                        itemsPerPage: data.size,
                    })
                }
            }, error => {
                this.tableCallback({
                    rows: [],
                    page: 0
                })
            });
    }

    buildColumns() {
        this.columns = [
            {
                id: 'name',
                i18n: 'WSS.TOUR_NAME',
                width: '30%',
                sortable: true
            },
            {
                id: 'time',
                i18n: 'WSS.TIME',
                type: 'select',
                width: '60%',
                cellTemplate: this.colTime,
                sortable: false
            },
            {
                id: 'action',
                i18n: ' ',
                filterable: false,
                sortable: false,
                cellTemplate: this.colActions,
            },
        ] as SagTableColumn[];
    }

    deleteTour(id) {
        SpinnerService.start();
        this.tourService.removeTour(id)
            .subscribe(res => {
                SpinnerService.stop();
                if (this.numberOfElements === 1) {
                    this.tableRequest.page = this.tableRequest.page - 1 > 0 ? this.tableRequest.page - 1 : 1;
                }

                this.getTourData();
            }, ({ error }) => {
                SpinnerService.stop();
                this.handleDeleteTourError(error);
            });
    }

    handleDeleteTourError(error) {
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'WSS.UNABLE_DELETE_ASSIGNED_TOUR',
                okButton: 'COMMON_LABEL.YES',
                showCancelButton: false,
            }
        });
    }

    confirmDeleteTour(tour) {
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'WSS.DELETE_TOUR',
                messageParams: { name: tour.name },
                okButton: 'COMMON_LABEL.YES',
                cancelButton: 'COMMON_LABEL.NO',
                close: () => {
                    this.deleteTour(tour.id);
                }
            }
        });
    }

    handleSearchRequest() {
        const { sort } = this.tableRequest;
        this.requestDto = new TourRequestModel();

        this.requestDto.orderDescByTourName = sort.direction ? sort.direction !== Constant.ASC_LOWERCASE : false;

    }

    searchTableData({ request, callback }) {
        this.tableCallback = callback;
        this.tableRequest = request;
        this.handleSearchRequest();
        this.getTourData();
    }

    openCreateTour(tourId?) {
        this.appModal.modals = this.modalService.show(TourEditFormModalComponent, {
            initialState: {
                tourId,
                onClose: () => {
                    this.getTourData();
                }
            },
            class: 'modal-user-form  modal-xl',
            ignoreBackdropClick: false,
        });
    }
}
