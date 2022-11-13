import { SbSortingService } from './services/sb-sorting.service';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
    declarations: [

    ],
    imports: [
        CommonModule,
        FormsModule,
        TranslateModule
    ],
    entryComponents: [

    ],
    exports: [

    ],
    providers: [
        SbSortingService
    ]
})
export class SbCustomModule { }
