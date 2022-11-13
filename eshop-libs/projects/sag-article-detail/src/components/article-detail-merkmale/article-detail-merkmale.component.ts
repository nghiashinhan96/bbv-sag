import { Component, Input, OnInit } from '@angular/core';
import { ArticleModel } from '../../models/article.model';
@Component({
  selector: 'sag-article-detail-merkmale',
  templateUrl: './article-detail-merkmale.component.html',
  styleUrls: ['./article-detail-merkmale.component.scss']
})
export class SagArticleDetailMerkmaleComponent implements OnInit {
  @Input() article: ArticleModel;
  @Input() info: any;
  @Input() showLabel = false;
  @Input() shouldShowArticleNumberSection = false;
  genArtDescription = '';
  shouldShowProductAddonLine = true;

  constructor () { }

  ngOnInit() {
    this.updateGenArtInfo();
    this.checkSpecialRuleToShowProductAddon();
  }

   updateGenArtInfo() {
    if (this.article) {
      this.article.genArtTxts = this.article.genArtTxts != null ? this.article.genArtTxts : [];
      this.genArtDescription = this.article.genArtTxts.length > 0 && this.article.genArtTxts[0].gatxtdech || '';
    }
  }

  checkSpecialRuleToShowProductAddon() {
    if (this.shouldShowArticleNumberSection && this.article && this.article.productAddon) {
      if (this.article.productAddon.toUpperCase() === this.genArtDescription.toUpperCase()) {
        this.shouldShowProductAddonLine = false;
      }
    }
  }

}
