import { CustomStepDefinition, Options } from '@angular-slider/ngx-slider';
import { Component, EventEmitter, Input, OnChanges, OnDestroy, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { NgSelectComponent } from '@ng-select/ng-select';
import { ChartDataSets, ChartOptions, ChartType } from 'chart.js';
import { BaseChartDirective, Label } from 'ng2-charts';
import { CurrencyUtil } from 'sag-currency';
import { SubSink } from 'subsink';

interface SliderValueChange {
  selectedRangeValues: any[];
  isSelectAll : boolean;
}
@Component({
  selector: 'connect-slider-bar-chart',
  templateUrl: './slider-bar-chart.component.html',
  styleUrls: ['./slider-bar-chart.component.scss']
})
export class SliderBarChartComponent implements OnInit, OnChanges, OnDestroy {
  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective | undefined;
  @ViewChild('lowerInput', {static: true}) lowerInput: NgSelectComponent;
  @ViewChild('highInput', {static: true}) highInput: NgSelectComponent;
  readonly defaultLowerValue = 0;
  readonly defaultHighValue = 0;
  @Input() lowerValueInput = this.defaultLowerValue;
  @Input() highValueInput = this.defaultHighValue;
  @Input() primaryColor = '';
  @Input() fullArrayDataUsedForRenderTemplate: any[];
  @Input() arrayDataUsedForChart: any[];
  @Input() keyObj = {
    valueKey: 'value',
    countkey: 'count'
  };

  @Output() sliderValueChange = new EventEmitter<SliderValueChange>();
  
  finalChartValues = [];
  inRangeValues = [];
  rawValuesKey = 'rawValues'; // used for keep raw value from source data
  finalChartValuesWithRaw = [];
  private subs = new SubSink();
  lowerValue = this.defaultLowerValue;
  highValue = this.defaultHighValue;
  minRangeBetween2SliderPointer = 1;

  // --- slider options
  options: Options = {
    floor: this.defaultLowerValue,
    ceil: this.defaultHighValue,
    noSwitching: true,
    minRange: this.minRangeBetween2SliderPointer
  };

  // --- chart options
  inRangeColor = '#0000ff';
  outRangeColor = '#808080';
  barChartType: ChartType = 'bar';
  barChartLabels: Label[] = [];
  barChartData: ChartDataSets[] = [
    {
      data: [],
      backgroundColor: this.inRangeColor
    }
  ]

  barChartOptions: ChartOptions = {
    responsive: true,
    scales: {
      xAxes: [{
        gridLines: {
          display: false
        },
        ticks: {
          display: false
        }
      }],
      yAxes: [{
        gridLines: {
          display: false
        },
        ticks: {
          display: false
        }
      }]
    },
    legend: {
      display: false
    }
    
  };
  lowerInputItems = [];
  highInputItems = [];

  constructor(
  ) { }

  ngOnInit() {
    
  }

  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  
  private initial(arrayData: any[]) {
    const adaptedArrayData = this.adaptArrayData(arrayData, this.keyObj.valueKey, this.keyObj.countkey);
    let finalArrayData = this.sortArrayData(adaptedArrayData, this.keyObj.valueKey);
    if (this.fullArrayDataUsedForRenderTemplate && this.fullArrayDataUsedForRenderTemplate.length !== finalArrayData.length) {
      finalArrayData = this.addPaddingDataForChartData(finalArrayData, this.keyObj.valueKey, this.keyObj.countkey);
    }
    this.finalChartValuesWithRaw = finalArrayData;
    const finalValues = (finalArrayData || []).map(item => item[this.keyObj.valueKey]);
    const finalCounts = (finalArrayData || []).map(item => item[this.keyObj.countkey]);
    this.finalChartValues = finalValues;
    const floorAndCeilObj = this.getFloorAndCeilFromArrayData(finalValues);
    this.setNewOptions(floorAndCeilObj, finalValues);
    this.inRangeColor = this.primaryColor;
    this.loadChartData(finalCounts, finalValues);
    if (!this.lowerValueInput || this.lowerValueInput === this.defaultLowerValue) {
      this.lowerValue = floorAndCeilObj.floor;
    } else {
      this.lowerValue = this.lowerValueInput;
    }
    let isHasHighValueFromInput = false;
    if (!this.highValueInput || this.highValueInput === this.defaultHighValue) {
      this.highValue = floorAndCeilObj.ceil;
    } else {
      this.highValue = this.highValueInput;
      isHasHighValueFromInput = true;
    }
    if (isHasHighValueFromInput) {
      this.updateChartBackGroundColors();
      this.getInRangeValues();
    }
    this.lowerInputItems = this.getPossibleValues();
    this.highInputItems = this.getPossibleValues(true);
  }

  ngOnChanges(changes: SimpleChanges): void {
  if (changes.fullArrayDataUsedForRenderTemplate) {
    const currentArrayDataUsedForRenderTemplate = changes.fullArrayDataUsedForRenderTemplate.currentValue;
    if (currentArrayDataUsedForRenderTemplate) {
      const adaptedArrayData = this.adaptArrayData(currentArrayDataUsedForRenderTemplate, this.keyObj.valueKey, this.keyObj.countkey);
      const finalArrayData = this.sortArrayData(adaptedArrayData, this.keyObj.valueKey);
      this.fullArrayDataUsedForRenderTemplate = finalArrayData;
    }
  }
   if (changes.arrayDataUsedForChart) {
     const currentArrayData = changes.arrayDataUsedForChart.currentValue;
     if (currentArrayData && this.fullArrayDataUsedForRenderTemplate) {
      this.initial(currentArrayData);
     }
   }
  }

  private getFloorAndCeilFromArrayData(arrayData: any[]) {
    if (!(arrayData && arrayData.length > 0)) {
      return { floor: this.defaultLowerValue, ceil: this.defaultHighValue };
    }
    const arrayLength = arrayData.length;
    const firstItem = arrayData[0];
    const lastItem = arrayData[arrayLength - 1];
    return { floor: firstItem, ceil: lastItem };
  }

  private setNewOptions(options: any, stepsArray: any[]) {
    const newOptions = Object.assign({}, this.options);
    newOptions.floor = options && options.floor || 0;
    newOptions.ceil = options && options.ceil || 0;
    if (stepsArray && stepsArray.length && stepsArray.length > 0) {
      newOptions.stepsArray = (stepsArray || []).map((item): CustomStepDefinition => {
        return { value: item };
      });
    }
    this.options = newOptions;
  }

  private loadChartData(chartData: any[], chartLabel: any[]) {
    this.barChartData[0].data = chartData || [];
    this.barChartData[0].backgroundColor = this.inRangeColor;
    this.barChartLabels = chartLabel || [];
    this.chart.update();
  }

  onSliderUserChange(event) {
    const sliderValueChangeObj: SliderValueChange = this.buildSliderValueChangeObj();
    this.sliderValueChange.emit(sliderValueChangeObj);
    this.lowerInputItems = this.getPossibleValues();
    this.highInputItems = this.getPossibleValues(true);
  }

  onSliderValueChange(event) {
    this.updateChartBackGroundColors();
    this.getInRangeValues();
  }

  onLowerInputChange(event) {
    this.lowerValue = event;
    this.getInRangeValues();
    const sliderValueChangeObj: SliderValueChange = this.buildSliderValueChangeObj();
    this.sliderValueChange.emit(sliderValueChangeObj);
    this.highInputItems = this.getPossibleValues(true);
  }

  onHighInputChange(event) {
    this.highValue = event;
    this.getInRangeValues();
    const sliderValueChangeObj: SliderValueChange = this.buildSliderValueChangeObj();
    this.sliderValueChange.emit(sliderValueChangeObj);
    this.lowerInputItems = this.getPossibleValues();
  }

  private getInRangeValues() {
    const inRangeValuesTemp = (this.finalChartValuesWithRaw || []).filter(item => {
      const value = item[this.keyObj.valueKey];
      return value >= this.lowerValue && value <= this.highValue;
    }).map(item => item[this.rawValuesKey]);
    this.inRangeValues = inRangeValuesTemp.reduce((acc, val) => acc.concat(val), []);
  }

  private updateChartBackGroundColors() {
    const backgroundColors: string[] = [];
    (this.finalChartValues || []).forEach((elementValue: number) => {
      if (elementValue < this.lowerValue || elementValue > this.highValue) {
        backgroundColors.push(this.outRangeColor);
      } else {
        backgroundColors.push(this.inRangeColor);
      }
    });
    this.barChartData[0].backgroundColor = backgroundColors;
    this.chart.update();
  }

  private adaptArrayData(arrayData: any[], valueKey: string, countKey: string) {
    return (arrayData || []).reduce( (previousValue: any[], currentValue) => {
      const value = currentValue[valueKey];
      const count = currentValue[countKey];
      let floatValue;
      floatValue = CurrencyUtil.formatCommaNumber(value.toString());
      const foundDuplicatedItemIndex = previousValue.findIndex(item => item[valueKey] === floatValue);
      // sum count value if found duplicated item
      if (foundDuplicatedItemIndex !== -1) {
        const foundDuplicatedItem = previousValue[foundDuplicatedItemIndex];
        foundDuplicatedItem[countKey] = foundDuplicatedItem[countKey] + count;
        let rawValues = foundDuplicatedItem[this.rawValuesKey];
        rawValues = [...rawValues, value];
        foundDuplicatedItem[this.rawValuesKey] = rawValues;
        return previousValue;
      } else {
        // push new item to previousValue
        return [...previousValue, {
          ...currentValue,
          [valueKey]: floatValue,
          [this.rawValuesKey]: [value]
        }]
      }
    }, []);
  }

  private sortArrayData(arrayData: any[], key: string) {
    return (arrayData || []).sort((a, b) => {
      return a[key] - b[key]
    });
  }

  private addPaddingDataForChartData(finalArrayData: any[], valueKey: string, countKey: string) {
    const newFinalValues = [];
    (this.fullArrayDataUsedForRenderTemplate || []).forEach(element => {
      const foundItem = (finalArrayData || []).find(item => item[valueKey] === element[valueKey]);
      if (foundItem) {
        newFinalValues.push(foundItem);
      } else {
        newFinalValues.push({
          [valueKey]: element[valueKey],
          [countKey]: 0,
          [this.rawValuesKey]: element[this.rawValuesKey]
        });
      }
    });
    return newFinalValues;
  }

  private buildSliderValueChangeObj(): SliderValueChange {
    return {
      selectedRangeValues: this.inRangeValues,
      isSelectAll: this.lowerValue === this.options.floor && this.highValue === this.options.ceil
    };
  }

  private getPossibleValues(isHighInput?: boolean) {
    let result = [];
    result = this.finalChartValues.filter(item => {
      return isHighInput ? item > this.lowerValue : item < this.highValue;
    });
    return result;
  }

  onResetIconClick(event, isHightInput?: boolean) {
    if (isHightInput) {
      this.highValue = this.options.ceil;
      this.highValueInput = this.options.ceil;
      this.highInput.close();
      this.highInput.blur();
    } else {
      this.lowerValue = this.options.floor;
      this.lowerValueInput = this.options.floor;
      this.lowerInput.close();
      this.lowerInput.blur();
    }
    this.getInRangeValues();
    const sliderValueChangeObj: SliderValueChange = this.buildSliderValueChangeObj();
    this.sliderValueChange.emit(sliderValueChangeObj);
  }
}
