import { Component, OnInit, Input } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { SagMessageData } from 'sag-common';
import { VIN_MAX_LENGTH } from '../../articles.const';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-in-context-vin-popup',
    templateUrl: './vin-popup.component.html',
    styleUrls: ['./vin-popup.component.scss']
})
export class SagInContextVinPopupComponent implements OnInit {
    @Input() vinCode;
    errorMessage: SagMessageData;
    vinForm: FormGroup;

    constructor(
        private bsModalRef: BsModalRef,
        private fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.vinForm = this.fb.group({
            vin: this.vinCode
        });
    }

    cancel() {
        this.vinCode = null;
        this.bsModalRef.hide();
    }

    confirm() {
        this.errorMessage = null;
        const vinCode = (this.vinForm.value.vin || '').trim().replace(/\s/g, '');
        if (!vinCode || vinCode.length !== VIN_MAX_LENGTH) {
            this.errorMessage = {
                type: 'ERROR',
                message: 'SEARCH.VIN_ERROR_MSG.VIN_INVALID'
            };
            return;
        }

        this.vinCode = vinCode;
        this.bsModalRef.hide();
    }
}
