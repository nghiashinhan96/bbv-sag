import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SagCommonImgSliderComponent } from './components/slider/img-slider/img-slider.component';
import { SagCommonImgSliderModalComponent } from './components/slider/img-slider-modal/img-slider-modal.component';

import { CarouselModule } from 'ngx-bootstrap/carousel';
import { TranslateModule } from '@ngx-translate/core';

@NgModule({
  declarations: [
    SagCommonImgSliderComponent,
    SagCommonImgSliderModalComponent
  ],
  imports: [
    CommonModule,
    CarouselModule.forRoot(),
    TranslateModule
  ],
  entryComponents: [
    SagCommonImgSliderModalComponent
  ],
  exports: [
    SagCommonImgSliderModalComponent
  ]
})
export class SagCommonSliderModule { }
