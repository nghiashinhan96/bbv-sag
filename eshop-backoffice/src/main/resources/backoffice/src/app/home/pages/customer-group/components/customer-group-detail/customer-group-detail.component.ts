import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators, FormArray } from '@angular/forms';

import { Subscription, Observable } from 'rxjs';
import { first, map } from 'rxjs/operators';
import { cloneDeep, isEmpty, isEqual } from 'lodash';

import { CustomerGroupLogoUploaderComponent } from '../customer-group-logo-uploader/customer-group-logo-uploader.component';
import { CustomerGroupDetailModel } from '../../models/customer-group-detail.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { ApiUtil } from 'src/app/core/utils/api.util';
import { AffiliatePermissionItem } from 'src/app/home/models/affiliate/affiliate-permission-item.model';
import { NotificationModel } from 'src/app/shared/models/notification.model';
import { CustomerGroupService } from 'src/app/home/services/customer-group/customer-group.service';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';


@Component({
    selector: "backoffice-customer-group-detail",
    templateUrl: './customer-group-detail.component.html',
    styleUrls: ['./customer-group-detail.component.scss'],
})
export class CustomerGroupDetailComponent implements OnInit, OnDestroy {
    customerGroupDetail$: Observable<CustomerGroupDetailModel>;
    customerGroupDetailForm: FormGroup;
    affiliates = [];
    notifier: NotificationModel;
    inProgressImg = false;
    newLogo: any;
    private newLogoFile: File;
    private oldLogoUrl: string;
    private affiliateSub: Subscription;
    constructor(
        private formBuilder: FormBuilder,
        private activatedRoute: ActivatedRoute,
        private service: CustomerGroupService,
        private modalService: BsModalService,
        private router: Router
    ) { }

    ngOnInit() {
        this.customerGroupDetailForm = this.formBuilder.group({
            collectionShortName: '',
            affiliateShortName: ['', Validators.required],
            name: ['', Validators.required],
            description: '',
            permissions: [this.formBuilder.array([]), Validators.required],
            settings: this.formBuilder.group({
                logo_image: '',
                title: ['', Validators.required],
            }),
        });

        this.customerGroupDetailForm
            .get('affiliateShortName')
            .valueChanges.subscribe((val) => {
                if (!!val) {
                    this.getTemplate(val);
                }
            });

        this.customerGroupDetail$ = this.activatedRoute.data.pipe(
            map(({ data }) => {
                this.affiliates = data.affiliates.map((item) => {
                    return { value: item.shortName, label: item.name };
                });
                if (!!data.customerGroupDetail.collectionShortName) {
                    const isDefaultCollection = data.customerGroupDetail.default;
                    const logoUrl =
                        (data.customerGroupDetail.settings &&
                            data.customerGroupDetail.settings.logo_image) ||
                        '';
                    this.loadLogoFromUrl(logoUrl);
                    this.customerGroupDetailForm.patchValue(data.customerGroupDetail, {
                        emitEvent: false,
                    });
                    this.customerGroupDetailForm
                        .get('affiliateShortName')
                        .disable({ emitEvent: false });
                    setTimeout(() => {
                        this.customerGroupDetailForm.setControl(
                            'permissions',
                            this.formBuilder.array([])
                        );
                        this.createPermissionsArrayGroup(
                            data.customerGroupDetail.permissions || [],
                            isDefaultCollection
                        );
                    });
                } else {
                    if (data.affiliate && data.affiliate.trim()) {
                        this.customerGroupDetailForm
                            .get('affiliateShortName')
                            .setValue(data.affiliate);
                    }
                }
                return data.customerGroupDetail;
            })
        );
    }

    ngOnDestroy(): void {
        if (!!this.affiliateSub) {
            this.affiliateSub.unsubscribe();
        }
    }

    backToList() {
        this.router.navigate(['../'], {
            relativeTo: this.activatedRoute,
            queryParamsHandling: 'merge',
        });
    }

