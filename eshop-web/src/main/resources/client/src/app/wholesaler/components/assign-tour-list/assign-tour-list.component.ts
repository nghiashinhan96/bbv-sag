import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

import { BsModalService } from 'ngx-bootstrap/modal';
import { SagConfirmationBoxComponent } from 'sag-common';
import { DAY_IN_WEEK } from 'src/app/core/conts/app.constant';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { DateUtil } from 'src/app/core/utils/date.util';

import { TourAssignModel } from '../../models/tour-assign.model';
import { PROFILE_EDIT_MODE } from '../../services/constant';
import { DeliveryProfileService } from '../../services/delivery-profile.service';
import { DeliveryProfileEditModalComponent } from '../delivery-profile-edit-modal/delivery-profile-edit-modal.component';

@Component({
    // tslint:disable-next-line: component-selector
    selector: '[connect-assign-tour-list]',
    templateUrl: './assign-tour-list.component.html',
    styleUrls: ['./assign-tour-list.component.scss']
})
export class AssignTourListComponent implements OnInit {

    @Input() set tourAssignData(data: TourAssignModel[]) {
        this.sortedTourAssigns = this.sortTour(data);
    };
    @Input() profileId: string;
    @Output() reloadTourList = new EventEmitter();

    sortedTourAssigns: TourAssignModel[] = [];

    constructor(
        private modalService: BsModalService,
        private translateService: TranslateService,
        private deliveryProfileService: DeliveryProfileService,
        private appModal: AppModalService) { }

    ngOnInit() {
    }


    onCloseEditTourModal() {
        this.reloadTourList.emit();
    }

    creatNewTour() {
        this.appModal.modals = this.modalService.show(DeliveryProfileEditModalComponent, {
            initialState: {
                editMode: PROFILE_EDIT_MODE.NEW_TOUR,
                title: 'DELIVERY_PROFILE.EDIT_TOUR',
                profileId: this.profileId,
                onClose: () => {
                    this.onCloseEditTourModal();
                }
            },
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    onEditTour(tour: TourAssignModel) {
        this.appModal.modals = this.modalService.show(DeliveryProfileEditModalComponent, {
            initialState: {
                tourId: tour.id,
                profileId: this.profileId,
                editMode: PROFILE_EDIT_MODE.EDIT_TOUR,
                title: 'DELIVERY_PROFILE.EDIT_TOUR',
                onClose: () => {
                    this.onCloseEditTourModal();
                }
            },
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    onDeleteTour(tour: TourAssignModel) {
        const timeMessage = this.translateService.instant(`DAY_IN_WEEK.${tour.supplierTourDay.substr(0, 3)}`) + ` ${tour.supplierTourTime}`
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'DELIVERY_PROFILE.DELETE_TOUR_CONFIRM',
                messageParams: { time: timeMessage },
                okButton: 'COMMON_LABEL.YES',
                showHeaderIcon: false,
                showCloseButton: true,
                close: () => {
                    this.deliveryProfileService.deleteTour(tour.id).subscribe(res => {
                        this.reloadTourList.emit();
                    }, error => {
                        console.log(error);
                    })
                }
            }
        });

    }

    sortTour(data: TourAssignModel[] = []) {
        const groupByDay = data.reduce((group, tour) => {
            if (!group[tour.supplierTourDay]) {
                group[tour.supplierTourDay] = [];
            }
            group[tour.supplierTourDay].push(tour);
            return group;
        }, {});

        let sortedTour = [];
        const days = ['ALL', ...DAY_IN_WEEK];
        days.forEach(day => {
            (groupByDay[day] || []).sort((tourA: TourAssignModel, tourB: TourAssignModel) => {
                const timeA = DateUtil.parseTime(tourA.supplierTourTime);
                const timeB = DateUtil.parseTime(tourB.supplierTourTime);
                if (!timeA || !timeB) {
                    return 0;
                }
                return timeA > timeB ? 1 : -1;
            })
            sortedTour = [...sortedTour, ...(groupByDay[day] || [])];
        })
        return sortedTour;
    }
}
