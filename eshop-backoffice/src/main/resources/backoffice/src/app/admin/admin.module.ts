import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';

import { AdminModuleRoutes } from './admin.routes';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { SharedModules } from '../shared/components/shared-components.module';

@NgModule({
    declarations: [ChangePasswordComponent],
    imports: [
        AdminModuleRoutes,
        CommonModule,
        SharedModules,
        TranslateModule,
        FormsModule,
        ReactiveFormsModule],
})
export class AdminModule { }
