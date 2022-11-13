import { Component, OnInit, Input, ChangeDetectionStrategy } from '@angular/core';
import { ArticleModel } from '../../models/article.model';

@Component({
  selector: 'sag-article-detail-manufacturer',
  templateUrl: './article-detail-manufacturer.component.html',
  styleUrls: ['./article-detail-manufacturer.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SagArticleDetailManufacturerComponent implements OnInit {
  @Input() article: ArticleModel;
  @Input() affiliateCode: string;

  manufacturerSuffix = 'm';
  brandLocation = 'https://s3.exellio.de/connect_media/common/images/brands/';

  constructor(
  ) { }

  ngOnInit() {
  }

}
