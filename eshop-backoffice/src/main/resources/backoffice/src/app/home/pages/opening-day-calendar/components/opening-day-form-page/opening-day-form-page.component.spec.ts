import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpeningDayFormPageComponent } from './opening-day-form-page.component';

xdescribe('OpeningDayFormPageComponent', () => {
  let component: OpeningDayFormPageComponent;
  let fixture: ComponentFixture<OpeningDayFormPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpeningDayFormPageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpeningDayFormPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
