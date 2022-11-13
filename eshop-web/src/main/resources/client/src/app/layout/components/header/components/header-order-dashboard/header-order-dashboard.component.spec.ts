import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderOrderDashboardComponent } from './header-order-dashboard.component';

xdescribe('HeaderOrderDashboardComponent', () => {
  let component: HeaderOrderDashboardComponent;
  let fixture: ComponentFixture<HeaderOrderDashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderOrderDashboardComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderOrderDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
