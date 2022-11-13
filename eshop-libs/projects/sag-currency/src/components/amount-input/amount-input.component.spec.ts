import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibAmountInputComponent } from './amount-input.component';

xdescribe('AmountInputComponent', () => {
  let component: SagLibAmountInputComponent;
  let fixture: ComponentFixture<SagLibAmountInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibAmountInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibAmountInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
