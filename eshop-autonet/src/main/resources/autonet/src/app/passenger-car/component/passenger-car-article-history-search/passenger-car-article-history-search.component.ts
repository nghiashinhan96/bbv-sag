import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { HISTORY_SEARCH_MODE } from '../../enums/search-history.enums';
import { VehicleSearchService } from '../../services/vehicle-search.service';
import { SearchHistoryModalComponent } from '../search-history-modal/search-history-modal.component';

@Component({
  selector: 'autonet-passenger-car-article-history-search',
  templateUrl: './passenger-car-article-history-search.component.html',
  styleUrls: ['./passenger-car-article-history-search.component.scss']
})
export class PassengerCarArticleHistorySearchComponent implements OnInit, OnDestroy {
  modalRef: BsModalRef;
  
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public bsModalService: BsModalService,
    private searchService: VehicleSearchService
  ) { }

  ngOnInit() {}

  ngOnDestroy() {
    if (this.modalRef) {
      this.modalRef.hide();
    }
  }

  viewSelectedHistory(data: any) {
    this.searchService.navigateToArticleSearch(data);
  }

  showVehicleHistory() {
    this.modalRef = this.bsModalService.show(SearchHistoryModalComponent, {
      class: 'modal-xl',
      ignoreBackdropClick: true,
      initialState: {
          mode: HISTORY_SEARCH_MODE.ARTICLE,
          title: 'SEARCH.ARTICLE_HISTORY_TITLE'
      }
    });
  }
}
