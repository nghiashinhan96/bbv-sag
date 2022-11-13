import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { environment } from 'src/environments/environment';
import { AppStorageService } from '../core/services/custom-local-storage.service';


@Component({
    selector: 'autonet-authentication',
    templateUrl: './authentication.component.html'
})
export class AuthenticationComponent implements OnInit, AfterViewInit, OnDestroy {

    second = 5;
    loginUrl = environment.autonetServer;
    private interval: any;
    constructor(
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        this.appStorage.lastKeyword = '';
    }

    ngAfterViewInit(): void {
        this.interval = setInterval(() => {
            if (this.second > 0) {
                this.second -= 1;
            }
            if (this.second === 0) {
                clearInterval(this.interval);
                window.location.href = this.loginUrl;
            }
        }, 1000);
    }

    backLogin() {
        window.location.href = this.loginUrl;
    }

    ngOnDestroy(): void {
        if (this.interval) {
            clearInterval(this.interval);
        }
    }

}
