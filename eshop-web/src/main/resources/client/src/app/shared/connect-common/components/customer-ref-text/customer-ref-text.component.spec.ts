import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomerRefTextComponent } from './customer-ref-text.component';

xdescribe('CustomerRefTextComponent', () => {
  let component: CustomerRefTextComponent;
  let fixture: ComponentFixture<CustomerRefTextComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerRefTextComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerRefTextComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
