import { Component, EventEmitter, Input, Output, OnChanges, SimpleChanges } from '@angular/core';
import { SagArticleShowPriceComponent } from '../article-show-price/article-show-price.component';
import { ArticleModel } from './../../models/article.model';

@Component({
  selector: 'sag-article-gross-price',
  templateUrl: './article-gross-price.component.html',
  styleUrls: ['./article-gross-price.component.scss']
})
export class SagArticleGrossPriceComponent extends SagArticleShowPriceComponent implements OnChanges {
  @Input() article: ArticleModel;
  @Input() disabled: boolean;
  @Input() priceType: string;
  @Input() customPriceBrand: any = null;
  // Use for lazy loading the price from ERP
  @Input() set price(val: any) {
    if (this.article && val) {
        this.article.price = val;
        this.initData();
    }
  }

  @Output() onCustomPriceChangeEmit = new EventEmitter<any>();

  ngOnChanges(changes: SimpleChanges): void {
    super.ngOnChanges(changes);

    if (changes.article && !changes.article.firstChange) {
      this.initData();
    }
  }

  initData() {
    if (!this.isPDP) {
      this.article.setGrossPrice(this.currentStateVatConfirm);
    }
  }

  initialItem() {
    return this.article;
  }

  onCustomPriceChange(event) {
    this.onCustomPriceChangeEmit.emit(event);
  }
}
