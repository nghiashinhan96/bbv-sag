import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibAllVehicleDialogComponent } from './all-vehicle-dialog.component';

xdescribe('AllVehicleDialogComponent', () => {
  let component: SagLibAllVehicleDialogComponent;
  let fixture: ComponentFixture<SagLibAllVehicleDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibAllVehicleDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibAllVehicleDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
