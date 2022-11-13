import { environment } from 'src/environments/environment';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from './services/api.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { AppThemeDirective } from './directives/app-theme.directive';
import { TranslateModule } from '@ngx-translate/core';
import { ModalModule } from 'ngx-bootstrap/modal';
import { SysNotificationComponent } from './components/sys-notification/sys-notification.component';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { SystemMessagesService } from './services/system-messages.service';
import { SysTimeoutComponent } from './components/sys-timeout/sys-timeout.component';
import { Adal8HTTPService, Adal8Service } from 'adal-angular8';
import { LegalDocumentListModalComponent } from '../authentication/components/legal-document-list-modal/legal-document-list-modal.component';
import { LegalDocumentDetailModalComponent } from '../authentication/components/legal-document-detail-modal/legal-document-detail-modal.component';
import { FormsModule } from '@angular/forms';
import { SysIERecommendationComponent } from './components/sys-ie-reconmmendation/sys-ie-reconmmendation.component';

@NgModule({
    declarations: [
        AppThemeDirective,
        SysNotificationComponent,
        SysTimeoutComponent,
        LegalDocumentListModalComponent,
        LegalDocumentDetailModalComponent,
        SysIERecommendationComponent
    ],
    imports: [
        CommonModule,
        HttpClientModule,
        TranslateModule,
        ConnectCommonModule,
        ModalModule.forRoot(),
        FormsModule
    ],
    providers: [
        ApiService,
        SystemMessagesService,
        Adal8Service,
        { provide: Adal8HTTPService, useFactory: Adal8HTTPService.factory, deps: [HttpClient, Adal8Service] }
    ],
    entryComponents: [
        SysNotificationComponent,
        SysTimeoutComponent,
        LegalDocumentListModalComponent,
        LegalDocumentDetailModalComponent,
        SysIERecommendationComponent
    ],
    exports: [
        SysNotificationComponent,
        SysTimeoutComponent,
        SysIERecommendationComponent
    ]
})
export class CoreModule { }
