import { TestBed } from '@angular/core/testing';

import { AppInitService } from './app-init.service';

xdescribe('AppInitService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AppInitService = TestBed.get(AppInitService);
    expect(service).toBeTruthy();
  });
});
