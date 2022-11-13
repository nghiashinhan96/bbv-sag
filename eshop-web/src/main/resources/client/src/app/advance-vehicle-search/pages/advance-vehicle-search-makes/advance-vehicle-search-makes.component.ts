import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppStorageService } from 'src/app/core/services/app-storage.service';

@Component({
    selector: 'connect-advance-vehicle-search-makes-page',
    templateUrl: './advance-vehicle-search-makes.component.html',
})
export class AdvanceVehicleSearchMakesComponent implements OnInit {

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService
    ) { }

    ngOnInit(): void { }

    navigateTo(make) {
        if (!make) {
            return;
        }

        this.appStorage.advanceVehicleSearchMake = JSON.stringify(make);

        this.router.navigate(['../', 'models'], { relativeTo: this.activatedRoute });
    }
}