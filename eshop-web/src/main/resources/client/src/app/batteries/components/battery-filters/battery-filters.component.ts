import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { BatteryFilter } from '../../models/battery-filter.model';
import { BatteriesModel } from '../../models/batteries.model';
import { ThemeService } from 'src/app/core/services/theme.service';
import { Constant } from 'src/app/core/conts/app.constant';
import { CriteriaStatistic } from 'src/app/shared/connect-common/models/criteria-statistic.model';
import { BATTERY_SEARCH_KEY } from 'src/app/core/enums/battery-search-key.enum';

@Component({
    selector: 'connect-battery-filters',
    templateUrl: './battery-filters.component.html',
    styleUrls: ['./battery-filters.component.scss']
})
export class BatteryFiltersComponent implements OnInit {

    @Input() data: BatteriesModel;
    @Input() models: BatteryFilter;
    @Input() loading: boolean;

    @Input() fullAmpeDataUsedForRenderTemplate: CriteriaStatistic[];
    @Input() ampeData: CriteriaStatistic[];
    @Input() ampereHourHistoryData: string;
    @Input() isSliderAmpeDataLoading: boolean;

    @Input() fullLengthDataUsedForRenderTemplate: CriteriaStatistic[];
    @Input() lengthData: CriteriaStatistic[];
    @Input() lengthHistoryData: string;
    @Input() isSliderLengthDataLoading: boolean;

    @Input() fullWidthDataUsedForRenderTemplate: CriteriaStatistic[];
    @Input() widthData: CriteriaStatistic[];
    @Input() widthHistoryData: string;
    @Input() isSliderWidthDataLoading: boolean;

    @Input() fullHeightDataUsedForRenderTemplate: CriteriaStatistic[];
    @Input() heightData: CriteriaStatistic[];
    @Input() heightHistoryData: string;
    @Input() isSliderHeightDataLoading: boolean;

    @Output() updateFilter = new EventEmitter<BatteryFilter>();
    @Output() resetFilter = new EventEmitter();
    @Output() search = new EventEmitter<BatteryFilter>();
    @Output() sliderValueChangeEmitter = new EventEmitter();

    keyObj = {
        valueKey: 'criteriaValue',
        countkey: 'numberOfArticles'
    }

    CONSTANT = Constant;
    FIELDS = BATTERY_SEARCH_KEY;

    constructor(
        public themeService: ThemeService
    ) { }

    ngOnInit() {
    }

    onUpdateFilter(fieldName) {
        this.models.isToggleSliderOn = false;
        this.models.isFilterChanged = true;
        this.models.lastUpdatedField = fieldName;
        this.updateFilter.emit(this.models);
    }

    onSearch() {
        this.search.emit(this.models);
    }

    onReset() {
        this.resetFilter.emit();
    }

    onSliderValueChange(event, key) {
        this.models.isToggleSliderOn = false;
        this.models.isFilterChanged = true;
        this.models.lastUpdatedField = key;
        this.models[key] = event;
        this.sliderValueChangeEmitter.emit(this.models);
    }

    onChangeIsRangeCb(event, fieldName) {
        let dataKey, historyKey;
        switch (fieldName) {
            case BATTERY_SEARCH_KEY.AMPERE_HOUR:
                dataKey = 'ampeData';
                historyKey = 'ampereHourHistoryData';
                break;
            case BATTERY_SEARCH_KEY.LENGTH:
                dataKey = 'lengthData';
                historyKey = 'lengthHistoryData';
                break;
            case BATTERY_SEARCH_KEY.WIDTH:
                dataKey = 'widthData';
                historyKey = 'widthHistoryData';
                break;
            case BATTERY_SEARCH_KEY.HEIGHT:
                dataKey = 'heightData';
                historyKey = 'heightHistoryData';
                break;
        }
        if (event) {
            this[dataKey] = null;
            this[historyKey] = Constant.SPACE;
        }
        this.models.isToggleSliderOn = event;
        this.models.isFilterChanged = this.models[fieldName] !== Constant.SPACE;
        this.models.lastUpdatedField = fieldName;
        this.models[fieldName] = Constant.SPACE;
        this.updateFilter.emit(this.models);
    }
}
