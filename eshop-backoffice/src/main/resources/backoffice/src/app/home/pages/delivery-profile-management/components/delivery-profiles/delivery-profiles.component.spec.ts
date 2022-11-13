import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryProfilesComponent } from './delivery-profiles.component';

xdescribe('DeliveryProfilesComponent', () => {
  let component: DeliveryProfilesComponent;
  let fixture: ComponentFixture<DeliveryProfilesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeliveryProfilesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryProfilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
