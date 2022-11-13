import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Constant } from 'src/app/core/conts/app.constant';

@Component({
  selector: 'connect-slider-bar-chart-container',
  templateUrl: './slider-bar-chart-container.component.html',
  styleUrls: ['./slider-bar-chart-container.component.scss']
})
export class SliderBarChartContainerComponent implements OnInit {

  @Input() fullArrayDataUsedForRenderTemplate: any[];
  @Input() arrayDataUsedForChart: any[];
  @Input() selectedHistoryData: string;
  @Input() primaryColor = '';
  @Input() keyObj = {};
  @Input() loading: boolean;

  @Output() sliderValueChange = new EventEmitter<string>();

  lowerValueInput = null;
  highValueInput = null;
  inRangeValues = [];
  currentSelectedValue = '';
  isSelectAll = false;

  constructor(
  ) { }

  ngOnInit() {
    this.handleHistoryData();
  }

  private handleHistoryData() {
    // handle history data behavior
    if (this.selectedHistoryData) {
      if (this.selectedHistoryData !== Constant.SPACE) {
        const models = this.selectedHistoryData.toString().split(',');
        this.lowerValueInput = parseFloat(models[0]);
        this.highValueInput = parseFloat(models[models.length - 1]);
        this.inRangeValues = models;
      } else {
        this.lowerValueInput = null;
        this.highValueInput = null;
        this.inRangeValues = [];
      }
    }
  }

  onSliderValueChange(event) {
    const { selectedRangeValues, isSelectAll } = event;
    this.inRangeValues = selectedRangeValues;
    this.isSelectAll = isSelectAll;
    if (this.inRangeValues.length === 0) {
      return;
    }
    if (!isSelectAll) {
      this.currentSelectedValue = this.inRangeValues.join(',');
      this.lowerValueInput = parseFloat(this.inRangeValues[0]);
      this.highValueInput = parseFloat(this.inRangeValues[this.inRangeValues.length - 1]);
    } else {
      this.currentSelectedValue = Constant.SPACE;
      this.lowerValueInput = null;
      this.highValueInput = null;
    }
    this.sliderValueChange.emit(this.currentSelectedValue);
  }
}
