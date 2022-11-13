import { TestBed } from '@angular/core/testing';

import { AppStorageService } from './app-storage.service';

xdescribe('CustomLocalStorageService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AppStorageService = TestBed.get(AppStorageService);
    expect(service).toBeTruthy();
  });
});
