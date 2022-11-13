import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LocalStorageService } from 'ngx-webstorage';

@Component({
    selector: 'backoffice-authentication',
    templateUrl: './authentication.component.html'
})
export class AuthenticationComponent implements OnInit, OnDestroy {

    constructor(
        private translateService: TranslateService,
        private localStorage: LocalStorageService
    ) { }

    ngOnInit() {

    }

    ngOnDestroy(): void {

    }


}
