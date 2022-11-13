import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LicenseSearchFormComponent } from './license-search-form.component';

xdescribe('LicenseSearchFormComponent', () => {
  let component: LicenseSearchFormComponent;
  let fixture: ComponentFixture<LicenseSearchFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LicenseSearchFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LicenseSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
