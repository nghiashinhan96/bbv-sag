import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgOption } from '@ng-select/ng-select';

import { SubSink } from 'subsink';
import { uniqBy } from 'lodash';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { forkJoin, Observable } from 'rxjs';
import { SagMessageData } from 'sag-common';
import { DAY_IN_WEEK } from 'src/app/core/conts/app.constant';
import { DateUtil } from 'src/app/core/utils/date.util';
import { Validator } from 'src/app/core/utils/validator';

import { DeliveryProfileModel } from '../../models/delivery-profile.model';
import { TourAssignModel } from '../../models/tour-assign.model';
import { TourModel, TourRequestModel } from '../../models/tour.model';
import { PROFILE_EDIT_MODE, TIME_REGEX } from '../../services/constant';
import { DeliveryProfileService } from '../../services/delivery-profile.service';
import { OpeningDayCalendarService } from '../../services/opening-day-calendar.service';
import { TourService } from '../../services/tour.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { finalize } from 'rxjs/operators';
import * as moment from 'moment';

@Component({
    selector: 'connect-delivery-profile-edit-modal',
    templateUrl: './delivery-profile-edit-modal.component.html',
    styleUrls: ['./delivery-profile-edit-modal.component.scss']
})
export class DeliveryProfileEditModalComponent implements OnInit, OnDestroy {

    @Input() editMode: string;
    @Input() profileId: string;
    @Input() tourId: string;
    @Input() title = 'DELIVERY_PROFILE.EDIT_PROFILE_TOUR';
    @Input() onClose: any;

    message: SagMessageData;

    profileData: DeliveryProfileModel;
    isProfileOpen = true;
    profileForm: FormGroup;
    branchList = [];

    tourData: TourAssignModel;

    suppplierTourForm: FormGroup;
    isSupplierTourOpen = true;
    isTourDepartureTimeOpen = false;
    tourTimeList = [];

    fcTourForm: FormGroup;
    isFcTourOpen = true;
    fcTourList = [];
    hourList = DateUtil.buildDataTimePicker();
    durationList = DateUtil.buildDataTimePicker(1);
    isDurationPickerOpen = false;
    durationValue = 0;
    tourDepartureTimeInvalid = false;

    readonly PAGE_SIZE = 20;
    currentPage = 0;
    totalTourPage = 0;

    tourDayList: NgOption[] = [{
        value: 'ALL',
        label: 'COMMON_LABEL.ALL'
    }];

    hintMsg: SagMessageData = {
        message: 'DELIVERY_PROFILE.FORM_MESSAGE.INVALID_WSS_TOUR',
        type: 'INFO'
    }

    private subs = new SubSink();

