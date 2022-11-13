import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerCarVehicleSearchComponent } from './passenger-car-vehicle-search.component';

xdescribe('PassengerCarVehicleSearchComponent', () => {
  let component: PassengerCarVehicleSearchComponent;
  let fixture: ComponentFixture<PassengerCarVehicleSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassengerCarVehicleSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengerCarVehicleSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
