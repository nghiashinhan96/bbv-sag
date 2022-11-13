import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';
import { SagTableModule } from 'sag-table';
import { NgSelectModule } from '@ng-select/ng-select';

import { SalesListComponent } from './components/sales-list/sales-list.component';
import { SalesCreatingComponent } from './components/sales-creating/sales-creating.component';
import { SalesSavingComponent } from './components/sales-saving/sales-saving.component';
import { SalesEditingComponent } from './components/sales-editing/sales-editing.component';
import { HomeMenuModule } from '../../home-menu/home-menu.module';
import { CanDeactivateGuard } from 'src/app/core/services/deactive-guard.service';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { AadAccountService } from './services/aad-accounts/aad-accounts-service';
import { SalesManagementRoutes } from './sales-management.routes';

@NgModule({
    declarations: [SalesListComponent, SalesCreatingComponent, SalesEditingComponent, SalesSavingComponent],
    imports: [
        SalesManagementRoutes,
        CommonModule,
        SharedModules,
        NgSelectModule,
        HomeMenuModule,
        SagTableModule,
        TranslateModule,
        FormsModule,
        ReactiveFormsModule
    ],
    providers: [CanDeactivateGuard,
        AadAccountService]
})
export class SalesManagementModule { }
