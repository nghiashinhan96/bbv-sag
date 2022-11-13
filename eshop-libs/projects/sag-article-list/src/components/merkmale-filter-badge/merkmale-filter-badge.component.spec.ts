import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MerkmaleFilterBadgeComponent } from './merkmale-filter-badge.component';

xdescribe('MerkmaleFilterBadgeComponent', () => {
  let component: MerkmaleFilterBadgeComponent;
  let fixture: ComponentFixture<MerkmaleFilterBadgeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MerkmaleFilterBadgeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MerkmaleFilterBadgeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
