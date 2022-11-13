import { TestBed } from '@angular/core/testing';

import { SubShoppingBasketService } from './sub-shopping-basket.service';

xdescribe('SubShoppingBasketService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SubShoppingBasketService = TestBed.get(SubShoppingBasketService);
    expect(service).toBeTruthy();
  });
});
