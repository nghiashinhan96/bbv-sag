import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { DecimalNumberDirective } from './decimal/decimal-number.directive';

@NgModule({
  declarations: [DecimalNumberDirective],
  imports: [CommonModule, TranslateModule],
  exports: [DecimalNumberDirective],
})
export class SharedDirectiveModule {}
