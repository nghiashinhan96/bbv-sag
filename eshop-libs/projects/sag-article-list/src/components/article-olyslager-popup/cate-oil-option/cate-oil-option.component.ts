import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { OilType } from '../../../enums/oil-type.enum';
import { OilChoice } from '../../../models/oil-cate-choice.model';
import { OilOption } from '../../../models/oil-cate-option.model';

@Component({
    selector: 'sag-article-list-cate-oil-option',
    templateUrl: './cate-oil-option.component.html',
    styleUrls: ['./cate-oil-option.component.scss']
})
export class SagArticleListCateOilOptionComponent implements OnInit {
    @Input() set option(op: OilChoice) {
        if (op) {
            this.selectedOil = [new OilChoice(op)];
        }
    }

    @Output() selected = new EventEmitter<any>();
    cateId: string;
    selectedOil: OilChoice[] = [];
    requiredtotalGroup = 0;
    data = [];
    constructor() { }

    ngOnInit(): void { }

    onSelect(selected: OilOption, index: number) {
        this.removelowerSelectedValue(index);
        if (selected.type === OilType.Choice && !!selected.choice) {
            this.selectedOil.push(selected.choice);
            this.cateId = null;
            this.selected.emit(null);
        } else {
            this.cateId = selected.cateId;
            this.selected.emit(selected);
        }

    }

    private removelowerSelectedValue(index) {
        const totalDeleted = this.selectedOil.length - (index + 1);
        if (totalDeleted > 0) {
            this.selectedOil.splice((index + 1), totalDeleted);
        }
    }
}
