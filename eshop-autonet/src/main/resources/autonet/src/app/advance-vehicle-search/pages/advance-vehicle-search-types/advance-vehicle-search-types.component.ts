import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';
import { SubSink } from 'subsink';

@Component({
    selector: 'autonet-advance-vehicle-search-types-page',
    templateUrl: './advance-vehicle-search-types.component.html',
})
export class AdvanceVehicleSearchTypesComponent implements OnInit, OnDestroy {
    private subs = new SubSink();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService
    ) { }

    ngOnInit(): void {
        this.subs.sink = this.activatedRoute.queryParams.subscribe(res => {
            if (res && Object.keys(res).length === 0) {
                try {
                    let modelId = '';
                    let makeId = '';
                    if (JSON.parse(this.appStorage.advanceVehicleSearchMake) && JSON.parse(this.appStorage.advanceVehicleSearchModel)) {
                        makeId = JSON.parse(this.appStorage.advanceVehicleSearchMake).id;
                        modelId = JSON.parse(this.appStorage.advanceVehicleSearchModel).id;
                    }
                    const make = this.appStorage.advanceVehicleSearchMake;
                    const model = this.appStorage.advanceVehicleSearchModel;
                    const search = JSON.stringify({
                        term: {
                            modelId: modelId,
                            makeId: makeId
                        },
                        filter: {
                            body_type: '',
                            fuel_type: '',
                            zylinder: '',
                            capacity_cc_tech: '',
                            motor_code: '',
                            power: '',
                            drive_type: '',
                            vehicle_advance_name: ''
                        }
                    });
                    if (make) {
                        this.router.navigate([], {
                            queryParams: {
                                search,
                                make,
                                model
                            },
                            relativeTo: this.activatedRoute,
                            skipLocationChange: true
                        });
                    }
                } catch (ex) { }
            }
        });
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }
}