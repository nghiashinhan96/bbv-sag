import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Constant } from 'src/app/core/conts/app.constant';
import { ThemeService } from 'src/app/core/services/theme.service';
import { CriteriaStatistic } from 'src/app/shared/connect-common/models/criteria-statistic.model';
import { OilFilter } from '../../models/oil-filter.model';
import { OilModel } from '../../models/oil.model';

@Component({
    selector: 'connect-oil-filters',
    templateUrl: './oil-filters.component.html',
    styleUrls: ['./oil-filters.component.scss']
})
export class OilFiltersComponent {
    @Input() data: OilModel;
    @Input() models: OilFilter;
    @Input() loading: boolean;
    @Input() fullBottleSizeDataUsedForRenderTemplate: CriteriaStatistic[] = [];
    @Input() bottleSizeData: CriteriaStatistic[] = [];
    @Input() bottleSizeHistoryData = Constant.SPACE;
    @Input() isSliderDataLoading: boolean;

    @Output() updateFilter = new EventEmitter<OilFilter>();
    @Output() resetFilter = new EventEmitter();
    @Output() search = new EventEmitter<OilFilter>();

    @Output() sliderValueChangeEmitter = new EventEmitter();

    Constant = Constant;

    keyObj = {
        valueKey: 'criteriaValue',
        countkey: 'numberOfArticles'
    };

    constructor(
        public themeService: ThemeService
    ) { }

    onUpdateFilter() {
        this.updateFilter.emit(this.models);
    }

    onSearch() {
        this.search.emit(this.models);
    }

    onReset() {
        this.resetFilter.emit();
    }

    onSliderValueChange(event) {
        this.models.bottleSize = event;
        this.sliderValueChangeEmitter.emit(this.models);
    }

    onChangeIsRangeBottleSizeCb(event: boolean): void {
        if (event) {
            this.bottleSizeData = null;
            this.bottleSizeHistoryData = Constant.SPACE;
        }
        this.models.bottleSize = Constant.SPACE;
        this.updateFilter.emit(this.models);
    }
}
