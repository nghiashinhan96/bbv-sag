import { Injectable } from '@angular/core';
import { NgSelectConfig } from '@ng-select/ng-select';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
export class NgSelectConfigService {
    notFoundText = '';
    placeholder = '';
    loadingText = 'Loading...';
    constructor(private translate: TranslateService) {
        translate.get('SEARCH.NO_RESULTS_FOUND').subscribe(body => {
            this.notFoundText = body;
        });
    }
}
