import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DurationSelectionComponent } from './duration-selection.component';

xdescribe('DurationSelectionComponent', () => {
  let component: DurationSelectionComponent;
  let fixture: ComponentFixture<DurationSelectionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DurationSelectionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DurationSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
