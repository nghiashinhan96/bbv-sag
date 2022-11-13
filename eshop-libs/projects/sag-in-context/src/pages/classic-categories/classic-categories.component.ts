import { OnInit, Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { SagInContextStorageService } from '../../services/articles-in-context-storage.service';
import { CategoryTreeService } from 'sag-article-list';

@Component({
    selector: 'sag-in-context-classic-categories',
    templateUrl: 'classic-categories.component.html',
    styleUrls: ['classic-categories.component.scss']
})
export class SagInContextClassicCategoriesComponent implements OnInit {
    constructor(
        private router: Router,
        public activatedRoute: ActivatedRoute,
        public appStorage: SagInContextStorageService,
        private categoryTreeService: CategoryTreeService
    ) { }

    ngOnInit() {
        this.appStorage.classicViewMode = true;
    }

    search() {
        this.router.navigate(['../', 'articles'], { relativeTo: this.activatedRoute });
        this.categoryTreeService.emitSearchRequest();
    }
}
