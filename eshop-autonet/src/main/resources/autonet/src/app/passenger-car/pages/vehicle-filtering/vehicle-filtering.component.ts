import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { AutonetCommonService } from 'src/app/shared/autonet-common/autonet-common.service';

@Component({
    selector: 'autonet-vehicle-filtering',
    templateUrl: './vehicle-filtering.component.html',
    styleUrls: ['./vehicle-filtering.component.scss']
})
export class VehicleFilteringComponent implements OnInit, OnDestroy {
    private queryParamsSubscription: Subscription;
    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService,
        private autonetCommonService: AutonetCommonService
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

        setTimeout(() => {
            this.autonetCommonService.emitPageName('autonet-vehicle-filtering');
        });
    }

    ngOnDestroy(): void {
        if (this.queryParamsSubscription) {
            this.queryParamsSubscription.unsubscribe();
        }
    }


    navigateTo(vehicleId) {
        this.router.navigate(['../../', vehicleId], { relativeTo: this.activatedRoute });
    }
}
