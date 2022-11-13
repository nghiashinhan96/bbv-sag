import { TestBed } from '@angular/core/testing';

import { ShoppingBasketHistoryService } from './shopping-basket-history.service';

xdescribe('ShoppingBasketHistoryService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ShoppingBasketHistoryService = TestBed.get(ShoppingBasketHistoryService);
    expect(service).toBeTruthy();
  });
});
