import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagLibVehicleListComponent } from './vehicle-list.component';

xdescribe('VehicleListComponent', () => {
  let component: SagLibVehicleListComponent;
  let fixture: ComponentFixture<SagLibVehicleListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagLibVehicleListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagLibVehicleListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
