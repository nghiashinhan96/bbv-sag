import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { SubSink } from 'subsink';
import { UserService } from 'src/app/core/services/user.service';

@Component({
    selector: 'connect-advance-vehicle-search-models-page',
    templateUrl: './advance-vehicle-search-models.component.html',
})
export class AdvanceVehicleSearchModelsComponent implements OnInit, OnDestroy {
    private subs = new SubSink();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService,
        public userService: UserService,
    ) { }

    ngOnInit(): void {
        this.subs.sink = this.activatedRoute.queryParams.subscribe(res => {
            if (res && Object.keys(res).length === 0) {
                const make = this.appStorage.advanceVehicleSearchMake;
                if (make) {
                    this.router.navigate([], {
                        queryParams: {
                            make
                        },
                        relativeTo: this.activatedRoute,
                        skipLocationChange: true
                    });
                }
            }
        });
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }

    navigateTo(model) {
        if (!model) {
            return
        }

        this.appStorage.advanceVehicleSearchModel = JSON.stringify(model);

        this.router.navigate(['../', 'types'], { relativeTo: this.activatedRoute });
    }

}