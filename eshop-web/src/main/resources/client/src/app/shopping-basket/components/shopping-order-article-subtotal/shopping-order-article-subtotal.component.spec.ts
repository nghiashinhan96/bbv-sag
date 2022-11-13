import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { SagCommonModule } from 'sag-common';
import { SagCurrencyModule } from 'sag-currency';
import { ShoppingOrderArticleSubtotalComponent } from './shopping-order-article-subtotal.component';

xdescribe('ShoppingOrderArticleSubtotalComponent', () => {
  let component: ShoppingOrderArticleSubtotalComponent;
  let fixture: ComponentFixture<ShoppingOrderArticleSubtotalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderArticleSubtotalComponent]
    })
      .overrideTemplate(ShoppingOrderArticleSubtotalComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderArticleSubtotalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
