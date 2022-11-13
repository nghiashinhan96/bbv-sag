import { OnInit, Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'connect-articles-in-context-classic-categories',
    templateUrl: 'classic-categories.component.html'
})
export class ArticlesInContextClassicCategoriesComponent implements OnInit {
    constructor(
        private router: Router,
        public activatedRoute: ActivatedRoute
    ) { }

    ngOnInit() {

    }

    search() {
        this.router.navigate(['../', 'articles'], { relativeTo: this.activatedRoute });
    }
}
