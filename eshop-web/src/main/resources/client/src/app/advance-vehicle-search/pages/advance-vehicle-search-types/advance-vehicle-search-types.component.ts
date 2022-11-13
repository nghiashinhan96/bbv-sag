import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { VehicleListService } from 'sag-vehicle-list';
import { SubSink } from 'subsink';
import { first } from 'rxjs/operators';

@Component({
    selector: 'connect-advance-vehicle-search-types-page',
    templateUrl: './advance-vehicle-search-types.component.html',
})
export class AdvanceVehicleSearchTypesComponent implements OnInit, OnDestroy {
    private subs = new SubSink();

    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService,
        private analyticService: AnalyticLoggingService,
        private vehicleListService: VehicleListService
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

    navigateToPartList(data) {
        if (!data) {
            return;
        }

        if (!data.brand || !data.model || !data.type) {
            return;
        }

        let yearFrom = `${this.vehicleListService.formatDateWithMonthAndYear(data.type.vehicle_built_year_from)}`;
        let yearTill = `${this.vehicleListService.formatDateWithMonthAndYear(data.type.vehicle_built_year_till)}`;
        const eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
        const request = this.analyticService.createMakeModelTypeSearchEventData(
            {
                vehSearchTypeSelected: 'veh_make_model_paged',
                vehBrandSelected: data.brand.label,
                vehModelSelected: `${data.model.label} [${yearFrom} - ${yearTill}]`,
                vehTypeSelected: `${data.type.vehicle_name} ${data.type.vehicle_power_kw} kW ${data.type.vehicle_engine_code}`,
                vehVehicleIdResult: data.type.vehid,
                vehVehicleNameResult: `${data.brand.label} ${data.model.label} [${yearFrom} - ${yearTill}] ${data.type.vehicle_fuel_type} ${data.type.vehicle_name} ${data.type.vehicle_power_kw} kW ${data.type.vehicle_engine_code}`
            }, false, true
        );

        this.analyticService.postEventFulltextSearch(request, eventType)
            .pipe(first())
            .toPromise();
    }
}