import { Constant } from 'src/app/core/conts/app.constant';

export class BatteryFilter {
  voltage: string = Constant.VOLTAGE_DEFAULT;
  ampereHour: string = Constant.SPACE;
  length: string = Constant.SPACE;
  width: string = Constant.SPACE;
  height: string = Constant.SPACE;
  interconnection: string = Constant.SPACE;
  typeOfPole: string = Constant.SPACE;
  withoutStartStop = false;
  withStartStop = false;
  totalElements = 0;
  isRangeAmpereHourCb = false;
  isRangeLengthCb = false;
  isRangeWidthCb = false;
  isRangeHeightCb = false;
  isFilterChanged = false;
  isToggleSliderOn = false;
  lastUpdatedField = '';

  constructor(voltage = Constant.VOLTAGE_DEFAULT) {
    this.voltage = voltage;
  }
}
