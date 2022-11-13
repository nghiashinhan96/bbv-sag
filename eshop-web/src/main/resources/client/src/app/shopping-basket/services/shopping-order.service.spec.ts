import { TestBed } from '@angular/core/testing';

import { ShoppingOrderService } from './shopping-order.service';

xdescribe('ShoppingOrderService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ShoppingOrderService = TestBed.get(ShoppingOrderService);
    expect(service).toBeTruthy();
  });
});
