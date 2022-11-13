import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { NgSelectModule } from '@ng-select/ng-select';
import { NgModule } from '@angular/core';
import { TranslateModule } from '@ngx-translate/core';
import { LMarkdownEditorModule } from 'ngx-markdown-editor';

import { SagTableModule } from 'sag-table';

import { SharedModules } from '../../../shared/components/shared-components.module';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { AffiliateResultsComponent } from './components/affiliate-results/affiliate-results.component';
import { AffiliateDetailsComponent } from './components/affiliate-details/affiliate-details.component';
import { AffiliateInfoComponent } from './components/affilate-info/affiliate-info.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { AffiliateSearchRoutes } from './affiliate-search-routes';
import { HomeMenuModule } from '../../home-menu/home-menu.module';
import { CustomerGroupService } from '../../services/customer-group/customer-group.service';
import { CustomerService } from '../../services/customer/customer.service';
import { ModalModule } from 'ngx-bootstrap/modal';
import { AffiliateAvailSettingComponent } from './components/affiliate-avail-setting/affiliate-avail-setting.component';
import { AffiliateExternalPartSettingComponent } from './components/affiliate-external-part-setting/affiliate-external-part-setting.component';

@NgModule({
  declarations: [
    AffiliateResultsComponent,
    AffiliateDetailsComponent,
    AffiliateInfoComponent,
    RegistrationComponent,
    AffiliateAvailSettingComponent,
    AffiliateExternalPartSettingComponent
  ],
  imports: [
    AffiliateSearchRoutes,
    CommonModule,
    TranslateModule,
    SharedModules,
    AngularMyDatePickerModule,
    FormsModule,
    ReactiveFormsModule,
    NgSelectModule,
    HomeMenuModule,
    SagTableModule,
    ModalModule.forRoot(),
    LMarkdownEditorModule
  ],
  entryComponents: [RegistrationComponent],
  providers: [CustomerGroupService, CustomerService],
})
export class AffiliateSearchModule { }
