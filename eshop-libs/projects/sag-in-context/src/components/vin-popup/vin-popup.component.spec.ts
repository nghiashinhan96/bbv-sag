import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VinPopupComponent } from './vin-popup.component';

xdescribe('VinPopupComponent', () => {
  let component: VinPopupComponent;
  let fixture: ComponentFixture<VinPopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VinPopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VinPopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
