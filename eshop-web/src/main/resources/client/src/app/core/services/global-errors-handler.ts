import { ErrorHandler, Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { AppCommonService } from './app-common.service';

const CHUNK_ERROR_KEYWORDS = /Loading chunk [0-9] failed/g;
@Injectable({ providedIn: 'root' })
export class GlobalErrorsHandler implements ErrorHandler {
    constructor(
        private appCommonService: AppCommonService
    ) { }

    handleError(errorRes?: HttpErrorResponse) {
        if (!navigator.onLine) {
            this.appCommonService.offlineMessage();
            return;
        }
        const message = errorRes.message ? errorRes.message : errorRes.toString();
        if (this.isChunkFailedError(message)) {
            this.appCommonService.logError({
                url: location.href,
                message
            }).subscribe(res => {
                this.appCommonService.invalidVersionMessage();
            });
            return;
        }

        this.appCommonService.logError({
            url: location.href,
            message
        }).subscribe(res => {});

        throw errorRes;
    }

    private isChunkFailedError(errorMessage) {
        return errorMessage.search(CHUNK_ERROR_KEYWORDS) !== -1;
    }
}
