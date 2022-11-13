import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { NumberDecimalDirective } from './number-decimal.directive';
@NgModule({
    imports: [TranslateModule, RouterModule],
    declarations: [NumberDecimalDirective],
    exports: [NumberDecimalDirective],
    providers: [],
})
export class NumberModule { }
