import { TestBed } from '@angular/core/testing';

import { CreditLimitService } from './credit-limit.service';

xdescribe('CreditLimitService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CreditLimitService = TestBed.get(CreditLimitService);
    expect(service).toBeTruthy();
  });
});
