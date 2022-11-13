import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AffiliateSearchFormComponent } from './affiliate-search-form.component';

xdescribe('AffiliateSearchFormComponent', () => {
  let component: AffiliateSearchFormComponent;
  let fixture: ComponentFixture<AffiliateSearchFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AffiliateSearchFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AffiliateSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
