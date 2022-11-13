import { Component, OnInit } from '@angular/core';
import { Router, NavigationStart, NavigationEnd, NavigationError, NavigationCancel } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { AppStorageService } from './core/services/custom-local-storage.service';
import { DEFAULT_LANG } from './core/conts/app.constant';
import { SpinnerService } from './core/utils/spinner';
import { AuthService } from './authentication/services/auth.service';
// import { SpinnerService } from './core/utils/spinner';
// import { UserService } from './core/services/user.service';
// import { AppStorageService } from './core/services/custom-local-storage.service';
// import { SUPPORTED_LANG_CODES, DEFAULT_LANG_CODE } from './core/conts/app-lang-code.constant';

@Component({
    selector: 'backoffice-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

    constructor(
        private router: Router,
        private translateService: TranslateService,
        private appStorage: AppStorageService,
        private authService: AuthService
    ) { }

    ngOnInit(): void {
        let spinner;
        this.router.events.subscribe(evt => {
            if (evt instanceof NavigationStart) {
                spinner = SpinnerService.startApp();
            } else if (evt instanceof NavigationEnd || evt instanceof NavigationError || evt instanceof NavigationCancel) {
                SpinnerService.stop(spinner);
            }
        });

        this.initLangguage();

        this.getUserDetail();
    }

    initLangguage() {
        this.translateService.setDefaultLang(DEFAULT_LANG);
        const lang = DEFAULT_LANG;
        this.appStorage.appLangCode = lang;
        this.translateService.use(lang);
    }

    private getUserDetail() {
        if (!this.appStorage.appToken) {
            return;
        }
        this.authService.getUserDetail().subscribe();
    }
}
