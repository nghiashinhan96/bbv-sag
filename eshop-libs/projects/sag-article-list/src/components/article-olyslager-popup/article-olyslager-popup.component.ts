import { Component, OnInit, Input } from '@angular/core';
import { OilOption } from '../../models/oil-cate-option.model';
import { OilType } from '../../enums/oil-type.enum';
import { BsModalRef } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-article-list-olyslager-popup',
    templateUrl: './article-olyslager-popup.component.html',
    styleUrls: ['./article-olyslager-popup.component.scss']
})
export class SagArticleListOlyslagerPopupComponent implements OnInit {

    @Input() data: OilOption[] = [];

    onClosed: (data: { key: string, value: string }[]) => void;

    selectedOil = [];
    requiredtotalGroup = 0;
    oilType = OilType;
    autoSelectedOil = [];

    constructor(
        private bsModalRef: BsModalRef
    ) { }

    ngOnInit(): void {
        this.requiredtotalGroup = this.data.length;
    }

    cancel() {
        this.onClosed(null);
        this.bsModalRef.hide();
    }

    getRecommendationByTypeIdFromPopup(oil: OilOption) {
        this.selectedOil = this.selectedOil.filter(i => i.key !== oil.cateId);
        this.selectedOil.push({ key: oil.cateId, value: oil.guid });
    }

    select() {
        this.onClosed([...this.selectedOil, ...this.autoSelectedOil]);
        this.bsModalRef.hide();
    }

    onSelectApplicationId(oil: OilOption, cateId: string, selectedInput) {
        if (oil) {
            this.getRecommendationByTypeIdFromPopup(oil);
            selectedInput.checked = true;
        } else {
            this.selectedOil = this.selectedOil.filter(i => i.key !== cateId);
            selectedInput.checked = false;
        }
    }
}
