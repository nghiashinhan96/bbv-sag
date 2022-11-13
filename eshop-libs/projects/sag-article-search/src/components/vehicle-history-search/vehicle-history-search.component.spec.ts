import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SagSearchVehicleHistoryComponent } from './vehicle-history-search.component';

xdescribe('VehicleHistorySearchComponent', () => {
  let component: SagSearchVehicleHistoryComponent;
  let fixture: ComponentFixture<SagSearchVehicleHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SagSearchVehicleHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SagSearchVehicleHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
