import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerCarComponent } from './passenger-car.component';

xdescribe('PassengerCarComponent', () => {
  let component: PassengerCarComponent;
  let fixture: ComponentFixture<PassengerCarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassengerCarComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengerCarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
