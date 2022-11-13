import { Injectable } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AppModalService } from 'src/app/core/services/app-modal.service';

@Injectable({
    providedIn: 'root'
})
export class CommonModalService {
    private replaceArticlesModal;
    private accessoryArticlesModal;
    private partListArticlesModal;
    private crossReferenceModal;

    constructor(
        private modalService: BsModalService,
        private appModal: AppModalService
    ) { }

    setReplaceArticlesModal(modal) {
        this.replaceArticlesModal = modal;
    }

    setAccessoryArticlesModal(modal) {
        this.accessoryArticlesModal = modal;
    }

    setPartListArticlesModal(modal) {
        this.partListArticlesModal = modal;
    }

    setCrossReferenceModal(modal) {
        this.crossReferenceModal = modal;
    }

    showReplaceModal(article) {
        if (!article) {
            return;
        }
        this.appModal.modals = this.modalService.show(this.replaceArticlesModal, {
            initialState: {
                article: article
            },
            class: 'modal-lg modal-article-list',
            ignoreBackdropClick: true
        });
    }

    showAccessoriesModal(data) {
        if (!data || !data.article) {
            return;
        }
        this.appModal.modals = this.modalService.show(this.accessoryArticlesModal, {
            initialState: {
                article: data.article,
                vehicle: data.vehicle,
                category: data.category
            },
            class: 'modal-lg modal-article-list',
            ignoreBackdropClick: true
        });
    }

    showPartsListModal(data) {
        if (!data || !data.article) {
            return;
        }
        this.appModal.modals = this.modalService.show(this.partListArticlesModal, {
            initialState: {
                article: data.article,
                vehicle: data.vehicle,
                category: data.category
            },
            class: 'modal-lg modal-article-list',
            ignoreBackdropClick: true
        });
    }

    showCrossReferenceModal(data) {
        if (!data || !data.article) {
            return;
        }
        this.appModal.modals = this.modalService.show(this.crossReferenceModal, {
            initialState: {
                article: data.article,
                vehicle: data.vehicle,
                category: data.category
            },
            class: 'modal-lg modal-article-list',
            ignoreBackdropClick: true
        });
    }
}