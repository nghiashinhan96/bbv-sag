import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { of } from 'rxjs/internal/observable/of';
import { catchError, finalize } from 'rxjs/operators';
import { SagConfirmationBoxComponent } from 'sag-common';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { UserService } from 'src/app/core/services/user.service';
import { NO_DEFAULT_ARTICLE_GROUP_SETTING, NO_DEFAULT_BRAND_SETTING  } from '../../services/constant';

@Component({
    selector: 'connect-margin-management',
    templateUrl: './margin-management.component.html',
    styleUrls: ['./margin-management.component.scss']
})
export class MarginManagementComponent implements OnInit {
    showNetPrice: boolean;
    tab = 1;

    isEnableShowNetPriceCheck = true;

    constructor(
        private userService: UserService,
        private bsModalService: BsModalService,
        private appModal: AppModalService
    ) {
        this.showNetPrice = this.userService.userDetail.wholeSalerHasNetPrice;
    }

    ngOnInit() {
    }

    onUpdateShowNetPrice(event) {
        this.appModal.modals = this.bsModalService.show(SagConfirmationBoxComponent, {
            class: 'modal-md clear-article-list-modal',
            ignoreBackdropClick: true,
            initialState: {
                message: 'MARGIN_MANAGE.NETTO_CONFIRM',
                okButton: 'COMMON_LABEL.YES',
                cancelButton: 'COMMON_LABEL.NO',
                showHeaderIcon: false,
                showCloseButton: true,
                close: () => {
                    this.isEnableShowNetPriceCheck = false;
                    this.userService.updateShowFCNetPrice(this.showNetPrice)
                        .pipe(
                            finalize(() => { this.isEnableShowNetPriceCheck = true; }),
                            catchError(({ error }) => {
                                this.showNetPrice = !event;
                                this.handlerError(error);
                                return of(null)
                            })
                        )
                        .subscribe();
                },
                cancel: () => {
                    this.showNetPrice = !event;
                }
            }
        });

    }

    handlerError(error) {
        if (!error) {
            return;
        }
        switch (error.error_code) {
            case NO_DEFAULT_ARTICLE_GROUP_SETTING:
            case NO_DEFAULT_BRAND_SETTING:
                this.appModal.modals = this.bsModalService.show(SagConfirmationBoxComponent, {
                    class: 'modal-md clear-article-list-modal',
                    ignoreBackdropClick: true,
                    initialState: {
                        message: 'MARGIN_MANAGE.DEFAULT_MARGIN_NOT_SETUP',
                        okButton: 'COMMON_LABEL.YES',
                        showCancelButton: false,
                    }
                });
                break;
            default:
                break;
        }
    }
}
