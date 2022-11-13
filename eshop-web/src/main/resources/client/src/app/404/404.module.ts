import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotFoundComponent } from './404.component';
import { NotFoundRoutingModule } from './404-routing.module';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
    imports: [
        CommonModule,
        NotFoundRoutingModule,
        TranslateModule
    ],
    declarations: [NotFoundComponent],
    exports: [NotFoundComponent]
})

export class NotFoundModule { }
