import {
    Component,
    OnInit,
    Input,
    Output,
    EventEmitter,
    ViewChild,
    ElementRef,
} from '@angular/core';

import { CustomerService } from 'src/app/home/services/customer/customer.service';

@Component({
    selector: "licence-delete-dialog",
    templateUrl: './licence-delete-dialog.component.html',
    styleUrls: ['./licence-delete-dialog.component.scss'],
})
export class LicenceDeleteDialogComponent {
    @Input() readonly licence: any;
    @Output() deleteSuccessEvent = new EventEmitter();
    @Output() closeModal = new EventEmitter();
    @ViewChild('closeBtn', { static: true }) closeBtn: ElementRef;

    public err: any;

    constructor(private customerService: CustomerService) { }

    deleteLicence() {
        this.customerService.deleteLicense(this.licence.id).subscribe(
            () => {
                this.closeModal.emit();
                this.deleteSuccessEvent.emit();
            },
            () => {
                this.err = 'CUSTOMER.LICENSE.DELETE_UNSUCCESSFUL';
            }
        );
    }

    cancelDeleteAction() {
        this.closeModal.emit();
    }
}
