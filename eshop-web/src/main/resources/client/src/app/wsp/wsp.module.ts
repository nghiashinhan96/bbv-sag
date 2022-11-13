import { TileListComponent } from './components/tile-list/tile-list.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SagCommonModule } from 'sag-common';
import { ArticleGroupSortService, SagArticleListModule } from 'sag-article-list';
import { WspComponent } from './wsp.component';
import { Routes, RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';
import { TabsModule } from 'ngx-bootstrap/tabs';
import { CardTileItemComponent } from './components/card-tile-item/card-tile-item.component';
import { CardAdsItemComponent } from './components/card-ads-item/card-ads-item.component';
import { TabelloInfoComponent } from './components/tabello-info/tabello-info.component';
import { WspArticleListComponent } from './components/wsp-article-list/wsp-article-list.component';
import { UniversalPartService } from './services/wsp.service';
import {
    ArticleInContextService,
    SagInContextModule
} from 'sag-in-context';
import { UniTreeComponent } from './components/uni-tree/uni-tree.component';
import { CardGenartItemComponent } from './components/card-genart-item/card-genart-item.component';
import { PopoverModule } from 'ngx-bootstrap/popover';
import { CzCustomModule } from '../shared/cz-custom/cz-custom.module';
import { ConnectCommonModule } from '../shared/connect-common/connect-common.module';
import { FormsModule } from '@angular/forms';
import { WspSortingService } from './services/wsp-sorting.service';

const routes: Routes = [
    {
        path: '',
        component: WspComponent
    }
];

@NgModule({
    declarations: [WspComponent, CardTileItemComponent, CardAdsItemComponent, TileListComponent, TabelloInfoComponent, WspArticleListComponent, UniTreeComponent, CardGenartItemComponent],
    imports: [
        CommonModule,
        RouterModule.forChild(routes),
        TranslateModule,
        ConnectCommonModule,
        SagCommonModule,
        SagInContextModule,
        TabsModule.forRoot(),
        SagArticleListModule,
        PopoverModule,
        CzCustomModule,
        FormsModule
    ],
    providers: [
        UniversalPartService,
        ArticleInContextService,
        WspSortingService,
        { provide: ArticleGroupSortService, useClass: WspSortingService },
    ]
})
export class WspModule { }
