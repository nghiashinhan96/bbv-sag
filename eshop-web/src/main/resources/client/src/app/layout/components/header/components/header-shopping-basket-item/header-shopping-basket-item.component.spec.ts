import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderShoppingBasketItemComponent } from './header-shopping-basket-item.component';

xdescribe('HeaderShoppingBasketItemComponent', () => {
  let component: HeaderShoppingBasketItemComponent;
  let fixture: ComponentFixture<HeaderShoppingBasketItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderShoppingBasketItemComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderShoppingBasketItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
