import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { BOValidator } from 'src/app/core/utils/validator';

@Component({
    selector: 'backoffice-duration-selection',
    templateUrl: './duration-selection.component.html',
    styleUrls: ['./duration-selection.component.scss']
})
export class DurationSelectionComponent implements OnInit {

    @Input() parentForm: FormGroup;
    @Input() controlName = 'timer';
    @Input() size: 'small' | 'normal' | 'large' = 'normal';
    @Output() changed = new EventEmitter<any>();

    durationForm: FormGroup;
    displayed = '';

    constructor(private formBuilder: FormBuilder) { }

    ngOnInit() {
        this.durationForm = this.formBuilder.group({
            hours: [null, BOValidator.hoursValidator],
            minutes: [null, BOValidator.minutesValidator]
        });

        const currentVal = this.parentForm.get(this.controlName).value;

        if (!!currentVal) {
            this.durationForm.patchValue(currentVal, { emitEvent: false });
            this.updateDisplayedVal(currentVal);
        }
    }

    onReset() {
        this.durationForm.reset({}, { emitEvent: false });
        this.parentForm.get(this.controlName).setValue(null);
        this.changed.emit(null);
        this.displayed = '';
    }

    updateDuration() {
        if (this.durationForm.valid) {
            const val = this.durationForm.value;
            this.updateDisplayedVal(val);
            this.parentForm.get(this.controlName).setValue(val);
            this.hours.setValue(this.sliceTwo(val.hours || 0), { emitEvent: false });
            this.minutes.setValue(this.sliceTwo(val.minutes || 0), { emitEvent: false });
            this.changed.emit(val);
        }
    }

    private updateDisplayedVal(val) {
        const h = this.sliceTwo(val.hours || 0);
        const m = this.sliceTwo(val.minutes || 0);
        this.displayed = `${h}:${m}`;
    }

    private sliceTwo(val) {
        return ('0' + val).slice(-2);
    }

    get days() {
        return this.durationForm.get('days');
    }

    get hours() {
        return this.durationForm.get('hours');
    }

    get minutes() {
        return this.durationForm.get('minutes');
    }
}
