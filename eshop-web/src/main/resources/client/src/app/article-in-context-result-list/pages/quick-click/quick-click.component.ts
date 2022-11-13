import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
    selector: 'connect-in-context-quick-click',
    templateUrl: './quick-click.component.html'
})
export class ArticlesInContextQuickClickComponent implements OnInit {

    constructor(
        private router: Router,
        public activatedRoute: ActivatedRoute
    ) { }

    ngOnInit() { }
}
