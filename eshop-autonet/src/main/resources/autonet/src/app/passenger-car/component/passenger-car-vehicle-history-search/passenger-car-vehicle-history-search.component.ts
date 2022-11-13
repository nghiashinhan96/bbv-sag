import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { HISTORY_SEARCH_MODE } from '../../enums/search-history.enums';
import { SearchHistoryModalComponent } from '../search-history-modal/search-history-modal.component';

@Component({
  selector: 'autonet-passenger-car-vehicle-history-search',
  templateUrl: './passenger-car-vehicle-history-search.component.html',
  styleUrls: ['./passenger-car-vehicle-history-search.component.scss']
})
export class PassengerCarVehicleHistorySearchComponent implements OnInit, OnDestroy {
  modalRef: BsModalRef;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public bsModalService: BsModalService
  ) { }

  ngOnInit() {}

  ngOnDestroy() {
    if (this.modalRef) {
      this.modalRef.hide();
    }
  }

  viewSelectedVehicleHistory(vehicle: any) {
    const vehid = vehicle && vehicle.vehicleId;
    if (!vehid) {
      return;
    }
    this.router.navigate(['vehicle', vehid, 'quick-click'], {
      relativeTo: this.activatedRoute
    });
  }

  showVehicleHistory() {
    this.modalRef = this.bsModalService.show(SearchHistoryModalComponent, {
      class: 'modal-xl',
      ignoreBackdropClick: true,
      initialState: {
          mode: HISTORY_SEARCH_MODE.VEHICLE,
          title: 'SEARCH.VEHICLE_HISTORY_TITLE'
      }
    });
  }
}
