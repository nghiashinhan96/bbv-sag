import { Injectable } from '@angular/core';
import { ArticleModel, AvailabilityUtil } from 'sag-article-detail';
import { AffiliateUtil, ProjectId } from 'sag-common';
import { ArticleListConfigService } from './article-list-config.service';
@Injectable()
export class BrandPriorityAvailService {
  isCz = AffiliateUtil.isCz(this.config.affiliate);
  isSb = AffiliateUtil.isSb(this.config.affiliate);

  constructor (
    private config: ArticleListConfigService,
  ) { }

  applyBrandPriorityAvail(projectId: string, items: ArticleModel[], p: boolean) {
    return items.filter(item => {
      if (p && projectId === ProjectId.CONNECT) {
        if (item.pseudo) {
          return true;
        }
        
        if (AffiliateUtil.isAffiliateCZ10(this.config.affiliate)) {
          if (!item.allowedAddToShoppingCart) {
              return true;
          }
        }

        const isArticleCon = AvailabilityUtil.isArticleCon(item.availabilities);

        if(this.isCz) {
          return AvailabilityUtil.hasAvailCz(item.availabilities) && !isArticleCon;
        } else if(this.isSb) {
          return AvailabilityUtil.hasAvailSb(item.availabilities) && !isArticleCon;
        } else {
          return !AvailabilityUtil.articleNoAvail(item) && !isArticleCon;
        }
      }

      return item;
    });
  }
}
