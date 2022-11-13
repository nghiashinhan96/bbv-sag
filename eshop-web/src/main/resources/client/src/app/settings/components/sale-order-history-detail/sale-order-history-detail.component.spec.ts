import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaleOrderHistoryDetailComponent } from './sale-order-history-detail.component';

xdescribe('SaleOrderHistoryDetailComponent', () => {
  let component: SaleOrderHistoryDetailComponent;
  let fixture: ComponentFixture<SaleOrderHistoryDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SaleOrderHistoryDetailComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaleOrderHistoryDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
