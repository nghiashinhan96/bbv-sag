import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { SagTableModule } from 'sag-table';

import { UserDetailComponent } from './components/user-detail/user-detail.component';
import { UserManagementComponent } from './user-management.component';
import { CanDeactivateGuard } from 'src/app/core/services/deactive-guard.service';
import { HomeMenuModule } from '../../home-menu/home-menu.module';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { UserManagementRoutes } from './user-management.routes';
import { AuthService } from 'src/app/authentication/services/auth.service';

@NgModule({
    declarations: [UserManagementComponent, UserDetailComponent],
    providers: [CanDeactivateGuard, AuthService],
    imports: [
        UserManagementRoutes,
        SharedModules,
        SagTableModule,
        TranslateModule,
        NgSelectModule,
        HomeMenuModule,
        CommonModule,
        FormsModule,
        ReactiveFormsModule
    ],
})
export class UserManagementModule { }
