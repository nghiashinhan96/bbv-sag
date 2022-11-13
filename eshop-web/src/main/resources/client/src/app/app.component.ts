import { Component, OnInit, OnDestroy } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { Router, NavigationStart, NavigationEnd, NavigationError, NavigationCancel } from '@angular/router';

import { BsModalService } from 'ngx-bootstrap/modal';
import { UserIdleService } from 'angular-user-idle';
import { Adal8Service } from 'adal-angular8';

import { SpinnerService } from './core/utils/spinner';
import { UserService } from './core/services/user.service';
import { AppStorageService } from './core/services/app-storage.service';
import { UserDetail } from './core/models/user-detail.model';
import { SystemMessagesService } from './core/services/system-messages.service';
import { GoogleAnalyticsService } from './analytic-logging/services/google-analytics.service';
import { SysTimeoutComponent } from './core/components/sys-timeout/sys-timeout.component';
import { AdalConfigService } from './authentication/services/adal-config.service';
import { environment } from 'src/environments/environment';
import { AffiliateUtil, BroadcastService } from 'sag-common';
import {
    SingleArticleModalComponent
} from './shared/cz-custom/components/article-detail/single-article-modal/single-article-modal.component';
import { ArticleShoppingBasketService } from './core/services/article-shopping-basket.service';
import { ArticleBroadcastKey, ArticleModel } from 'sag-article-detail';
import { FreetextArticleService } from './article-not-context-result-list/service/freetext-article.service';
import { SEARCH_MODE } from 'sag-article-list';
import { MultiLevelCategory } from './article-not-context-result-list/models/multi-level-category.model';
import { CountryUtil } from './core/utils/country.util';

