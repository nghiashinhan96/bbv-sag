import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { SagTableModule } from 'sag-table';

import { HomeRoutingModule } from './home-routing.module';
import { HomeComponent } from './home.component';
import { HomeSearchComponent } from './pages/search/search.component';
import { HomeMenuModule } from './home-menu/home-menu.module';
import { SharedModules } from '../shared/components/shared-components.module';
import { AffiliateService } from '../core/services/affiliate.service';
import { AffiliateSearchFormComponent } from './components/affiliate-search-form/affiliate-search-form.component';

import { CustomerGroupSearchFormComponent } from './components/customer-group-search-form/customer-group-search-form.component';
import { CustomerSearchFormComponent } from './components/customer-search-form/customer-search-form.component';
import { UserSearchFormComponent } from './components/user-search-form/user-search-form.component';
import { LicenseSearchFormComponent } from './components/license-search-form/license-search-form.component';
import { HomeAuthResolver } from './home-auth-resolver.service';
import { CustomerService } from './services/customer/customer.service';

@NgModule({
    declarations: [
        HomeComponent,
        HomeSearchComponent,
        AffiliateSearchFormComponent,
        CustomerGroupSearchFormComponent,
        CustomerSearchFormComponent,
        UserSearchFormComponent,
        LicenseSearchFormComponent,
    ],
    imports: [
        CommonModule,
        TranslateModule,
        HomeRoutingModule,
        HomeMenuModule,
        SharedModules,
        NgSelectModule,
        FormsModule,
        ReactiveFormsModule,
        AngularMyDatePickerModule,
        SagTableModule
    ],
    providers: [AffiliateService, HomeAuthResolver, CustomerService],
})
export class HomeModule { }
