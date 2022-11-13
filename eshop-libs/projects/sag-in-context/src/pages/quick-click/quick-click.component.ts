import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { SubSink } from 'subsink';

import { SagInContextStorageService } from '../../services/articles-in-context-storage.service';
import { CategoryTreeService } from 'sag-article-list';
import { SagInContextIntegrationService } from '../../services/articles-in-context-integration.service';
import { ProjectId } from 'sag-common';
import { SagInContextConfigService } from '../../services/articles-in-context-config.service';

@Component({
    selector: 'sag-in-context-quick-click',
    templateUrl: './quick-click.component.html',
    styleUrls: ['./quick-click.component.scss']
})
export class SagInContextQuickClickComponent implements OnInit {
    currentStateSingleSelectMode: boolean;

    private subs = new SubSink();

    constructor(
        private router: Router,
        public activatedRoute: ActivatedRoute,
        private categoryTreeService: CategoryTreeService,
        public appStorage: SagInContextStorageService,
        private integrationService: SagInContextIntegrationService,
        private config: SagInContextConfigService
    ) { }

    ngOnInit() {
        this.appStorage.classicViewMode = false;
        this.subs.sink = this.integrationService.userSetting$.subscribe(settings => {
            this.currentStateSingleSelectMode = settings && settings.currentStateSingleSelectMode;
            if (this.config.projectId === ProjectId.AUTONET) {
                this.currentStateSingleSelectMode = true;
            }
        });
    }

    performanceSearch() {
        this.router.navigate(['../', 'articles'], { relativeTo: this.activatedRoute });
        this.categoryTreeService.emitSearchRequest();
    }

}
