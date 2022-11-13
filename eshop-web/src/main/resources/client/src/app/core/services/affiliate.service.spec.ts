import { TestBed } from '@angular/core/testing';

import { AffiliateService } from './affiliate.service';

xdescribe('AffiliateService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AffiliateService = TestBed.get(AffiliateService);
    expect(service).toBeTruthy();
  });
});
