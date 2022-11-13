import { Component, Input } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-article-list-search-modal',
    templateUrl: 'article-list-search-modal.component.html'
})
export class ArticleListSearchModalComponent {
    @Input() close: any;
    constructor(
        private bsModalRef: BsModalRef
    ) { }

    onClose() {
        this.bsModalRef.hide();
        if (this.close) {
            this.close();
        }
    }
}
