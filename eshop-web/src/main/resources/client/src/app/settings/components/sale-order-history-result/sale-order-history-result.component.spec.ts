import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SaleOrderHistoryResultComponent } from './sale-order-history-result.component';

xdescribe('SaleOrderHistoryResultComponent', () => {
  let component: SaleOrderHistoryResultComponent;
  let fixture: ComponentFixture<SaleOrderHistoryResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SaleOrderHistoryResultComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SaleOrderHistoryResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
