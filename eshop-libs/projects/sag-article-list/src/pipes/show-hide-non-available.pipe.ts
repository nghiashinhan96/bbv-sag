import { Pipe, PipeTransform } from '@angular/core';
import { AffiliateUtil, ProjectId } from 'sag-common';
import { ArticleModel, AvailabilityUtil } from 'sag-article-detail';

@Pipe({
    name: 'sagShowHideNonAvail'
})
export class SagShowHideNonAvailPipe implements PipeTransform {
    transform(articles: ArticleModel[], params:
        {
            affiliate: string,
            projectId: string,
            hideNonAvail: boolean,
            availabilities?: any[]
        }
    ) {
        const enabled = this.enabledFilter(params.affiliate, params.projectId);

        if (!enabled || (enabled && !params.hideNonAvail)) {
            return articles;
        }

        return (articles || []).filter(art => this.hasAvail(art, params.affiliate, params.projectId, params.availabilities));
    }

    private enabledFilter(affiliate, projectId) {
        return AffiliateUtil.isAffiliateHasFilterNoAvail(affiliate, projectId);
    }

    private hasAvail(article: ArticleModel, affiliate: string, projectId: string, availabilities?: any) {
        if (article) {
            if (projectId === ProjectId.AUTONET) {
                if (article.autonetRequested) {
                    return AvailabilityUtil.hasAvailAutonet(article.availabilities);
                }
                return true;
            }
            
            if (article.pseudo) {
                return true;
            }

            if (!article.availRequested) {
                return true;
            }

            if (AffiliateUtil.isAffiliateCZ10(affiliate)) {
                if (!article.allowedAddToShoppingCart) {
                    return true;
                }
            }

            if(availabilities) {
                const artAvail = (availabilities || []).find(avail => avail.key === article.pimId);
                if (!!artAvail && !article.availabilities) {
                    article.availabilities = artAvail.value;
                }
            }

            if (projectId === ProjectId.CONNECT) {
                const isArticleCon = AvailabilityUtil.isArticleCon(article.availabilities);

                if (AffiliateUtil.isCz(affiliate)) {
                    return AvailabilityUtil.hasAvailCz(article.availabilities) && !isArticleCon;
                } else if (AffiliateUtil.isSb(affiliate)) {
                    return AvailabilityUtil.hasAvailSb(article.availabilities) && !isArticleCon;
                } else {
                    return !AvailabilityUtil.articleNoAvail(article) && !isArticleCon;
                }
            }
        }
    }
}
