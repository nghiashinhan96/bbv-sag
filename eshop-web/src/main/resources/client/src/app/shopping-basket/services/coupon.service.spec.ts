import { TestBed } from '@angular/core/testing';

import { CouponService } from './coupon.service';

xdescribe('CouponService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CouponService = TestBed.get(CouponService);
    expect(service).toBeTruthy();
  });
});
