import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TranslateModule } from '@ngx-translate/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SagTableModule } from 'sag-table';

import { BranchesRoutingModule } from './branches.routes';
import { BranchListComponent } from './components/branch-list/branch-list.component';
import { BranchDetailFormComponent } from './components/branch-detail-form/branch-detail-form.component';
import { BranchService } from './service/branch.service';
import { HomeMenuModule } from '../../home-menu/home-menu.module';
import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { DeleteConfirmModalComponent } from 'src/app/shared/common/components/delete-confirm-modal/delete-confirm-modal.component';
import { BackOfficeCommonModule } from 'src/app/shared/common/bo-common.module';


@NgModule({
    imports: [
        BranchesRoutingModule,
        CommonModule,
        HomeMenuModule,
        SagTableModule,
        SharedModules,
        TranslateModule,
        NgSelectModule,
        FormsModule,
        ReactiveFormsModule,
        BackOfficeCommonModule
    ],
    declarations: [BranchListComponent, BranchDetailFormComponent],
    providers: [BranchService]
})
export class BranchesModule { }
