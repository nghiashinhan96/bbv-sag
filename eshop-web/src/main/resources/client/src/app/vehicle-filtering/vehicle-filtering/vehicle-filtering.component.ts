import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/internal/Subscription';
import { AppStorageService } from 'src/app/core/services/app-storage.service';

@Component({
    selector: 'connect-vehicle-filtering',
    templateUrl: './vehicle-filtering.component.html',
    styleUrls: ['./vehicle-filtering.component.scss']
})
export class VehicleFilteringComponent implements OnInit, OnDestroy {
    private queryParamsSubscription: Subscription;

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        this.queryParamsSubscription = this.activatedRoute.queryParams.subscribe(res => {
            if (res && Object.keys(res).length === 0) {
                const search = this.appStorage.vehicleFilter;
                if (search) {
                    this.router.navigate([], {
                        queryParams: {
                            search
                        },
                        relativeTo: this.activatedRoute,
                        skipLocationChange: true
                    });
                }
            }
        });
    }
    ngOnDestroy(): void {
        if (this.queryParamsSubscription) {
            this.queryParamsSubscription.unsubscribe();
        }
    }
    navigateTo(vehicleId) {
        this.router.navigate(['../', 'vehicle', vehicleId], {
            relativeTo: this.activatedRoute
        });
    }

}
