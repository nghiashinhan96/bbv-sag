import { Component, ElementRef, EventEmitter, HostListener, Input, OnDestroy, OnInit, Output, ViewChild } from "@angular/core";
import { finalize } from "rxjs/operators";
import { get } from 'lodash';
import { SAG_COMMON_DATETIME_FORMAT, BroadcastService } from "sag-common";
import { ArticleBroadcastKey, FAVORITE_BROADCAST_EVENT } from "sag-article-detail";
import { UserService } from "src/app/core/services/user.service";
import { SpinnerService } from "src/app/core/utils/spinner";
import { VehicleSearchService } from "src/app/home/service/vehicle-search.service";
import { SubSink } from "subsink";
import { HeaderSearchService } from "../../services/header-search.service";
import { BsModalService } from "ngx-bootstrap/modal";
import { HISTORY_SEARCH_MODE, FROM_SOURCE } from "src/app/home/enums/search-history.enums";
import { AppModalService } from "src/app/core/services/app-modal.service";
import { SearchHistoryModalComponent } from "src/app/shared/connect-common/components/search-history-modal/search-history-modal.component";
import { AppStorageService } from "src/app/core/services/app-storage.service";
import { Router } from "@angular/router";
import { PopoverDirective } from "ngx-bootstrap/popover";
import { FavoriteCommonService } from "src/app/shared/connect-common/services/favorite-common.service";

@Component({
    selector: 'connect-header-search-dashboard',
    templateUrl: './header-search-dashboard.component.html'
})
export class HeaderSearchDashboardComponent implements OnInit, OnDestroy {
    @Input() container: HTMLElement;
    @Output() outsideClick = new EventEmitter();

    @ViewChild('pop', { static: false }) customPopover: PopoverDirective;

    private subs = new SubSink();
    data: any;
    dateTimeFormat = SAG_COMMON_DATETIME_FORMAT;

    constructor(
        private ref: ElementRef,
        private broadcaster: BroadcastService,
        private bsModalService: BsModalService,
        private router: Router,
        private appModal: AppModalService,
        private appStorageService: AppStorageService,
        private userService: UserService,
        private headerSearchService: HeaderSearchService,
        private searchService: VehicleSearchService,
        private favoriteCommonService: FavoriteCommonService
    ) { }

    @HostListener('document:click', ['$event']) onClickHandler(event: any) {
        const isInside = this.ref.nativeElement.contains(event.target);
        const isContainer = this.container && this.container.contains(event.target);
        const isSelectOption = event.target.querySelector('.header-search-option');
        const editModal = document.querySelector('modal-container .article-favorite-edit-modal');
        const editModalContainer = editModal && editModal.parentElement || editModal;
        const isEditComment = editModalContainer && (editModalContainer.contains(event.target) || editModalContainer.isSameNode(event.target));
        const isSelectLastestComment = event.target.classList.contains('lastest-comment-item');
        if (isInside || isContainer || isSelectOption || isEditComment || isSelectLastestComment) {
            return;
        }
        this.outsideClick.emit();
    }

    ngOnInit() {
        const spinner = SpinnerService.start('connect-header-search-dashboard .row');
        const source = get(this.userService, 'userDetail.isSalesOnBeHalf') ? FROM_SOURCE.C4S : FROM_SOURCE.C4C;
        this.subs.sink = this.headerSearchService.getDashboardData(source)
            .pipe(
                finalize(() => SpinnerService.stop(spinner))
            )
            .subscribe(data => {
                this.data = data;
            });

        this.subs.sink = this.broadcaster.on(ArticleBroadcastKey.FAVORITE_ITEM_EVENT).subscribe((data: any) => {
            if (!this.data || !this.data.unipartFavotite || this.data.unipartFavotite.length === 0) {
                return;
            }
            switch (data.action) {
                case FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_LEAF:
                case FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_ARTICLE:
                case FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_VEHICLE:
                    const conditionsToFilterOutItemsInUnipartFavorite = [FAVORITE_BROADCAST_EVENT.REMOVE_ARTICLE, FAVORITE_BROADCAST_EVENT.REMOVE_LEAF, FAVORITE_BROADCAST_EVENT.REMOVE_VEHICLE];
                    if (conditionsToFilterOutItemsInUnipartFavorite.indexOf(data.mode) !== -1) {
                        this.data.unipartFavotite = this.data.unipartFavotite.filter(item => item.id !== data.data.id);
                    }
                    break;
                case FAVORITE_BROADCAST_EVENT.EDIT_ARTICLE:
                case FAVORITE_BROADCAST_EVENT.EDIT_LEAF_COMMENT:
                case FAVORITE_BROADCAST_EVENT.EDIT_VEHICLE:
                    const edittedItem = this.data.unipartFavotite.find(item => item.id === data.data.id);
                    if (edittedItem) {
                        edittedItem.comment = data.data.comment;
                    }
            }
        });
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    showArticleHistory() {
        this.appModal.modals = this.bsModalService.show(SearchHistoryModalComponent, {
            class: 'modal-xl',
            ignoreBackdropClick: true,
            initialState: {
                mode: HISTORY_SEARCH_MODE.ARTICLE,
                title: 'SEARCH.ARTICLE_HISTORY_TITLE'
            }
        });
    }

    showVehicleHistory() {
        this.appModal.modals = this.bsModalService.show(SearchHistoryModalComponent, {
            class: 'modal-xl',
            ignoreBackdropClick: true,
            initialState: {
                mode: HISTORY_SEARCH_MODE.VEHICLE,
                title: 'SEARCH.VEHICLE_HISTORY_TITLE'
            }
        });
    }

    viewArticle(article) {
        this.searchService.navigateToArticleSearch(article, get(this.userService, 'userDetail.id'));
    }

    viewVehicle(vehicle) {
        this.searchService.navigateToHistory(vehicle);
    }

    viewFavorite(item) {
        if (item.vehicleId) {
            this.favoriteCommonService.navigateToFavoriteVehicle(item);
        } else if (item.treeId && item.leafId) {
            this.router.navigate(['wsp'], {
                queryParams: {
                    treeId: item.treeId,
                    nodeId: item.leafId,
                    gaId: item.gaId
                }
            });
            this.appStorageService.selectedFavoriteLeaf = item;
        } else {
            this.favoriteCommonService.navigateToFavoriteArticle(item);
        }
    }
}
