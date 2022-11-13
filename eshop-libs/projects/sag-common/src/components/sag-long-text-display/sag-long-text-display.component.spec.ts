import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLongTextDisplayComponent } from './sag-long-text-display.component';

xdescribe('SagLongTextDisplayComponent', () => {
  let component: SagLongTextDisplayComponent;
  let fixture: ComponentFixture<SagLongTextDisplayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLongTextDisplayComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLongTextDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
