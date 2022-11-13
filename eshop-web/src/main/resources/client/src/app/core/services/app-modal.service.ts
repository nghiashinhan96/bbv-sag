import { Injectable } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Injectable({
    providedIn: 'root'
})
export class AppModalService {
    private list = [] as BsModalRef[];

    set modals(modal: BsModalRef) {
        this.list.push(modal);
    }

    closeAll() {
        this.list.forEach(ref => {
            ref.hide();
        });
        this.list = [];
    }
}

