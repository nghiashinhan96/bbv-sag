import { Component, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { Subject } from 'rxjs/internal/Subject';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { UserService } from 'src/app/core/services/user.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { CreateUserModalFormComponent } from '../../components/create-user-modal-form/create-user-modal-form.component';
import { ProfileService } from '../../services/profile.service';
import { map } from 'rxjs/internal/operators/map';
import { UserProfile } from '../../models/user-profile/user-profile.model';
import { switchMap } from 'rxjs/internal/operators/switchMap';
import { NormalAdminService } from '../../services/normal-admin.service';
import { Observable } from 'rxjs';
import { AdminManagementUser } from '../../models/normal-admin/user-admin-magement.model';
import { OciService } from 'src/app/oci/services/oci.service';
import { ActivatedRoute } from '@angular/router';
import { CustomerModel } from 'src/app/core/models/customer.model';
import { SpinnerService } from 'src/app/core/utils/spinner';

@Component({
    selector: 'connect-normal-admin',
    templateUrl: './normal-admin.component.html',
    styleUrls: ['./normal-admin.component.scss']
})
export class NormalAdminComponent implements OnInit, OnDestroy, AfterViewInit {
    bsModalRef: BsModalRef;
    userDetail: UserDetail;
    userProfile: UserProfile;

    allUsersObserver: Observable<AdminManagementUser[]>;

    isOciFlow: boolean;

    private destroy$ = new Subject();

    constructor(
        private userService: UserService,
        private modalService: BsModalService,
        private profileService: ProfileService,
        private normalAdminService: NormalAdminService,
        private ociService: OciService,
        private route: ActivatedRoute) { }

    ngOnInit() {
        const spinner = SpinnerService.start();
        this.profileService.getUserProfile().pipe(
            map(userProfile => this.userProfile = new UserProfile(userProfile)),
            switchMap(() => this.userService.userDetail$)
        ).subscribe(userDetail => {
            this.userDetail = { ...userDetail };
            SpinnerService.stop(spinner);
        });

        this.allUsersObserver = this.route.data.pipe(
            map((res) => res.allUsers)
        );
    }

    ngOnDestroy() {
        this.destroy$.complete();
        this.destroy$.unsubscribe();
    }

    ngAfterViewInit(): void {
        this.isOciFlow = this.ociService.getOciState();
    }

    openModal() {
        const initialState = {
            userDetail: this.prepareUserDetailsForNewAccount(this.userDetail),
            userProfile: this.prepareProfileForNewAccount(this.userProfile),
            onSuccess: () => {
                this.refreshUserList();
            }
        };

        this.bsModalRef = this.modalService.show(CreateUserModalFormComponent, {
            ignoreBackdropClick: true,
            class: 'modal-lg',
            initialState
        });
    }

    refreshUserList() {
        this.allUsersObserver = this.normalAdminService.getAllUsers();
    }

    private prepareProfileForNewAccount(userProfile: UserProfile) {
        return new UserProfile({
            languages: userProfile.languages,
            salutations: userProfile.salutations,
            types: userProfile.types,
            languageId: userProfile.languages[0].id || 1,
            salutationId: userProfile.salutations[0].id || 1,
            typeId: userProfile.types[0].id || 1
        });
    }

    private prepareUserDetailsForNewAccount(userDetail: UserDetail) {
        let user = new UserDetail({
            salesUser: userDetail.salesUser,
            defaultBranchId: userDetail.defaultBranchId,
            defaultBranchName: userDetail.defaultBranchName,
            custNr: userDetail.custNr
        });

        if (userDetail.finalCustomer) {
            let finalCustomer = {
                name: userDetail.finalCustomer.name
            };
            user.finalCustomer = finalCustomer;
        }

        if (userDetail.customer) {
            let customer = new CustomerModel();
            customer.companyName = userDetail.customer.companyName;
            user.customer = customer;
        }

        return user;
    }
}
