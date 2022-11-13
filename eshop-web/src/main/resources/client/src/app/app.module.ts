import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { TranslateLoader, TranslateModule, MissingTranslationHandlerParams, MissingTranslationHandler } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { NgxPageScrollCoreModule } from 'ngx-page-scroll-core';
import { NgxWebstorageModule } from 'ngx-webstorage';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AuthenticationModule } from './authentication/authentication.module';
import { CoreModule } from './core/core.module';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { AppInitService } from './core/services/app-init.service';
import { AnalyticLoggingModule } from './analytic-logging/analytic-logging.module';
import { DEFAULT_LANG_CODE } from './core/conts/app-lang-code.constant';
import { ActiveDmsProcessor } from './dms/context/active-dms-processor';
import { DmsProcessorFactory } from './dms/context/dms-processor-factory';
import { ParamUtil } from './core/utils/params.utils';
import { SAG_COMMON_LANG_CODE, AffiliateUtil } from 'sag-common';
import { UserIdleModule } from 'angular-user-idle';
import { LIB_PROVIDERS } from './app.providers';
import { GlobalErrorsHandler } from './core/services/global-errors-handler';
import { NgSelectConfigService } from './core/configurations/ng-select.config.service';
import { NgSelectConfig } from '@ng-select/ng-select';
import { CzCustomModule } from './shared/cz-custom/cz-custom.module';
import { environment } from 'src/environments/environment';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { ArticlesService } from 'sag-article-detail';
import { ArticleGroupSortService, ArticleListConfigService } from 'sag-article-list';
import { SbSortingService } from './shared/sb-custom/services/sb-sorting.service';
import { MISSING_TRANSLATIONS } from './core/enums/missing-translations';
import { SbCustomModule } from './shared/sb-custom/sb-custom.module';

const STORAGE_PREFIX = 'connect8';
export class ConnectMissingTranslationHandler implements MissingTranslationHandler {
    handle(params: MissingTranslationHandlerParams) {
        // Since storage service is not init yet, so we use localStorage
        let lang;
        const storageLangCode = localStorage.getItem(`${STORAGE_PREFIX}.${environment.affiliate}.${SAG_COMMON_LANG_CODE}`);
        if (storageLangCode) {
            lang = JSON.parse(storageLangCode);
        } else {
            lang = params.translateService.currentLang || DEFAULT_LANG_CODE;
        }
        if (params.interpolateParams && params.interpolateParams['defaultOnMissingKey']) {
            return params.interpolateParams['defaultOnMissingKey'];
        }
        return MISSING_TRANSLATIONS[lang][params.key] || params.key;
    }
}

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', `/translations.json?ver=${Date.now()}`);
}

export function init_app(appLoadService: AppInitService) {
    return () => appLoadService.init();
}

export function dmsFactory(processer: DmsProcessorFactory) {
    const query: string = window.location.search.substring(1);
    let queryParam: any = ParamUtil.parseQueryFromUrl(query);
    if (queryParam.returnUrl) {
        queryParam = ParamUtil.parseQueryFromUrl(queryParam.returnUrl.replace(/^\/.*\?/g, ''));
    }
    processer.preUrlHandle(queryParam);
    return processer.get();
}

export function sortFactory(articleService: ArticlesService, config: ArticleListConfigService) {
    if (AffiliateUtil.isSb(environment.affiliate)) {
        return new SbSortingService(articleService);
    }
    return new ArticleGroupSortService(articleService, config);
}

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        AppRoutingModule,
        CoreModule,
        AuthenticationModule,
        HttpClientModule,
        TranslateModule.forRoot({
            missingTranslationHandler: {
                provide: MissingTranslationHandler,
                useClass: ConnectMissingTranslationHandler
            },
            loader: {
                provide: TranslateLoader,
                useFactory: (createTranslateLoader),
                deps: [HttpClient]
            }
        }),
        NgxWebstorageModule.forRoot({ prefix: `connect8.${environment.affiliate}`, separator: '.', caseSensitive: false }),
        UserIdleModule.forRoot({ idle: 3540, timeout: 10, ping: 120 }),
        NgxPageScrollCoreModule,
        AnalyticLoggingModule,
        CzCustomModule,
        SbCustomModule
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true
        },
        {
            provide: ErrorHandler,
            useClass: GlobalErrorsHandler,
        },
        { provide: NgSelectConfig, useClass: NgSelectConfigService },
        LIB_PROVIDERS,
        {
            provide: APP_INITIALIZER,
            useFactory: init_app,
            deps: [AppInitService],
            multi: true
        }, {
            provide: ActiveDmsProcessor,
            useFactory: dmsFactory,
            deps: [DmsProcessorFactory]
        },
        {
            provide: ArticleGroupSortService,
            useFactory: sortFactory,
            deps: [ArticlesService, ArticleListConfigService]
        }
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [AppComponent]
})
export class AppModule { }
