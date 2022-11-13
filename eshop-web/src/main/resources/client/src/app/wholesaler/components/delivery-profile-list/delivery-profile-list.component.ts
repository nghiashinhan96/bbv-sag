import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

import { finalize } from 'rxjs/operators';
import { SagCommonSortObj, SagConfirmationBoxComponent } from 'sag-common';

import { SpinnerService } from 'src/app/core/utils/spinner';
import { DeliveryProfileModel, DeliveryProfileRequestModel } from '../../models/delivery-profile.model';
import { TourAssignModel } from '../../models/tour-assign.model';
import { DeliveryProfileService } from '../../services/delivery-profile.service';
import {
    TablePage
} from 'sag-table';
import { BsModalService } from 'ngx-bootstrap/modal';
import { DeliveryProfileEditModalComponent } from '../delivery-profile-edit-modal/delivery-profile-edit-modal.component';
import { PROFILE_EDIT_MODE } from '../../services/constant';
import { Constant } from 'src/app/core/conts/app.constant';
import { AppModalService } from 'src/app/core/services/app-modal.service';

@Component({
    selector: 'connect-delivery-profile-list',
    templateUrl: './delivery-profile-list.component.html',
    styleUrls: ['./delivery-profile-list.component.scss']
})
export class DeliveryProfileListComponent implements OnInit {
    sort: SagCommonSortObj;
    keywords: string;

    private readonly SIZE = 10;

    deliveryProfileData: DeliveryProfileModel[] = null;
    tourAssignData: TourAssignModel[] = [];
    expandingProfileId: string;

    searchRequest: DeliveryProfileRequestModel;
    page = new TablePage();
    numberOfElements: number;

    constructor(
        private cdRef: ChangeDetectorRef,
        public deliveryProfileService: DeliveryProfileService,
        public modalService: BsModalService,
        private appModal: AppModalService) { }

    ngOnInit() {
        this.resetSearchDeliveryProfile();
    }

    tableCallback(data) {
        this.page = {
            currentPage: data.page,
            totalItems: data.totalItems || 0,
            itemsPerPage: data.itemsPerPage || 0,
        }
    }

    searchDP() {
        SpinnerService.start();
        this.deliveryProfileService.getDeliverProfileList(this.searchRequest, this.page)
            .pipe(
                finalize(() => SpinnerService.stop())
            ).subscribe((data: any) => {
                this.deliveryProfileData = data.content.map(dp => new DeliveryProfileModel(dp));
                this.numberOfElements = data.numberOfElements;

                if (this.tableCallback) {
                    this.tableCallback({
                        page: data.pageable.pageNumber + 1,
                        totalItems: data.totalElements,
                        itemsPerPage: data.size,
                    })
                }
            }, error => {
                this.deliveryProfileData = []
                this.tableCallback({
                    page: 0
                })
            });
    }

    sortData(sortObject: SagCommonSortObj) {
        this.resetFilter();
        this.resetPage();
        if (sortObject.field) {
            this.searchRequest[sortObject.field] = sortObject.direction == Constant.ASC_LOWERCASE;
        }
        this.searchDP();
    }

    openCreateProfile() {
        this.appModal.modals = this.modalService.show(DeliveryProfileEditModalComponent, {
            initialState: {
                editMode: PROFILE_EDIT_MODE.NEW_PROFILE,
                title: 'DELIVERY_PROFILE.NEW_PROFILE_TOUR',
                onClose: () => {
                    this.onCloseProfileEditModal();
                }
            },
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    onEditProfile(profile: DeliveryProfileModel) {
        this.appModal.modals = this.modalService.show(DeliveryProfileEditModalComponent, {
            initialState: {
                profileId: profile.id,
                editMode: PROFILE_EDIT_MODE.EDIT_PROFILE,
                title: 'DELIVERY_PROFILE.EDIT_PROFILE',
                onClose: () => {
                    this.onCloseProfileEditModal();
                }
            },
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    onDeleteProfile(profile) {
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'DELIVERY_PROFILE.DELETE_PROFILE_CONFIRM',
                messageParams: { name: profile.name || '' },
                okButton: 'COMMON_LABEL.YES',
                showHeaderIcon: false,
                showCloseButton: true,
                close: () => {
                    this.deliveryProfileService.deleteDeliveryProfile(profile.id).subscribe(res => {
                        let page = this.page.currentPage;

                        if(this.numberOfElements === 1) {
                            page = this.page.currentPage - 1 > 0 ? this.page.currentPage - 1 : 1;
                        }

                        this.resetSearchDeliveryProfile(page - 1);
                    }, ({ error }) => {
                        this.handleDeleteProfileError(error)
                    })
                }
            }
        });
    }

    handleDeleteProfileError(error) {
        this.appModal.modals = this.modalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'DELIVERY_PROFILE.UNABLE_DELETE_ASSIGNED_PROFILE',
                okButton: 'COMMON_LABEL.YES',
                showCancelButton: false,
                showHeaderIcon: false,
                showCloseButton: true,
            }
        });
    }

    reloadTourList() {
        if (!this.expandingProfileId) {
            return;
        }
        SpinnerService.start();
        this.deliveryProfileService.getDeliveryProfile(this.expandingProfileId)
            .pipe(
                finalize(() => {
                    SpinnerService.stop();
                })
            )
            .subscribe(res => {
                const profile = new DeliveryProfileModel(res);
                if (this.deliveryProfileData) {
                    let dpIndex = this.deliveryProfileData.findIndex(dp => dp.id == profile.id);
                    if (dpIndex >= 0) {
                        const newDeliveryProfileData = [...this.deliveryProfileData];
                        newDeliveryProfileData.splice(dpIndex, 1);
                        newDeliveryProfileData.splice(dpIndex, 0, profile);
                        this.deliveryProfileData = [...newDeliveryProfileData];
                        this.tourAssignData = profile.wssDeliveryProfileToursDtos;
                    }
                }
            }, error => {
                console.log(error);
            })
    }

    expandProfile(profile: DeliveryProfileModel) {
        if (this.expandingProfileId !== profile.id) {
            this.expandingProfileId = profile.id;
            this.tourAssignData = profile.wssDeliveryProfileToursDtos;
            return;
        }
        this.expandingProfileId = null;
    }

    pageChanged(currentPage: number) {
        this.page.currentPage = currentPage - 1;
        this.searchDP();
    }

    resetPage(page = 0) {
        this.page = new TablePage;
        this.page.currentPage = page;
        this.page.itemsPerPage = this.SIZE;
    }

    resetFilter() {
        this.searchRequest = new DeliveryProfileRequestModel();
    }

    resetSearchDeliveryProfile(page = 0) {
        this.resetPage(page);
        this.resetFilter();
        this.searchDP();
    }

    onCloseProfileEditModal() {
        const page = this.page.currentPage - 1;
        this.resetSearchDeliveryProfile(page);
    }
}
