import { Component, OnInit, OnDestroy, Input, EventEmitter, Output } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { SagLibThuleConfigService } from './services/sag-lib-thule-config.service';

@Component({
    selector: 'sag-lib-thule',
    templateUrl: './sag-lib-thule.component.html',
})
export class SagLibThuleComponent implements OnInit, OnDestroy {

    private readonly THULE_FLAG = 'fromThule';
    private readonly THULE_COOKIE_PARAMS: string[] = ['dealer', 'orderlist', 'order_list'];

    @Input() user: any;
    @Input() basketItemSource: any;

    @Output() onSuccess = new EventEmitter();
    @Output() onFallback = new EventEmitter();

    public error: string;
    constructor(
        private http: HttpClient,
        private config: SagLibThuleConfigService,
    ) {
        this.config.spinner.start();
    }

    ngOnInit() {
        if (!!this.user && !!this.user.custNr) {
            const thuleFlag = this.getCookie(this.THULE_FLAG);
            if (thuleFlag) {
                this.addThuleBuyersGuideDataToShoppingCart();
            } else {
                const fallbackRes = { fallback: true };
                this.onFallback.emit(fallbackRes);
            }
        }
    }

    ngOnDestroy(): void {
        this.config.spinner.stop();
    }

    private addThuleBuyersGuideDataToShoppingCart() {
        const thuleFormData = {
            dealer: this.getCookie(this.THULE_COOKIE_PARAMS[0]),
            orderlist: this.getCookie(this.THULE_COOKIE_PARAMS[1]),
            order_list: this.getCookie(this.THULE_COOKIE_PARAMS[2]),
            basketItemSourceId: this.basketItemSource && this.basketItemSource.basketItemSourceId || undefined,
            basketItemSourceDesc: this.basketItemSource && this.basketItemSource.basketItemSourceDesc || undefined
        };
        this.addBuyersGuideDataFromThule(thuleFormData)
            .subscribe(r => {
                this.onSuccess.emit(r);
                this.deleteCookies([...this.THULE_COOKIE_PARAMS, this.THULE_FLAG]);
            }, err => {
                this.error = `Error: ${err && err.message}`;
                this.config.spinner.stop();
            });
    }

    getCookie(cookieName: string) {
        if (!cookieName) {
            return '';
        }
        const cookieItem = this.config.cookie.get(cookieName);
        return cookieItem ? cookieItem : '';
    }

    deleteCookies(cookieNames: string[]) {
        if (!cookieNames) {
            return;
        }
        for (const cookieName of cookieNames) {
            this.config.cookie.remove(cookieName, { path: '' });
        }
    }

    private addBuyersGuideDataFromThule(body) {
        const url = `${this.config.baseUrl}thule/add-buyers-guide`;
        return this.http.post(url, body).pipe(catchError(error => this.handleError(error)));
    }

    private handleError(error: any) {
        return throwError(error);
    }
}
