import { Component, EventEmitter, Output, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Router, ActivatedRoute } from '@angular/router';
import {
    LIB_VEHICLE_SEARCH_DESC_YEAR,
    LIB_VEHICLE_SEARCH_VIN,
    LIB_VEHICLE_SEARCH_FREETEXT
} from 'sag-article-search';
import { AppStorageService } from 'src/app/core/services/custom-local-storage.service';

@Component({
    selector: 'autonet-passenger-car-vehicle-search',
    templateUrl: './passenger-car-vehicle-search.component.html',
    styleUrls: ['./passenger-car-vehicle-search.component.scss']
})
export class PassengerCarVehicleSearchComponent implements OnInit {
    @Output() navigateToModelPageEventEmitter = new EventEmitter();

    language = '';

    constructor(
        private translateService: TranslateService,
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private appStorage: AppStorageService
    ) {
        this.language = this.translateService.currentLang;
    }

    ngOnInit() {
    }

    searchVehicle(data) {
        const searchType = data.searchType;
        switch (searchType) {
            case LIB_VEHICLE_SEARCH_FREETEXT:
                this.router.navigate(['vehicle', data.search], {
                    relativeTo: this.activatedRoute
                });
                break;
            case LIB_VEHICLE_SEARCH_DESC_YEAR:
                this.appStorage.vehicleFilter = JSON.stringify(data.search);
                this.router.navigate(['vehicle', 'filtering'], {
                    relativeTo: this.activatedRoute
                });
                break;
            case LIB_VEHICLE_SEARCH_VIN:
                break;
        }
    }

    searchMakeModel(data) {
        const typeCode = data && data.body && data.body.typeCode;
        if (!typeCode) {
            return;
        }
        this.router.navigate(['vehicle', typeCode], {
            relativeTo: this.activatedRoute
        });
    }

    onNavigateToModelPage(make) {
        if (!make) {
            return;
        }
        this.appStorage.advanceVehicleSearchMake = JSON.stringify(make);
        this.router.navigate(['/advance-vehicle-search', 'models'], { relativeTo: this.activatedRoute });
    }
}
