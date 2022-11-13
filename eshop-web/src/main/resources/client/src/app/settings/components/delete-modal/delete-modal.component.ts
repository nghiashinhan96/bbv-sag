import { Component, OnInit } from '@angular/core';
import { AdminManagementUser } from '../../models/normal-admin/user-admin-magement.model';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { NormalAdminService } from '../../services/normal-admin.service';
import { ResponseMessage } from 'src/app/core/models/response-message.model';

@Component({
    selector: 'connect-delete-modal',
    templateUrl: './delete-modal.component.html',
    styleUrls: ['./delete-modal.component.scss']
})
export class DeleteModalComponent implements OnInit {
    userInfo: AdminManagementUser;
    responseMessage: ResponseMessage;

    constructor(public modelRef: BsModalRef, private normalAdminService: NormalAdminService) { }

    ngOnInit() {

    }

    confirmDelete() {
        this.normalAdminService.deleteUser(this.userInfo.id).subscribe(() => {
            this.responseMessage = new ResponseMessage({ key: 'SETTINGS.MESSAGE_SUCCESSFUL', isError: false });
            setTimeout(() => {
                this.modelRef.hide();
            }, 3000);
        }, error => this.responseMessage = new ResponseMessage({ key: 'SETTINGS.DELETE_FAILURE', isError: true }));

    }
}
