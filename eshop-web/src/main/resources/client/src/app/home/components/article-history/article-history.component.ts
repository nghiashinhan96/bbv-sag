import { Component, OnInit, Input } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { get } from 'lodash';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { UserService } from 'src/app/core/services/user.service';
import { VehicleSearchService } from '../../service/vehicle-search.service';
import { SearchHistoryModalComponent } from 'src/app/shared/connect-common/components/search-history-modal/search-history-modal.component';
import { HISTORY_SEARCH_MODE } from '../../enums/search-history.enums';

@Component({
    selector: 'connect-article-history',
    templateUrl: './article-history.component.html',
    styleUrls: ['./article-history.component.scss']
})
export class ArticleHistoryComponent implements OnInit {
    @Input() custNr: string;
    constructor(
        private bsModalService: BsModalService,
        private searchService: VehicleSearchService,
        private appModal: AppModalService,
        private userService: UserService
    ) { }

    ngOnInit() { }

    showVehicleHistory() {
        this.appModal.modals = this.bsModalService.show(SearchHistoryModalComponent, {
            class: 'modal-xl',
            ignoreBackdropClick: true,
            initialState: {
                mode: HISTORY_SEARCH_MODE.ARTICLE,
                title: 'SEARCH.ARTICLE_HISTORY_TITLE'
            }
        });
    }

    viewSelectedArticleHistory(data: any) {
        this.searchService.navigateToArticleSearch(data, get(this.userService, 'userDetail.id'));
    }

}