    async createNewGroup() {
        if (this.customerGroupDetailForm.invalid) {
            return;
        }
        const detailGroup = this.customerGroupDetailForm.getRawValue();
        try {
            SpinnerService.start();
            // remove old logo
            if (!!this.oldLogoUrl && this.oldLogoUrl.indexOf('http') !== -1) {
                const logoName = this.oldLogoUrl.split('/').pop();
                // Does not matter if can not remove the old logo
                try {
                    await this.service
                        .removeLogo('COLLECTION_LOGO', logoName)
                        .pipe(
                            first()
                        )
                        .toPromise();
                } catch (ex) { }
            }
            // upload new logo
            if (!!this.newLogoFile) {
                const res = await this.service
                    .uploadLogo('COLLECTION_LOGO', this.newLogoFile)
                    .pipe(
                        first()
                    )
                    .toPromise();
                // update new logo link
                detailGroup.settings[res.key] = res.url;
            }
            // update Collection info
            const newGroupInfo = cloneDeep(detailGroup);
            if (!detailGroup.collectionShortName) {
                // For creating
                await this.service.createNewGroup(newGroupInfo)
                    .pipe(
                        first()
                    )
                    .toPromise();
            } else {
                // For editing
                await this.service.updateGroup(newGroupInfo)
                    .pipe(
                        first()
                    )
                    .toPromise();
            }
            this.backToList();
        } catch ({ error }) {
            let errorMessage;
            switch (error && error.error_code) {
                case 'DUPLICATED_COLLECTION_NAME':
                    errorMessage = 'MESSAGE.DUPLICATED_COLLECTION_NAME';
                    break;
                default:
                    errorMessage = ApiUtil.handleErrorReponse(error);
                    break;
            }
            this.notifier = { messages: [errorMessage], status: false };
        }
        SpinnerService.stop();
    }

    uploadImage() {
        const modalRef: BsModalRef = this.modalService.show(
            CustomerGroupLogoUploaderComponent,
            {
                class: 'modal-md file-uploader',
                ignoreBackdropClick: true,
            }
        );
        modalRef.content.onClose = (imgFile) => {
            if (!!imgFile) {
                this.newLogoFile = imgFile;
                this.loadLogoFromFile(imgFile);
                this.deleteExistedLogo();
            }
        };
    }

    async removeLogoHandler() {
        this.newLogoFile = null;
        this.newLogo = null;
        this.deleteExistedLogo();
    }

    get permissions() {
        return this.customerGroupDetailForm.get('permissions') as FormArray;
    }

    get settings() {
        return this.customerGroupDetailForm.get('settings') as FormGroup;
    }

    private getTemplate(affiliateShortName) {
        this.service
            .getTemplateByAffiliateShortName(affiliateShortName)
            .subscribe((template) => {
                this.customerGroupDetailForm.setControl(
                    'permissions',
                    this.formBuilder.array([])
                );
                this.createPermissionsArrayGroup(template.permissions || []);
            });
    }

    private createPermissionsArrayGroup(
        permissions: AffiliatePermissionItem[],
        isDefaultCollection = false
    ) {
        permissions.forEach((item) => {
            this.permissions.push(
                this.formBuilder.group({
                    editable: item.editable,
                    enable: [
                        {
                            value: item.enable,
                            disabled: isDefaultCollection || !item.editable,
                        },
                    ],
                    langKey: item.langKey,
                    permission: item.permission,
                    permissionId: item.permissionId,
                })
            );
        });
    }

    private deleteExistedLogo() {
        if (!!this.settings.get('logo_image').value) {
            this.oldLogoUrl = this.settings.get('logo_image').value;
            this.settings.get('logo_image').setValue(null);
        }
    }

    private loadLogoFromFile(imgFile: File) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
            this.newLogo = e.target.result;
        };
        reader.readAsDataURL(imgFile);
    }

    private loadLogoFromUrl(url: string) {
        const newImg = new Image();
        newImg.onload = () => {
            this.inProgressImg = false;
        };
        newImg.onerror = () => {
            this.inProgressImg = true;
        };
        newImg.src = url;
    }
}
