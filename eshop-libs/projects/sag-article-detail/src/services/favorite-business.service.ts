import { Injectable } from "@angular/core";
import { BsModalService } from 'ngx-bootstrap/modal';
import { FAVORITE_BROADCAST_EVENT } from '../consts/article-detail.const';
import { SagArticleFavoriteEditModalComponent } from '../components/article-favorite-edit-modal/article-favorite-edit-modal.component';
import { FavoriteService } from "./favorite.service";

@Injectable({
    providedIn: 'root'
})
export class FavoriteBusinessService {

    constructor(
        private favoriteService: FavoriteService,
        private bsModalService: BsModalService
    ) { }

    showFavoriteModal(mode, data?, rootModalName?) {
        this.bsModalService.show(SagArticleFavoriteEditModalComponent, {
            class: 'modal-sm article-favorite-edit-modal',
            ignoreBackdropClick: true,
            initialState: {
                mode,
                data,
                rootModalName,
                onSave: (item) => {
                    switch (mode) {
                        case FAVORITE_BROADCAST_EVENT.ADD_ARTICLE:
                        case FAVORITE_BROADCAST_EVENT.ADD_LEAF:
                        case FAVORITE_BROADCAST_EVENT.ADD_VEHICLE:
                            this.favoriteService.addFavoriteItem(item, mode, rootModalName);
                            break;
                        case FAVORITE_BROADCAST_EVENT.EDIT_ARTICLE:
                        case FAVORITE_BROADCAST_EVENT.EDIT_LEAF:
                        case FAVORITE_BROADCAST_EVENT.EDIT_VEHICLE:
                            this.favoriteService.updateFavoriteItem(item).subscribe();
                            break;
                        default:
                            break;
                    }
                },
                onRemove: (item) => {
                    this.favoriteService.removeFavortieItem(item);
                }
            }
        });
    }
}