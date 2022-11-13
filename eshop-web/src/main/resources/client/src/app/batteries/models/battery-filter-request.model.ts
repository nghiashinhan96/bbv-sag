import { BatteryFilter } from './battery-filter.model';
import { Constant } from 'src/app/core/conts/app.constant';

export class BatteryFilterRequest {
    voltages: string[] = [];
    ampere_hours: string[] = [];
    lengths: string[] = [];
    widths: string[] = [];
    heights: string[] = [];
    interconnections: string[] = [];
    typeOfPoles: string[] = [];
    withoutStartStop = false;
    withStartStop = false;
    total_elements = 0;

    constructor(batteryFilter?: BatteryFilter) {
        if (!batteryFilter) {
            return;
        }
        this.voltages = batteryFilter.voltage !== Constant.SPACE ? [batteryFilter.voltage] : [];
        this.ampere_hours = batteryFilter.ampereHour !== Constant.SPACE ? batteryFilter.ampereHour.split(',') : [];
        this.lengths = batteryFilter.length !== Constant.SPACE ? batteryFilter.length.split(',') : [];
        this.widths = batteryFilter.width !== Constant.SPACE ? batteryFilter.width.split(',') : [];
        this.heights = batteryFilter.height !== Constant.SPACE ? batteryFilter.height.split(',') : [];
        this.interconnections = batteryFilter.interconnection !== Constant.SPACE ? [batteryFilter.interconnection] : [];
        this.typeOfPoles = batteryFilter.typeOfPole !== Constant.SPACE ? [batteryFilter.typeOfPole] : [];
        this.withoutStartStop = typeof (batteryFilter.withoutStartStop) === 'boolean' ? batteryFilter.withoutStartStop
            : batteryFilter.withoutStartStop === Constant.TRUE_AS_STRING;
        this.withStartStop = typeof (batteryFilter.withStartStop) === 'boolean' ? batteryFilter.withStartStop
            : batteryFilter.withStartStop === Constant.TRUE_AS_STRING;
        this.total_elements = batteryFilter.totalElements;
    }
}