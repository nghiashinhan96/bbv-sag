import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PriceOfferComponent } from './price-offer.component';

xdescribe('PriceOfferComponent', () => {
  let component: PriceOfferComponent;
  let fixture: ComponentFixture<PriceOfferComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PriceOfferComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PriceOfferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
