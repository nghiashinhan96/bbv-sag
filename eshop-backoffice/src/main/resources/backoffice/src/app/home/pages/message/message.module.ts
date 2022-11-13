import { MessageSavingComponent } from './components/message-saving/message-saving.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { LMarkdownEditorModule } from 'ngx-markdown-editor';
import { NgSelectModule } from '@ng-select/ng-select';
import { TranslateModule } from '@ngx-translate/core';
import { AngularMyDatePickerModule } from 'angular-mydatepicker';
import { SagTableModule } from 'sag-table';

import { SharedModules } from 'src/app/shared/components/shared-components.module';
import { MessageListComponent } from './components/message-list/message-list.component';
import { HomeMenuModule } from '../../home-menu/home-menu.module';
import { NumberFieldDirective } from 'src/app/shared/directive/decimal/number-field.directive';
import { MessageService } from './services/message.service';
import { MessageDeleteModalComponent } from './components/message-delete-modal/message-delete-modal.component';
import { ModalModule } from 'ngx-bootstrap/modal';

const routes: Routes = [
    { path: '', component: MessageListComponent },
    { path: 'create', component: MessageSavingComponent },
    { path: 'edit/:id', component: MessageSavingComponent }
];
@NgModule({
    declarations: [MessageListComponent, MessageSavingComponent, NumberFieldDirective, MessageDeleteModalComponent],
    imports: [CommonModule,
        SharedModules,
        NgSelectModule,
        LMarkdownEditorModule,
        SagTableModule,
        RouterModule.forChild(routes),
        HomeMenuModule,
        TranslateModule,
        FormsModule,
        ReactiveFormsModule,
        AngularMyDatePickerModule,
        ModalModule.forRoot(),
    ],
    exports: [NumberFieldDirective],
    entryComponents: [MessageDeleteModalComponent],
    providers: [
        MessageService
    ]
})
export class MessageModule { }
