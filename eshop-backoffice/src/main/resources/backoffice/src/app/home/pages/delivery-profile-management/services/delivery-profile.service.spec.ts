import { TestBed, inject } from '@angular/core/testing';

import { DeliveryProfileService } from './delivery-profile.service';

xdescribe('DeliveryProfileService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DeliveryProfileService]
    });
  });

  it('should be created', inject([DeliveryProfileService], (service: DeliveryProfileService) => {
    expect(service).toBeTruthy();
  }));
});
