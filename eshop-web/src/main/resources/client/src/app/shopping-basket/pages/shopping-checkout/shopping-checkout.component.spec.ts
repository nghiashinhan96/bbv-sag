import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ShoppingCheckoutComponent } from './shopping-checkout.component';

xdescribe('ShoppingCheckoutComponent', () => {
  let component: ShoppingCheckoutComponent;
  let fixture: ComponentFixture<ShoppingCheckoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingCheckoutComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingCheckoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
