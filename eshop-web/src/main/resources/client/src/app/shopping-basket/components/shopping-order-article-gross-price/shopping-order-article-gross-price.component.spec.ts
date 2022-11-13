import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { SagCurrencyModule } from 'sag-currency';
import { ShoppingOrderArticleGrossPriceComponent } from './shopping-order-article-gross-price.component';

xdescribe('ShoppingOrderArticleGrossPriceComponent', () => {
  let component: ShoppingOrderArticleGrossPriceComponent;
  let fixture: ComponentFixture<ShoppingOrderArticleGrossPriceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderArticleGrossPriceComponent],
      imports: [
        SagCurrencyModule.forRoot()
      ]
    })
      .overrideTemplate(ShoppingOrderArticleGrossPriceComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderArticleGrossPriceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
