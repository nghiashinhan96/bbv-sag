import { Component, OnInit } from '@angular/core';
import { ThemeService } from './core/services/theme.service';
import { environment } from 'src/environments/environment';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { TranslateService } from '@ngx-translate/core';
import { DEFAULT_LANG_CODE, SUPPORTED_LANG_CODES } from './core/conts/app.constant';
import { Router, NavigationStart, NavigationEnd, NavigationError, NavigationCancel } from '@angular/router';
import { SpinnerService } from './core/utils/spinner';
import { AppStorageService } from './core/services/custom-local-storage.service';
import { SingleArticleModalComponent } from './shared/single-article/single-article-modal/single-article-modal.component';
import { UserIdleService } from 'angular-user-idle';
import { UserService } from './core/services/user.service';
import { AppModalService } from './core/services/app-modal.service';
import { CommonModalService } from './shared/autonet-common/services/common-modal.service';
import { AccessoryListModalComponent } from './shared/autonet-common/components/article-accessories-modal/article-accessories-modal.component';
import { ArticleReplaceModalComponent } from './shared/autonet-common/components/article-replace-modal/article-replace-modal.component';
import { PartsListModalComponent } from './shared/autonet-common/components/article-parts-list-modal/article-parts-list-modal.component';
import { ArticleCrossReferenceModalComponent } from './shared/autonet-common/components/article-cross-reference-modal/article-cross-reference-modal.component';

@Component({
    selector: 'autonet-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    singleArtRef: BsModalRef;
    private onIdl: boolean;
    private spinner: any;
    constructor(
        private themeService: ThemeService,
        private modalService: BsModalService,
        private translateService: TranslateService,
        private appStorage: AppStorageService,
        private router: Router,
        private userIdle: UserIdleService,
        private userService: UserService,
        private appModalService: AppModalService,
        private commonModalService: CommonModalService
    ) {

    }
    ngOnInit(): void {
        this.commonModalService.setAccessoryArticlesModal(AccessoryListModalComponent);
        this.commonModalService.setReplaceArticlesModal(ArticleReplaceModalComponent);
        this.commonModalService.setPartListArticlesModal(PartsListModalComponent);
        this.commonModalService.setCrossReferenceModal(ArticleCrossReferenceModalComponent);
        this.setCurrentLanguage();

        this.router.events.subscribe(evt => {
            if (evt instanceof NavigationStart) {
                this.spinner = SpinnerService.start();
            } else if (evt instanceof NavigationEnd) {
                SpinnerService.stop(this.spinner);
            } else if (evt instanceof NavigationCancel) {
                SpinnerService.stop(this.spinner);
            } else if (evt instanceof NavigationError) {
                SpinnerService.stop(this.spinner);
            }
        });

        window.addEventListener('message', event => {
            if (environment.autonetServer.indexOf(event.origin) !== -1) {
                if (!event.data) {
                    return;
                }
                const sourceId = event.data.sourceId;
                const redirectUrl = event.data.logout && event.data.logout.redirectUrl || environment.autonetServer;

                switch (sourceId) {
                    case 1:
                        this.userService.logout(redirectUrl);
                        return;
                    case 2:
                        this.appModalService.closeAddToCartModal();
                        return;
                }

                if (this.singleArtRef) {
                    return;
                }
                this.singleArtRef = this.modalService.show(SingleArticleModalComponent, {
                    initialState: {
                        articleNumber: event.data
                    },
                    class: 'modal-lg',
                    ignoreBackdropClick: true
                });
                this.appModalService.modals = this.singleArtRef;
                this.modalService.onHide.subscribe(res => {
                    this.singleArtRef = null;
                });
            }
        });

        // Start watching for user inactivity.
        this.userIdle.startWatching();

        // Start watching when user idle is starting.
        this.userIdle.onTimerStart().subscribe(count => {
            this.userIdle.stopTimer();
            this.appStorage.autonet = null;
            window.location.reload();
        });
    }

    // test() {
    //     const pos = JSON.stringify({ articleId: '12345645632' });
    //     window.top.postMessage(pos, 'http://localhost:4200');
    // }

    private setCurrentLanguage() {
        let currentLangCode = DEFAULT_LANG_CODE;
        const appLangCode = this.appStorage.appLangCode;
        if (appLangCode) {
            if (SUPPORTED_LANG_CODES.indexOf(appLangCode) !== -1) {
                currentLangCode = appLangCode;
            }
        } else {
            const browserLangCode = this.translateService.getBrowserLang();
            if (SUPPORTED_LANG_CODES.indexOf(browserLangCode) !== -1) {
                currentLangCode = browserLangCode;
            }
        }
        this.appStorage.appLangCode = currentLangCode;
        this.translateService.use(currentLangCode);
    }
}
