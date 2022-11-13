import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { OffersComponent } from './offers.component';
import { OfferDetailComponent } from './pages/offer-detail/offer-detail.component';
import { OfferCustomersComponent } from './pages/offer-customers/offer-customers.component';
import { OfferArticlesComponent } from './pages/offer-articles/offer-articles.component';
import { OfferListComponent } from './pages/offer-list/offer-list.component';
import { ArticleType } from '../core/enums/article.enum';
import { CanDeactivateGuard } from '../core/services/deactive-guard.service';


const routes: Routes = [
    {
        path: '',
        component: OffersComponent,
        data: { title: 'OFFERS.OVERVIEW_OF_ALL_OFFERS' },
        children: [{
            path: '',
            component: OfferListComponent,
            data: { title: 'OFFERS.OVERVIEW_OF_ALL_OFFERS' }
        }, {
            path: 'edit/:id',
            component: OfferDetailComponent,
            data: { title: 'OFFERS.OFFER_DETAIL.TITLE' },
            canDeactivate: [CanDeactivateGuard]
        }, {
            path: 'customers',
            component: OfferCustomersComponent,
            data: { title: 'OFFERS.OVERVIEW_OF_ALL_CUSTOMERS' }
        }, {
            path: 'works',
            component: OfferArticlesComponent,
            data: { title: 'OFFERS.OWN_WORK.OVERVIEW_OF_ALL_WORK', type: ArticleType.WORK },
        }, {
            path: 'articles',
            component: OfferArticlesComponent,
            data: { title: 'OFFERS.OWN_ARTICLE.OVERVIEW_OF_ALL_ARTICLE', type: ArticleType.ARTICLE }
        }]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class OffersRoutingModule { }
