import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HappyPointDialogComponent } from './happy-point-dialog.component';

xdescribe('HappyPointDialogComponent', () => {
  let component: HappyPointDialogComponent;
  let fixture: ComponentFixture<HappyPointDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HappyPointDialogComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HappyPointDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
