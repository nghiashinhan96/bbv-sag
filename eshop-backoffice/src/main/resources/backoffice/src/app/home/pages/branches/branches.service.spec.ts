import { TestBed, inject } from '@angular/core/testing';

import { BranchService } from './service/branch.service';

xdescribe('BranchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BranchService]
    });
  });

  it('should create', inject([BranchService], (service: BranchService) => {
    expect(service).toBeTruthy();
  }));
});
