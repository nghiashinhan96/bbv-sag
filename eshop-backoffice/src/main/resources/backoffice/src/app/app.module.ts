import { BrowserModule } from '@angular/platform-browser';
import { NgModule, APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, Injector, ErrorHandler } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { NgxWebstorageModule } from 'ngx-webstorage';

import { CoreModule } from './core/core.module';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule, HttpClient, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/interceptors/auth.interceptor';
import { AuthenticationModule } from './authentication/authentication.module';
import { UserService } from './core/services/user.service';
import { BrowserService } from './core/services/browser.service';
import { AppInitService } from './core/services/app-init.service';
import { GlobalErrorsHandler } from './core/services/global-error-handler';
import { SagCommonModule } from 'sag-common';
import { SagTableModule, SagTableConfigService } from 'sag-table';
import { AppConfigService } from './core/services/app.config.service';

export function createTranslateLoader(http: HttpClient) {
    return new TranslateHttpLoader(http, './assets/i18n/', `/translations.json?ver=${Date.now()}`);
}

export function init_app(appLoadService: AppInitService) {
    return () => appLoadService.init();
}
@NgModule({
    declarations: [AppComponent],
    imports: [
        BrowserAnimationsModule,
        BrowserModule,
        AppRoutingModule,
        CoreModule,
        AuthenticationModule,
        HttpClientModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: createTranslateLoader,
                deps: [HttpClient],
            },
        }),
        NgxWebstorageModule.forRoot({
            prefix: 'backoffice8',
            separator: '.',
            caseSensitive: false,
        }),
        SagCommonModule.forRoot(),
        SagTableModule.forRoot()
    ],
    providers: [
        UserService,
        BrowserService,
        {
            provide: ErrorHandler,
            useClass: GlobalErrorsHandler,
        },
        {
            provide: HTTP_INTERCEPTORS,
            useClass: AuthInterceptor,
            multi: true,
        },
        {
            provide: APP_INITIALIZER,
            useFactory: init_app,
            deps: [AppInitService],
            multi: true
        },
        {
            provide: SagTableConfigService,
            useClass: AppConfigService
        }
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA],
    bootstrap: [AppComponent],
})
export class AppModule { }
