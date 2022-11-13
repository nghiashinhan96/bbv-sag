import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibMerkmaleFilterComponent } from './merkmale-filter.component';

xdescribe('MerkmaleFilterComponent', () => {
  let component: SagLibMerkmaleFilterComponent;
  let fixture: ComponentFixture<SagLibMerkmaleFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibMerkmaleFilterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibMerkmaleFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
