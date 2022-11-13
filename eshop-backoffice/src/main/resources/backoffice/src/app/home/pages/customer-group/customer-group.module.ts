import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CustomerGroupRoute } from './customer-group.routes';
import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SagTableModule } from 'sag-table';

import { CustomerGroupListComponent } from './components/customer-group-list/customer-group-list.component';
import { CustomerPopupComponent } from './components/customer-popup/customer-popup.component';
import { CustomerGroupDetailComponent } from './components/customer-group-detail/customer-group-detail.component';
import { CustomerGroupResolver } from './services/customer-group.resolver';
import { CustomerGroupLogoUploaderComponent } from './components/customer-group-logo-uploader/customer-group-logo-uploader.component';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { HomeAuthResolver } from '../../home-auth-resolver.service';
import { CustomerGroupService } from '../../services/customer-group/customer-group.service';
import { AffiliateService } from 'src/app/core/services/affiliate.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { NotificationComponent } from 'src/app/core/components/notification/notification.component';
import { ModalModule } from 'ngx-bootstrap/modal';

@NgModule({
    declarations: [
        CustomerGroupListComponent,
        CustomerPopupComponent,
        CustomerGroupDetailComponent,
        CustomerGroupLogoUploaderComponent,
    ],
    imports: [
        CommonModule,
        FormsModule,
        CustomerGroupRoute,
        TranslateModule,
        NgSelectModule,
        ModalModule.forRoot(),
        ReactiveFormsModule,
        SharedModules,
        SagTableModule
    ],
    entryComponents: [CustomerPopupComponent, CustomerGroupLogoUploaderComponent],
    providers: [
        CustomerGroupService,
        HomeAuthResolver,
        AffiliateService,
        CustomerGroupResolver,
    ],
})
export class CustomerGroupModule { }
