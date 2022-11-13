import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyCustomerOrderComponent } from './my-customer-order.component';

xdescribe('MyCustomerOrderComponent', () => {
  let component: MyCustomerOrderComponent;
  let fixture: ComponentFixture<MyCustomerOrderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MyCustomerOrderComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyCustomerOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
