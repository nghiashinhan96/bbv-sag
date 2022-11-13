import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerCarVehicleHistorySearchComponent } from './passenger-car-vehicle-history-search.component';

xdescribe('PassengerCarVehicleHistorySearchComponent', () => {
  let component: PassengerCarVehicleHistorySearchComponent;
  let fixture: ComponentFixture<PassengerCarVehicleHistorySearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassengerCarVehicleHistorySearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengerCarVehicleHistorySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
