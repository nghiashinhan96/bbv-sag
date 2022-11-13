import { TestBed } from '@angular/core/testing';

import { ShoppingExportService } from './shopping-export.service';

xdescribe('ShoppingExportService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ShoppingExportService = TestBed.get(ShoppingExportService);
    expect(service).toBeTruthy();
  });
});
