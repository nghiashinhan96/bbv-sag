import { TestBed } from '@angular/core/testing';

import { OrderDasboardService } from './order-dasboard.service';

xdescribe('OrderDasboardService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: OrderDasboardService = TestBed.get(OrderDasboardService);
    expect(service).toBeTruthy();
  });
});
