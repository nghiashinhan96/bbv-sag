import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderDashboardRefComponent } from './order-dashboard-ref.component';

xdescribe('OrderDashboardRefComponent', () => {
  let component: OrderDashboardRefComponent;
  let fixture: ComponentFixture<OrderDashboardRefComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OrderDashboardRefComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderDashboardRefComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
