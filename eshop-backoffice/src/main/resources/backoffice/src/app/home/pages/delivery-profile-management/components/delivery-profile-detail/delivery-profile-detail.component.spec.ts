import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveryProfileDetailComponent } from './delivery-profile-detail.component';

xdescribe('DeliveryProfileDetailComponent', () => {
  let component: DeliveryProfileDetailComponent;
  let fixture: ComponentFixture<DeliveryProfileDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeliveryProfileDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeliveryProfileDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
