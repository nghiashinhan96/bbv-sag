import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
    selector: 'sag-advance-vehicle-search-breadcumb',
    templateUrl: './advance-vehicle-search-breadcumb.component.html'
})
export class SagAdvanceVehicleSearchBreadcrumbComponent implements OnInit {
    @Input() breadcrumbs = [];
    @Input() isHideRoot:boolean = false;

    constructor(private router: Router, private activatedRoute: ActivatedRoute) { }

    ngOnInit(): void { }

    onClickBreadcrumd(breadcrumb) {
        if (!breadcrumb.navigateTo) {
            return;
        }

        this.router.navigate([`/advance-vehicle-search/${breadcrumb.navigateTo}`], {
            relativeTo: this.activatedRoute
        });
    }

    onRootBreadcrumb() {
        this.router.navigate(['/']);
    }
}