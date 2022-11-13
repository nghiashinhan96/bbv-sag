import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MotorbikeShopComponent } from './motorbike-shop.component';

describe('MotorbikeShopComponent', () => {
  let component: MotorbikeShopComponent;
  let fixture: ComponentFixture<MotorbikeShopComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MotorbikeShopComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MotorbikeShopComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
