import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';
import { DurationPipe } from './duration.pipe';
import { HoursMinutesPipe } from './hours-minutes.pipe';

@NgModule({
  declarations: [DurationPipe, HoursMinutesPipe],
  imports: [CommonModule, TranslateModule],
  exports: [DurationPipe, HoursMinutesPipe],
})
export class SharedPipesModule { }
