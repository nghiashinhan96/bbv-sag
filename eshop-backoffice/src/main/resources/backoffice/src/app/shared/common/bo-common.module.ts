import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { MessengerComponent } from './components/messenger/messenger.component';
import { DeleteConfirmModalComponent } from './components/delete-confirm-modal/delete-confirm-modal.component';


@NgModule({
    declarations: [
        MessengerComponent,
        DeleteConfirmModalComponent
    ],
    imports: [
        CommonModule,
        FormsModule,
        TranslateModule
    ],
    exports: [
        MessengerComponent,
        DeleteConfirmModalComponent
    ],
    entryComponents: [DeleteConfirmModalComponent]
})
export class BackOfficeCommonModule { }
