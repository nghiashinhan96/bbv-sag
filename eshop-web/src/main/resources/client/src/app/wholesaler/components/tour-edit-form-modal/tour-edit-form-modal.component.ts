import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';
import { SagMessageData } from 'sag-common';
import { DAY_IN_WEEK } from 'src/app/core/conts/app.constant';
import { DateUtil } from 'src/app/core/utils/date.util';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { Validator } from 'src/app/core/utils/validator';
import { TourModel } from '../../models/tour.model';
import { TIME_REGEX } from '../../services/constant';
import { TourService } from '../../services/tour.service';

@Component({
    selector: 'connect-tour-edit-form-modal',
    templateUrl: './tour-edit-form-modal.component.html',
    styleUrls: ['./tour-edit-form-modal.component.scss']
})
export class TourEditFormModalComponent implements OnInit {

    @Input() tourId: string;
    tourForm: FormGroup;
    tourData: TourModel;
    daysInWeek = DAY_IN_WEEK;
    timePickerToggle = {};
    hourList = DateUtil.buildDataTimePicker();

    message: SagMessageData;

    onClose: () => void;

    constructor(
        public bsModalRef: BsModalRef,
        private fb: FormBuilder,
        private tourService: TourService
    ) { }

    ngOnInit() {
        this.resetTimePickerToggle();
        this.getTourData();
    }

    resetTimePickerToggle() {
        DAY_IN_WEEK.forEach(day => this.timePickerToggle[day] = false);
    }

    getTourData() {
        if (this.tourId) {
            SpinnerService.start();
            this.tourService.getTour(this.tourId)
                .pipe(
                    finalize(() => {
                        SpinnerService.stop();
                    })
                )
                .subscribe(tourData => {
                    this.tourData = new TourModel(tourData);
                    this.buildForm();
                });
            return;
        }
        this.buildForm();
    }

    buildForm() {
        const formGroup: any = {};
        // tslint:disable-next-line: no-string-literal
        formGroup['tourName'] = new FormControl(this.tourData && this.tourData.name || '', Validators.required);
        DAY_IN_WEEK.forEach((day) => {
            let tourIndex = -1;
            if (this.tourData) {
                tourIndex = this.tourData.wssTourTimesDtos.findIndex(tour => tour.weekDay === day);
            }
            let time = ''
            if (tourIndex >= 0) {
                time = this.tourData && this.tourData.wssTourTimesDtos[tourIndex] && this.tourData.wssTourTimesDtos[tourIndex].departureTime || '';
            }
            formGroup[day] = new FormControl(time, Validator.invalidValueValidator(TIME_REGEX, true));
        });
        this.tourForm = new FormGroup(formGroup);
    }

    timeFormControl(day) {
        return this.tourForm && this.tourForm.get(day);
    }

    onToggleTimePicker(day) {
        const prevShowStatus = this.timePickerToggle[day];
        this.resetTimePickerToggle();
        this.timePickerToggle[day] = !prevShowStatus;
    }

    onSetHour(hour, day) {
        this.onToggleTimePicker(day);
        this.tourForm.get(day).setValue(hour.formatText);
    }

    buildTourDto() {
        const formValue = this.tourForm.getRawValue();
        const tour = new TourModel();
        tour.id = this.tourId || '0';
        tour.name = formValue.tourName;
        tour.wssTourTimesDtos = [];
        DAY_IN_WEEK.forEach(day => {
            tour.wssTourTimesDtos.push({
                departureTime: formValue[day],
                weekDay: day
            })
        })
        return tour;
    }

    onSubmit() {
        if (this.tourForm.invalid) {
            return;
        }
        SpinnerService.start('connect-tour-edit-form-modal');
        const tour: TourModel = this.buildTourDto();
        let tourObserver = this.tourId ? this.tourService.updateTour(tour) : this.tourService.createTour(tour);
        tourObserver
            .pipe(
                finalize(() => {
                    SpinnerService.stop('connect-tour-edit-form-modal');
                })
            )
            .subscribe(res => {
                this.onClose();
                this.bsModalRef.hide();
            }, ({ error }) => {
                this.handleError(error);
            })
    }

    handleError(error) {
        if (!error) {
            this.message = { type: 'ERROR', message: 'BRANCHES.BRANCH_DETAIL_FORM.CREATE_COMMON_ERROR_MESSAGE' }
            return;
        }
        switch (error.error_code) {
            case 'DUPLICATED_TOUR_NAME':
                this.message = { type: 'ERROR', message: 'WSS.CREATE_DUPLICATE_TOUR' }
                break;
            default:
                this.message = { type: 'ERROR', message: 'BRANCHES.BRANCH_DETAIL_FORM.CREATE_COMMON_ERROR_MESSAGE' }
                break;
        }
    }
}