import { SubSink } from 'subsink';
import { AppModalService } from './core/services/app-modal.service';
import { CommonModalService } from './shared/connect-common/services/common-modal.service';
import { AccessoryListModalComponent } from './shared/connect-common/components/article-accessories-modal/article-accessories-modal.component';
import { ArticleReplaceModalComponent } from './shared/connect-common/components/article-replace-modal/article-replace-modal.component';
import { PartsListModalComponent } from './shared/connect-common/components/article-parts-list-modal/article-parts-list-modal.component';
import { SysIERecommendationComponent } from './core/components/sys-ie-reconmmendation/sys-ie-reconmmendation.component';
import { ThemeService } from './core/services/theme.service';
import { ArticleCrossReferenceModalComponent } from './shared/connect-common/components/article-cross-reference-modal/article-cross-reference-modal.component';
@Component({
    selector: 'connect-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
    private languageSub: Subscription;
    private userSub: Subscription;
    private idleSub: Subscription;
    private singleArtRef: any;

    private subs = new SubSink();

    constructor(
        private router: Router,
        private translateService: TranslateService,
        private gaService: GoogleAnalyticsService,
        private systemMessageService: SystemMessagesService,
        private appStorage: AppStorageService,
        private userService: UserService,
        private userIdle: UserIdleService,
        private modalService: BsModalService,
        private adal8Service: Adal8Service,
        private adalConfigService: AdalConfigService,
        private articleBasketService: ArticleShoppingBasketService,
        private freetextArticleService: FreetextArticleService,
        private articleListBroadcastService: BroadcastService,
        private appModal: AppModalService,
        private commonModalService: CommonModalService,
        private themeService: ThemeService
    ) {
        this.adal8Service.init(this.adalConfigService.adalConfig);
    }

    ngOnInit(): void {
        let spinner;
        this.commonModalService.setAccessoryArticlesModal(AccessoryListModalComponent);
        this.commonModalService.setReplaceArticlesModal(ArticleReplaceModalComponent);
        this.commonModalService.setPartListArticlesModal(PartsListModalComponent);
        this.commonModalService.setCrossReferenceModal(ArticleCrossReferenceModalComponent);
        this.router.events.subscribe(evt => {
            if (evt instanceof NavigationStart) {
                spinner = SpinnerService.startApp();
            } else if (evt instanceof NavigationEnd || evt instanceof NavigationError || evt instanceof NavigationCancel) {
                SpinnerService.stop(spinner);
            }
            if (evt instanceof NavigationEnd) {
                this.gaService.sendPageView();
            }
        });

        const defaultLangCode = CountryUtil.getDefaultLangCode(environment.affiliate);
        const currentLangCode = this.appStorage.appLangCode || defaultLangCode;
        this.userService.setCurrentLanguage(currentLangCode);
        this.getSystemMessagesData();
        this.themeService.updatePrimaryBackground(this.userService.userDetail);

        // Detect IE browser and show recommendation popup
        let isAuthed: boolean = false;
        this.subs.sink = this.userService.userDetail$.subscribe((user: UserDetail) => {
            isAuthed = !!user;
            const isIE = /msie\s|trident\//i.test(window.navigator.userAgent);
            if(isIE && !isAuthed) {
                this.showIERecommendationPopup();
            }
        });
        
        // init idle
        // Start watching for user inactivity.
        this.userService.userDetail$.subscribe(userDetail => {
            this.stopWatching();
            if (userDetail) {
                setTimeout(() => {
                    this.startWatching(userDetail);
                });
            }
        });

        this.adal8Service.handleWindowCallback();
        this.adal8Service.getUser();

        const affiliate = environment.affiliate;
        document.documentElement.classList.add(affiliate);

        this.subs.sink = this.articleListBroadcastService.on(ArticleBroadcastKey.REFERENCE_NUMBER_CHANGE)
            .subscribe(() => {
                this.appModal.closeAll();
            });

        window.addEventListener('message', event => {
            if (!event.data) {
                return;
            }
            if (!environment.unicatServer || environment.unicatServer.indexOf(event.origin) === -1) {
                return;
            }
            switch (event.data.type) {
                case 'VIEW_ARTICLE_DETAIL':
                    if (this.singleArtRef) {
                        return;
                    }
                    this.singleArtRef = this.modalService.show(SingleArticleModalComponent, {
                        initialState: {
                            acticleId: event.data.art_id
                        },
                        class: 'modal-lg',
                        ignoreBackdropClick: true
                    });
                    this.modalService.onHide.subscribe(res => {
                        this.singleArtRef = null;
                    });
                    break;
                    // event sample
                    // {
                    //     type: 'VIEW_ARTICLE_DETAIL',
                    //     art_id: "gdb3562"
                    // }
                case 'ADD_TO_BASKET':
                    const basketSpinner = SpinnerService.start();
                    const categoryLevel = new MultiLevelCategory(SEARCH_MODE.ID_SAGSYS);
                    categoryLevel.setKeyword(event.data.art_id);
                    categoryLevel.offset = 0;
                    this.freetextArticleService.getMultipleLevelCategory(categoryLevel)
                        .subscribe((res: any) => {
                            if (res.articles && res.articles.content && res.articles.content.length > 0) {
                                const art = res.articles.content[0];
                                const content: any = {
                                    action: 'ADD',
                                    amount: event.data.quantity,
                                    artnr: art.artnr,
                                    cartKey: null,
                                    category: undefined,
                                    pimId: art.id_pim,
                                    stock: undefined,
                                    vehicle: undefined,
                                    callback: () => {
                                        SpinnerService.stop(basketSpinner);
                                    }
                                };
                                content.article = new ArticleModel(art);
                                this.articleBasketService.addItemToCart(content);
                            } else {
                                SpinnerService.stop(basketSpinner);
                            }
                        });
                    // event sample
                    // {
                    //     type: 'ADD_TO_BASKET',
                    //     art_id: "gdb3562",
                    //     quantity: 1
                    // }
                    break;
            }
        });

        window.addEventListener('scroll', () => {
            var left = window.scrollX || window.pageXOffset || (document.documentElement && document.documentElement.scrollLeft) || 0;
            const message = document.querySelector('.top-message .system-message .banner');
            if (message) {
                (message as HTMLElement).style.left = `-${left}px`;
            }
            const header = document.querySelector('nav.header');
            if (header) {
                (header as HTMLElement).style.left = `-${left}px`;
            }
        })
    }

    ngOnDestroy() {
        this.languageSub.unsubscribe();
        this.userSub.unsubscribe();
        this.subs.unsubscribe();
    }

    private getSystemMessagesData() {
        let isAuthed = false;

        this.languageSub = this.translateService.onLangChange.subscribe(langs => {
            this.themeService.updatePrimaryBackground(this.userService.userDetail);
            this.getMessagesData(isAuthed, langs.lang);
        });

        this.userSub = this.userService.userDetail$.subscribe((user: UserDetail) => {
            isAuthed = !!user;
            this.getMessagesData(isAuthed, this.translateService.getBrowserLang());
        });
    }

    private getMessagesData(isAuthed, lang) {
        this.systemMessageService.getSystemMessages(isAuthed, lang).subscribe();
    }

    private showTimeOutPopup() {
        this.modalService.show(SysTimeoutComponent, {
            class: 'modal-sm modal-timeout',
            ignoreBackdropClick: true
        });
    }

    private stopWatching() {
        if (this.idleSub) {
            this.idleSub.unsubscribe();
        }
        this.userIdle.stopWatching();
    }

    private startWatching(userDetail: UserDetail) {
        const idleInSeconds = userDetail && userDetail.settings && userDetail.settings.sessionTimeoutSeconds;
        if (idleInSeconds) {
            this.userIdle.setConfigValues({idle: idleInSeconds, timeout: 0, ping: 0});
        }
        this.userIdle.startWatching();
        // Start watching when user idle is starting.
        this.idleSub = this.userIdle.onTimerStart().subscribe(count => {
            this.userService.logout();
            this.stopWatching();
            this.showTimeOutPopup();
        });
    }

    private showIERecommendationPopup() {
        this.modalService.show(SysIERecommendationComponent, {
            initialState: {
                title: 'BROWSER_RECOMMENDATION.TITLE',
                firstDescription: 'BROWSER_RECOMMENDATION.FIRST_DESCRIPTION',
                secondDescription: 'BROWSER_RECOMMENDATION.SECOND_DESCRIPTION'
            },
            class: 'modal-sm sys-ie-recommendation',
            ignoreBackdropClick: true,
            keyboard: false
        });
    }
}
