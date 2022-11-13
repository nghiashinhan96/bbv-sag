import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibImgSliderComponent } from './img-slider.component';

xdescribe('ImgSliderComponent', () => {
  let component: SagLibImgSliderComponent;
  let fixture: ComponentFixture<SagLibImgSliderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibImgSliderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibImgSliderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
