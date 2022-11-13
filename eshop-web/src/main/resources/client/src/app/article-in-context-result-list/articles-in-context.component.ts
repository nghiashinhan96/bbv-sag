import { Component, OnDestroy, OnInit } from '@angular/core';
import { ArticlesInContextIntegrationService } from './services/articles-in-context-integration.service';
import { NavigationStart, Router } from '@angular/router';
import { AppStorageService } from '../core/services/app-storage.service';
import { FavoriteCommonService } from '../shared/connect-common/services/favorite-common.service';
import { SubSink } from 'subsink';

@Component({
    selector: 'connect-articles-in-context-result-list',
    templateUrl: './articles-in-context.component.html'
})
export class ArticleInContextResultListComponent implements OnInit, OnDestroy {
    private subs = new SubSink();

    constructor(
        private integrationService: ArticlesInContextIntegrationService,
        private router: Router,
        private appStorage: AppStorageService,
        private favoriteCommonService: FavoriteCommonService
    ) {
    }

    ngOnInit() {
        this.subs.sink = this.router.events.subscribe(event => {
            if (event instanceof NavigationStart) {
                if (event.url.indexOf('/vehicle') === -1) {
                    this.appStorage.clearBasketItemSource();
                }
            }
        });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    onSelectFavoriteItem(item: any) {
        if (item.vehicleId) {
            this.favoriteCommonService.navigateToFavoriteVehicle(item);
        } else if (item.treeId && item.leafId) {
            this.appStorage.selectedFavoriteLeaf = item;
            this.router.navigate(['wsp'], {
                queryParams: {
                    treeId: item.treeId,
                    nodeId: item.leafId,
                    gaId: item.gaId
                }
            });
        } else {
            this.favoriteCommonService.navigateToFavoriteArticle(item);
        }
    }
}
