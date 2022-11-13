import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OrderDashboardListComponent } from './order-dashboard-list.component';

xdescribe('OrderDashboardListComponent', () => {
  let component: OrderDashboardListComponent;
  let fixture: ComponentFixture<OrderDashboardListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [OrderDashboardListComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrderDashboardListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