    constructor(
        public bsModalRef: BsModalRef,
        private fb: FormBuilder,
        private openingDayService: OpeningDayCalendarService,
        private tourService: TourService,
        private deliveryService: DeliveryProfileService
    ) { }
    ngOnInit() {
        this.profileData = new DeliveryProfileModel();
        this.tourData = new TourAssignModel();
        this.buildWeekDayOptions();

        this.getFormOptionData().subscribe(res => {
            this.branchList = res[0];
            this.fcTourList = this.buildFcTourList(res[1])
            if (this.profileId) {
                this.profileData = new DeliveryProfileModel(res[2]);
                if (this.tourId) {
                    this.tourData = this.profileData.wssDeliveryProfileToursDtos.find(tour => tour.id === this.tourId);
                }
            }
            this.handleMode();
        })
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    buildFcTourList(data) {
        if (!data || !data.content) {
            return [];
        }
        this.totalTourPage = data.totalPages || 0;
        return data.content.map(tour => {
            const tourModel = new TourModel(tour);
            return {
                label: tourModel.name,
                value: tourModel.id
            }
        })
    }

    getFormOptionData(): Observable<any> {
        const branches = this.openingDayService.getBranches();
        const tours = this.tourService.getTourList(new TourRequestModel(), { page: 0, size: this.PAGE_SIZE })
        const obsArray = [branches, tours];
        if (this.profileId) {
            obsArray.push(this.deliveryService.getDeliveryProfile(this.profileId))
        }
        return forkJoin(obsArray);
    }

    buildWeekDayOptions() {
        DAY_IN_WEEK.forEach(day => {
            this.tourDayList.push({
                value: day,
                label: `DAY_IN_WEEK.${day.substr(0, 3)}`
            })
        })
    }

    handleMode() {
        switch (this.editMode) {
            case PROFILE_EDIT_MODE.NEW_PROFILE:
                this.buildProfileForm();
                this.buildSupplierTourForm();
                this.buildFcTourForm();
                break;
            case PROFILE_EDIT_MODE.EDIT_PROFILE:
                this.buildProfileForm();
                this.buildSupplierTourForm(true);
                this.buildFcTourForm(true);
                break;
            case PROFILE_EDIT_MODE.NEW_TOUR:
                this.buildProfileForm(true);
                this.buildSupplierTourForm();
                this.buildFcTourForm();
                break;
            case PROFILE_EDIT_MODE.EDIT_TOUR:
                this.buildProfileForm(true);
                this.buildSupplierTourForm();
                this.buildFcTourForm();
                break;
            default:
                break;
        }
    }

    submitByMode(data): Observable<any> {
        switch (this.editMode) {
            case PROFILE_EDIT_MODE.NEW_PROFILE:
                return this.deliveryService.createDeliveryProfile(data);
            case PROFILE_EDIT_MODE.EDIT_PROFILE:
                return this.deliveryService.updateDeliveryProfile(data);
            case PROFILE_EDIT_MODE.NEW_TOUR:
                return this.deliveryService.addTour(data);
            case PROFILE_EDIT_MODE.EDIT_TOUR:
                return this.deliveryService.updateTour(data);
        }
    }

    buildProfileForm(isDisable = false) {
        let branchValue = '';
        if (this.profileData && this.branchList) {
            let branch = this.branchList.find(br => br.code == this.profileData.branchCode);
            if (branch) {
                branchValue = branch.id;
            }
        }
        this.profileForm = this.fb.group({
            profileName: [this.profileData.name || '', Validators.required],
            profileDesc: [this.profileData.description || '', Validators.required],
            branchCode: [branchValue || '', Validators.required],
        });
        if (isDisable) {
            this.profileForm.disable();
        }
    }

    buildSupplierTourForm(isDisable = false) {
        this.suppplierTourForm = this.fb.group({
            day: [this.tourData.supplierTourDay || '', Validators.required],
            supplierDepartTime: [this.tourData.supplierTourTime || '', [Validator.invalidValueValidator(TIME_REGEX, false)]],
        });
        this.subs.sink = this.tourDepartureTime.valueChanges.subscribe(value => {
            this.tourDepartureTimeInvalid = this.tourDepartureTime.invalid;
        })
        if (isDisable) {
            this.suppplierTourForm.disable();
        }
    }

    buildFcTourForm(isDisable = false) {
        this.addDefaultTourToTourList();
        this.fcTourForm = this.fb.group({
            overnight: [this.tourData.isOverNight || false],
            tour: [this.tourData.wssTourId || null],
            duration: [this.tourData.durationDisplayText || '', Validator.invalidValueValidator(TIME_REGEX, true)],
        });
        if (this.tourData.pickupWaitDuration) {
            this.durationValue = this.tourData.pickupWaitDuration;
        }
        if (isDisable) {
            this.fcTourForm.disable();
        }
    }

    addDefaultTourToTourList() {
        if (!this.tourData.wssTourId) {
            return;
        }

        let tourIndex = this.fcTourList.findIndex(tour => tour.value == this.tourData.wssTourId);
        if (tourIndex >= 0) {
            return;
        }

        this.fcTourList.push({
            label: this.tourData.tourName,
            value: this.tourData.wssTourId
        })
    }

    onSetDuration(value: any) {
        this.isDurationPickerOpen = false;
        this.durationValue = DateUtil.durationObjectToSeconds(value);
        this.fcTourForm.patchValue({ duration: value.formatText });
    }

    onSetDepartureTime(value: any) {
        this.isTourDepartureTimeOpen = false;
        this.suppplierTourForm.patchValue({ supplierDepartTime: value.formatText });
    }

    checkValidFcTourForm() {
        if (this.fcTourForm.invalid) {
            return false;
        }
        return Boolean(this.tourName.value) || Boolean(this.tourDuration.value);
    }

    get tourName() {
        return this.fcTourForm.get('tour');
    }

    get tourDuration() {
        return this.fcTourForm.get('duration');
    }

    get tourDepartureTime() {
        return this.suppplierTourForm.get('supplierDepartTime');
    }

    showMessage(msg, isSuccess = false) {
        this.message = { message: msg.error_code ? `DELIVERY_PROFILE.${msg.error_code}` : msg, type: isSuccess ? 'SUCCESS' : 'ERROR' };
        return;
    }

    get isSaveBtnEnable() {
        if (!this.profileForm && !this.suppplierTourForm) {
            return false;
        }

        if (this.profileForm.invalid) {
            return false;
        }

        if (this.editMode !== PROFILE_EDIT_MODE.EDIT_PROFILE) {
            if (this.suppplierTourForm.invalid) {
                return false;
            }
        }
        return true;
    }

    onSubmit() {
        if (!this.isSaveBtnEnable) {
            return;
        }
        
        const durationValue = this.fcTourForm && this.fcTourForm.get('duration').value;
        const tourValue = this.fcTourForm && this.fcTourForm.get('tour').value;
        if (durationValue) {
            const time = moment(durationValue, "HH:mm");
            this.durationValue = DateUtil.durationObjectToSeconds({hour: time.hour(), mins: time.minute()});
        }
        if (this.editMode !== PROFILE_EDIT_MODE.EDIT_PROFILE) {
            if (!this.checkValidFcTourForm()) {
                return this.showMessage('DELIVERY_PROFILE.FORM_MESSAGE.INVALID_WSS_TOUR');
            }
            if(durationValue && this.durationValue == 0) {
                return this.showMessage('DELIVERY_PROFILE.FORM_MESSAGE.INVALID_WSS_TOUR_TIME');
            }
            if (!tourValue && !this.durationValue) {
                return this.showMessage('DELIVERY_PROFILE.FORM_MESSAGE.INVALID_WSS_TOUR');
            }
        }

        const profile = {
            id: this.profileId || null,
            name: this.profileForm.get('profileName').value,
            description: this.profileForm.get('profileDesc').value,
            wssBranchId: this.profileForm.get('branchCode').value,
            supplierTourDay: this.suppplierTourForm.get('day').value || null,
            supplierDepartureTime: this.suppplierTourForm.get('supplierDepartTime').value || null,
            isOverNight: this.fcTourForm.get('overnight').value,
            wssTourId: this.fcTourForm.get('tour').value || null,
            pickupWaitDuration: this.fcTourForm.get('duration').value ? this.durationValue : null,
            wssDeliveryProfileTourId: this.tourId || null
        }

        SpinnerService.start('connect-delivery-profile-edit-modal');
        this.submitByMode(profile)
            .pipe(
                finalize(() => {
                    SpinnerService.stop('connect-delivery-profile-edit-modal');
                })
            )
            .subscribe(res => {
                this.showMessage('COMMON_MESSAGE.SAVE_SUCCESSFULLY', true);
                setTimeout(() => {
                    this.onClose();
                    this.bsModalRef.hide();
                }, 1000)
            }, ({ error }) => {
                this.showMessage(error);
            })
    }

    onFCTourListScrollToEnd() {
        if (this.currentPage == this.totalTourPage - 1) {
            return;
        }
        this.currentPage += 1;
        this.fetchMoreTour();
    }

    fetchMoreTour() {
        this.tourService.getTourList(new TourRequestModel(), { page: this.currentPage, size: this.PAGE_SIZE }).subscribe((res: any) => {
            this.fcTourList = uniqBy([...this.buildFcTourList(res.content), ...this.fcTourList], 'value');
        }, ({ error }) => {
            console.log(error)
        })
    }
}
