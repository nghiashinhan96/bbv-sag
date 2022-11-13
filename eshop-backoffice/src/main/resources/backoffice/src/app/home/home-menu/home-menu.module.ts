import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { HomeMenuComponent } from './home-menu.component';
@NgModule({
    imports: [TranslateModule, RouterModule, CommonModule],
    declarations: [HomeMenuComponent],
    exports: [HomeMenuComponent]
})
export class HomeMenuModule {

}
