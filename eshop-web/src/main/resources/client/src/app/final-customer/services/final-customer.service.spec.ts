import { TestBed } from '@angular/core/testing';

import { FinalCustomerService } from './final-customer.service';

xdescribe('FinalCustomerService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FinalCustomerService = TestBed.get(FinalCustomerService);
    expect(service).toBeTruthy();
  });
});
