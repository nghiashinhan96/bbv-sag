import { Injectable } from '@angular/core';
import { AutonetFrameDialogComponent } from 'src/app/shared/autonet-common/dialogs/autonet-frame-dialog/autonet-frame-dialog.component';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Subject } from 'rxjs/internal/Subject';
import { ArticleModel } from 'sag-article-detail';
import { AppModalService } from 'src/app/core/services/app-modal.service';

@Injectable({ providedIn: 'root' })
export class AutonetCommonService {
    private freetextSearchSub = new Subject<boolean>();
    freetextSearchObs = this.freetextSearchSub.asObservable();

    private currentPageSub = new Subject<string>();
    currentPageObs = this.currentPageSub.asObservable();

    isInArticleListPage = false;

    constructor(
        private modalService: BsModalService,
        private appModalService: AppModalService
    ) { }

    enableFreetextSearch(status: boolean = false) {
        this.freetextSearchSub.next(status);
    }

    emitPageName(pageName: string) {
        this.currentPageSub.next(pageName);
    }

    addToCart(article: ArticleModel) {
        const data = {
            autonetId: article.pimId,
            quantity: article.salesQuantity,
            description: '',
            dlnrId: article.dlnrId,
            artnr: article.artnr,
            gaID: article.gaID,
            isAddToCart: true
        };
        this.appModalService.modals = this.modalService.show(AutonetFrameDialogComponent, {
            class: 'add-to-card modal-lg',
            ignoreBackdropClick: true,
            initialState: {
                data
            }
        });
    }
}
