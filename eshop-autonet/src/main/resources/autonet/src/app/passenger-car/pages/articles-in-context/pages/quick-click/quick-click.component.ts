import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
    selector: 'autonet-quick-click',
    templateUrl: './quick-click.component.html',
    styleUrls: ['./quick-click.component.scss']
})
export class QuickClickComponent implements OnInit {

    constructor(
        private router: Router,
        public activatedRoute: ActivatedRoute,
    ) { }

    ngOnInit() { }

    performanceSearch() {
        this.router.navigate(['../', 'articles'], { relativeTo: this.activatedRoute });
    }

}
