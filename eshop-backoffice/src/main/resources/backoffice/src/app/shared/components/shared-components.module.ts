import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TimepickerModule } from 'ngx-bootstrap/timepicker';
import { PopoverModule } from 'ngx-bootstrap/popover';

import { ExpandedComponent } from './expanded/expanded.component';
import { FileSelectionComponent } from './file-selection/file-selection.component';
import { NotificationComponent } from 'src/app/core/components/notification/notification.component';
import { ControlMessagesComponent } from './control-message/control-messages.component';
import { TimePickerComponent } from './time-picker/time-picker.component';
import { DurationSelectionComponent } from './duration-selection/duration-selection.component';
import { SharedPipesModule } from '../pipes/shared-pipes.module';
import { MainNavComponent } from './main-nav/main-nav.component';
import { RouterModule } from '@angular/router';
import { ColorPickerComponent } from './color-picker/color-picker.component';
import { ColorPickerModule } from 'ngx-color-picker';

@NgModule({
  declarations: [
    ExpandedComponent,
    FileSelectionComponent,
    NotificationComponent,
    ControlMessagesComponent,
    TimePickerComponent,
    DurationSelectionComponent,
    MainNavComponent,
    ColorPickerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    FormsModule,
    ReactiveFormsModule,
    SharedPipesModule,
    RouterModule,
    PopoverModule.forRoot(),
    TimepickerModule.forRoot(),
    ColorPickerModule
  ],
  exports: [
    ExpandedComponent,
    FileSelectionComponent,
    NotificationComponent,
    ControlMessagesComponent,
    TimePickerComponent,
    DurationSelectionComponent,
    MainNavComponent,
    ColorPickerComponent
  ],
})
export class SharedModules { }
