import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TimepickerConfig } from 'ngx-bootstrap/timepicker';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

export function getTimepickerConfig(): TimepickerConfig {
    return Object.assign(new TimepickerConfig(), {
        hourStep: 1,
        minuteStep: 5,
        showMeridian: false,
        readonlyInput: false,
        mousewheel: true,
        showMinutes: true,
        showSeconds: false
    });
}

@Component({
    selector: 'connect-opening-time-picker',
    templateUrl: './opening-time-picker.component.html',
    styleUrls: ['./opening-time-picker.component.scss'],
    providers: [{ provide: TimepickerConfig, useFactory: getTimepickerConfig }]
})
export class OpeningTimePickerComponent implements OnInit, OnDestroy {
    private destroy$ = new Subject<boolean>();

    @Input() parentForm: FormGroup;
    @Input() controlName = 'timer';
    @Input() size: 'small' | 'normal' | 'large' = 'normal';
    @Output() changed = new EventEmitter<any>();

    timeForm: FormGroup;
    timer: Date = null;

    constructor(private fb: FormBuilder) { }

    ngOnInit() {
        this.timer = this.parentForm.get(this.controlName).value;
        this.timeForm = this.fb.group({
            time: [this.timer],
        });

        this.timeForm.valueChanges.pipe(
            takeUntil(this.destroy$)
        ).subscribe(val => {
            this.updateTimer(val);
        });
    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
    }

    updateTimer(value) {
        const selectedTime = value.time || null;
        const curentTimer = this.parentForm.get(this.controlName).value;
        this.timer = selectedTime;
        if (!!selectedTime && selectedTime !== curentTimer) {
            this.parentForm.get(this.controlName).setValue(selectedTime);
            if (this.changed) {
                this.changed.emit(selectedTime);
            }
        }
    }

    onReset() {
        this.timer = null;
        this.parentForm.get(this.controlName).setValue(null);
        this.timeForm.get('time').setValue(null);
        if (this.changed) {
            this.changed.emit(null);
        }
    }
}
