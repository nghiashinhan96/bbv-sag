import { Injectable } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { get } from 'lodash';

@Injectable({
    providedIn: 'root'
})
export class AppModalService {
    private list = [] as BsModalRef[];

    set modals(modal: BsModalRef) {
        this.list.push(modal);
    }

    closeAll() {
        this.list.reverse().forEach(ref => {
            if (ref && ref.hide) {
                ref.hide();
            }
        });
        this.list = [];
    }

    closeAddToCartModal() {
        const modals = this.list.filter(m => get(m, 'content.data.isAddToCart'));
        modals.reverse().forEach(ref => {
            ref.hide();
        });
        this.list = this.list.filter(m => !get(m, 'content.data.isAddToCart'));
    }
}

