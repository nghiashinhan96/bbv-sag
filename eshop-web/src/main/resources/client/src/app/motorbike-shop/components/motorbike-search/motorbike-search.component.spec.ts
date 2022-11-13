import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MotorbikeSearchComponent } from './motorbike-search.component';

describe('MotorbikeSearchComponent', () => {
  let component: MotorbikeSearchComponent;
  let fixture: ComponentFixture<MotorbikeSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MotorbikeSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MotorbikeSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
