import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subscription } from 'rxjs/internal/Subscription';
import { SagValidator } from '../../utils/validator.util';

@Component({
    selector: 'sag-common-control-message',
    templateUrl: 'sag-control-message.component.html'
})
export class SagControlMessagesComponent implements OnInit, OnDestroy {
    @Input() control: FormControl;
    @Input() custom = true;

    subValueChanges: Subscription;
    subStatusChanges: Subscription;

    error: {
        message: string;
        param: any;
    };

    constructor() {
    }

    ngOnInit() {
        if (!this.control) {
            return;
        }
        this.subValueChanges = this.control.valueChanges.subscribe(() => {
            this.error = this.getError();
        });
        this.subStatusChanges = this.control.statusChanges.subscribe(() => {
            this.error = this.getError();
        });
    }

    ngOnDestroy() {
        if (this.subValueChanges) {
            this.subValueChanges.unsubscribe();
        }
        if (this.subStatusChanges) {
            this.subStatusChanges.unsubscribe();
        }
    }

    private getError() {
        if (this.control) {
            for (const error in this.control.errors) {
                if (this.control.errors.hasOwnProperty(error) && this.control.dirty && this.control.invalid) {
                    return SagValidator.getValidatorErrorMessage(error, this.control.errors[error]);
                }
            }
            return null;
        }
        return null;
    }
}
