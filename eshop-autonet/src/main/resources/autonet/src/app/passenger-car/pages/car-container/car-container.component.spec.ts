import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CarContainerComponent } from './car-container.component';

xdescribe('CarContainerComponent', () => {
  let component: CarContainerComponent;
  let fixture: ComponentFixture<CarContainerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CarContainerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CarContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
