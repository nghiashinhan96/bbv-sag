import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleInfoDialogComponent } from './vehicle-info-dialog.component';

xdescribe('VehicleInfoDialogComponent', () => {
  let component: VehicleInfoDialogComponent;
  let fixture: ComponentFixture<VehicleInfoDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VehicleInfoDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VehicleInfoDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
