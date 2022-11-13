import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { MockCartItem } from 'src/tests/mock-data/cartItem';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { ShoppingOrderArticleTotalPriceComponent } from './shopping-order-article-total-price.component';

xdescribe('ShoppingOrderArticleTotalPriceComponent', () => {
  let component: ShoppingOrderArticleTotalPriceComponent;
  let fixture: ComponentFixture<ShoppingOrderArticleTotalPriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderArticleTotalPriceComponent]
    })
      .overrideTemplate(ShoppingOrderArticleTotalPriceComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderArticleTotalPriceComponent);
    component = fixture.componentInstance;
    component.cartItem = new ShoppingBasketItemModel(MockCartItem);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
