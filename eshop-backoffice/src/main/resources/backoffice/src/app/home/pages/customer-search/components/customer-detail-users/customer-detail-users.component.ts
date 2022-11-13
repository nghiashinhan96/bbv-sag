import {
    Component,
    OnInit,
    Input,
    SimpleChanges,
    OnChanges,
    ViewChild,
    ElementRef,
    TemplateRef,
} from '@angular/core';

import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { UserFormModel } from '../../../user-management/models/user-form.model';
import { UserRequestModel } from 'src/app/home/models/user-request.model';
import { UserListComponent } from '../../../user-management/components/user-list/user-list.component';

@Component({
    selector: 'backoffice-customer-detail-users',
    templateUrl: './customer-detail-users.component.html',
    styleUrls: ['./customer-detail-users.component.scss'],
})
export class CustomerDetailUsersComponent implements OnInit, OnChanges {
    @Input() userQuery: UserRequestModel;

    @ViewChild(UserListComponent, { static: true }) userList: UserListComponent;
    @ViewChild('collapseBtn', { static: true }) collapseBtn: ElementRef;
    @ViewChild('userFormModal', { static: true }) userFormModal: ElementRef;

    isFormExpanded = false;

    userForm: UserFormModel;
    private userFormModalRef: BsModalRef = null;

    constructor(private modalService: BsModalService) { }

    ngOnInit() {
        if (this.userQuery) {
            this.userList.resetUsers(this.userQuery);
        }
    }

    ngOnChanges(changes: SimpleChanges) {
        this.buildOrUpdateUserForm();
    }

    onAddNewUserDone() {
        this.userList.reloadUsers();
        this.closeUserFormModal();
    }

    onOpenUserFormModal() {
        // this.modal.showModal(this.userFormModal);
        this.userFormModalRef = this.modalService.show(this.userFormModal, {
            class: 'modal-user-form  modal-lg',
            ignoreBackdropClick: false,
        });
    }

    buildOrUpdateUserForm() {
        this.userForm = new UserFormModel();

        if (this.userQuery) {
            this.userForm.affiliate = this.userQuery.affiliate;
            this.userForm.customerNumber = this.userQuery.customerNumber;
        }
    }

    closeUserFormModal() {
        this.userFormModalRef.hide();
    }

    public open() {
        this.isFormExpanded = true;
    }
}
