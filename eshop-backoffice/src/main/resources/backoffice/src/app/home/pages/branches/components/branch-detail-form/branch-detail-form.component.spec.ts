import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BranchDetailFormComponent } from './branch-detail-form.component';

xdescribe('BranchDetailFormComponent', () => {
  let component: BranchDetailFormComponent;
  let fixture: ComponentFixture<BranchDetailFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BranchDetailFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchDetailFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
