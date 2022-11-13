import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalCustomerAddressComponent } from './final-customer-address.component';

xdescribe('FinalCustomerAddressComponent', () => {
  let component: FinalCustomerAddressComponent;
  let fixture: ComponentFixture<FinalCustomerAddressComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FinalCustomerAddressComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinalCustomerAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
