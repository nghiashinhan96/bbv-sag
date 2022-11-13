import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { MessengerData } from 'src/app/shared/common/components/messenger/messenger.component';
import { LoginInfo } from '../../models/login-info.model';
import { AuthService } from '../../services/auth.service';

@Component({
    selector: 'backoffice-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {

    // message: MessengerData;
    mode: 'login' | 'forgot' = 'login';

    constructor(
        private authService: AuthService,
        private router: Router
    ) {
    }

    ngOnInit() {
    }

    ngOnDestroy(): void {
        // this.destroyed$.next(true);
        // this.destroyed$.complete();
    }

    submitLogin({ credentials, onSuccess, onError }) {
        SpinnerService.start();
        const data = new LoginInfo({
            username: credentials.username,
            password: credentials.password
        });
        this.authService.login(data).subscribe(
            () => {
                this.authService.getUserDetail().subscribe(() => {
                    this.router.navigateByUrl('/home');
                    if (onSuccess) {
                        onSuccess();
                    }
                });
            },
            err => {
                if (onError) {
                    onError(this.handleErrorMessage(err));
                }
            }
        );
    }

    resetPassword({ data, onSuccess, onError }) {
        const body = {
            ...data,
            redirectUrl: window.location.origin
        };
        this.authService.resetSystemAdminPassword(body)
            .subscribe(
                () => {
                    if (onSuccess) {
                        onSuccess({
                            type: 'SUCCESS',
                            message: 'LOGIN.PASSWORD_RETRIEVED_SUCCESSFUL'
                        });
                    }
                },
                err => {
                    if (err && err.error_code) {
                        if (onError) {
                            onError({
                                type: 'ERROR',
                                message: 'LOGIN.ERROR_MESSAGE.' + err.error_code
                            });
                        }
                    }
                });
    }

    private handleErrorMessage(error) {
        let errorMsg = '';
        if (error.message === 'timeout') {
            errorMsg = 'LOGIN.ERROR_MESSAGE.SYSTEM_ERROR';
        }
        if (error.error === 'access_denied') {
            errorMsg = 'LOGIN.ERROR_MESSAGE.USER_IS_NOT_ACTIVED';
        } else {
            errorMsg = 'LOGIN.ERROR_MESSAGE.INVALID_CREDENTIALS';
        }
        return { type: 'ERROR', message: errorMsg } as MessengerData;
    }
}
