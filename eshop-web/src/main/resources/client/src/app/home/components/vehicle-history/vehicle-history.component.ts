import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BsModalService } from 'ngx-bootstrap/modal';
import { VehicleSearchHistory } from 'sag-article-search';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { HISTORY_SEARCH_MODE } from '../../enums/search-history.enums';
import { VehicleSearchService } from '../../service/vehicle-search.service';
import { SearchHistoryModalComponent } from 'src/app/shared/connect-common/components/search-history-modal/search-history-modal.component';

@Component({
    selector: 'connect-vehicle-history',
    templateUrl: './vehicle-history.component.html',
    styleUrls: ['./vehicle-history.component.scss']
})
export class VehicleHistoryComponent implements OnInit {
    @Input() custNr: string;
    @Input() vehicleClass?: string;
    constructor(
        private router: Router,
        private activatedRoute: ActivatedRoute,
        private bsModalService: BsModalService,
        private vehicleSearchService: VehicleSearchService,
        private appModal: AppModalService
    ) { }

    ngOnInit() { }

    showVehicleHistory() {
        this.appModal.modals = this.bsModalService.show(SearchHistoryModalComponent, {
            class: 'modal-xl',
            ignoreBackdropClick: true,
            initialState: {
                mode: HISTORY_SEARCH_MODE.VEHICLE,
                title: 'SEARCH.VEHICLE_HISTORY_TITLE',
                vehicleClass: this.vehicleClass
            }
        });
    }

    viewSelectedVehicleHistory(vehicle: VehicleSearchHistory) {
        this.vehicleSearchService.navigateToHistory(vehicle);
    }

}
