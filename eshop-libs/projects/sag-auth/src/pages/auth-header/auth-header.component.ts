import { Component, OnInit, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { SUPPORTED_LANG_CODES } from '../../constants/sag-auth';
import { SagAuthStorageService } from '../../services/sag-auth-storage.service';


@Component({
    selector: 'sag-auth-header',
    templateUrl: './auth-header.component.html',
    styleUrls: ['./auth-header.component.scss']
})
export class SagAuthHeaderComponent implements OnInit {
    @Input() supportedLangCodes = SUPPORTED_LANG_CODES;
    appLangCode;
    constructor(
        private translateService: TranslateService,
        private appStorage: SagAuthStorageService
    ) {
        this.appLangCode = this.appStorage.langCode;
    }

    ngOnInit() {
    }

    useLang(selectedLangCode) {
        this.translateService.use(selectedLangCode);
        this.appStorage.langCode = selectedLangCode;
        this.appLangCode = selectedLangCode;
    }

}
