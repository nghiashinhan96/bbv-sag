import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SagAuthenGuard } from 'sag-auth';
import { DvseGuard } from '../authentication/guard/dvse.guard';
import { PermissionGuard } from '../authentication/guard/permission.guard';
import { SaleGuard } from '../authentication/guard/sale.guard';
import { WholesalerGuard } from '../authentication/guard/wholesaler.guard';
import { LayoutComponent } from './layout.component';
import { LayoutResolver } from './layout.resolver';

const routes: Routes = [
    {
        path: '',
        component: LayoutComponent,
        canActivateChild: [LayoutResolver],
        children: [
            {
                path: '',
                pathMatch: 'full',
                redirectTo: 'home'
            },
            {
                path: 'home',
                data: {
                    page: 'home'
                },
                loadChildren: () => import('../home/home.module').then(m => m.HomeModule)
            },
            {
                path: 'vehicle/:vehicleId',
                loadChildren: () => import('./../article-in-context-result-list/articles-in-context.module')
                    .then(m => m.ArticlesInContextModule)
            },
            {
                path: 'vin',
                loadChildren: () => import('./../gtmotive/gtmotive.module')
                    .then(m => m.GtmotiveModule)
            },
            {
                path: 'article',
                loadChildren: () => import('../article-not-context-result-list/article-not-context-result-list.module')
                    .then(m => m.ArticleNotContextResultListModule)
            },
            {
                path: 'article-list',
                loadChildren: () => import('../article-list/article-list-search.module')
                    .then(m => m.ArticleListSearchModule)
            },
            {
                path: 'vehicle-filtering',
                loadChildren: () => import('./../vehicle-filtering/vehicle-filtering.module')
                    .then(m => m.VehicleFilteringModule)
            },            {
                path: 'advance-vehicle-search',
                loadChildren: () => import('./../advance-vehicle-search/advance-vehicle-search.module')
                    .then(m => m.AdvanceVehicleSearchModule)
            },
            {
                path: 'settings',
                loadChildren: () => import('../settings/settings.module').then(m => m.SettingsModule)
            },
            {
                path: 'bulbs',
                data: {
                    permission: 'BULB_URL_ACCESS'
                },
                loadChildren: () => import('../bulbs/bulbs.module').then(m => m.BulbsModule),
                canActivate: [PermissionGuard]
            },
            {
                path: 'batteries',
                data: {
                    permission: 'BATTERY_URL_ACCESS'
                },
                loadChildren: () => import('../batteries/batteries.module').then(m => m.BatteriesModule),
                canActivate: [PermissionGuard]
            },
            {
                path: 'oil',
                data: {
                    permission: 'OIL_URL_ACCESS'
                },
                loadChildren: () => import('../oil/oil.module').then(m => m.OilModule),
                canActivate: [PermissionGuard]
            },
            {
                path: 'tyres',
                data: {
                    permission: 'TYRE_URL_ACCESS'
                },
                loadChildren: () => import('../tyres/tyres.module').then(m => m.TyresModule),
                canActivate: [PermissionGuard]
            },
            {
                path: 'wsp',
                data: {
                    permission: 'UNIPARTS_URL_ACCESS'
                },
                loadChildren: () => import('../wsp/wsp.module').then(m => m.WspModule),
                canActivate: [PermissionGuard]
            },
            {
                path: 'offers',
                data: {
                    permission: 'OFFER',
                    isSalesOnBeHalf: false
                },
                loadChildren: () => import('../offers/offers.module').then(m => m.OffersModule),
                canActivate: [PermissionGuard, SaleGuard]
            },
            {
                path: 'order-dashboard',
                loadChildren: () => import('../order-dashboard/order-dashboard.module').then(m => m.OrderDashboardModule),
                canActivate: [WholesalerGuard]
            },
            {
                path: 'shopping-basket',
                loadChildren: () => import('../shopping-basket/shopping-basket.module').then(m => m.ShoppingBasketModule)
            },
            {
                path: 'thule',
                loadChildren: () => import('../thule/thule.module').then(m => m.ThuleModule)
            },
            {
                path: 'return',
                loadChildren: () => import('../article-return/article-return.module').then(m => m.ArticleReturnModule)
            },
            {
                path: 'dvse',
                loadChildren: () => import('../dvse-catalog/dvse-catalog.module').then(m => m.DVSECatalogModule),
                canActivateChild: [DvseGuard]
            },
            {
                path: 'unicat',
                loadChildren: () => import('../unicat-catalog/unicat-catalog.module').then(m => m.UnicatCatalogModule)
            },
            {
                path: 'saginfo',
                loadChildren: () => import('../stg-info/stg-info.module').then(m => m.STGInfoModule)
            },
            {
                path: '404',
                canActivate: [SagAuthenGuard],
                loadChildren: () => import('../404/404.module').then(m => m.NotFoundModule)
            },
            {
                path: 'wholesaler',
                data: {
                    permission: 'WHOLESALER'
                },
                canActivate: [SagAuthenGuard, PermissionGuard, WholesalerGuard],
                loadChildren: () => import('../wholesaler/wholesaler.module').then(m => m.WholesalerModule)
            },
            {
                path: 'moto',
                data: {
                    permission: 'MOTO_URL_ACCESS'
                },
                loadChildren: () => import('../motorbike-shop/motorbike-shop.module').then(m => m.MotorbikeShopModule),
                canActivate: [PermissionGuard]
            },
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class LayoutRoutingModule { }
