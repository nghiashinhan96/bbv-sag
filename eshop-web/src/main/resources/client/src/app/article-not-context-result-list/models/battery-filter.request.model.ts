import { BatteryFilter } from './battery-filter.model';
import { Constant } from 'src/app/core/conts/app.constant';

export class BatteryFilterRequest {
    voltages: string[] = [];
    // tslint:disable-next-line: variable-name
    ampere_hours: string[] = [];
    lengths: string[] = [];
    widths: string[] = [];
    heights: string[] = [];
    interconnections: string[] = [];
    // tslint:disable-next-line: variable-name
    typeOfPoles: string[] = [];
    // tslint:disable-next-line: variable-name
    withoutStartStop: boolean;
    // tslint:disable-next-line: variable-name
    withStartStop: boolean;
    // tslint:disable-next-line: variable-name
    total_elements: number;

    constructor(data?: BatteryFilter) {
        if (data) {
            this.voltages = [...this.voltages, ...data.voltages];
            this.ampere_hours = [...this.ampere_hours, ...data.ampereHours];
            this.lengths = [...this.lengths, ...data.lengths];
            this.widths = [...this.widths, ...data.widths];
            this.heights = [...this.heights, ...data.heights];
            this.interconnections = [...this.interconnections, ...data.interconnections];
            this.typeOfPoles = [...this.typeOfPoles, ...data.typeOfPoles];
            this.withoutStartStop = data.withoutStartStop || false;
            this.withStartStop = data.withStartStop || false;
            this.total_elements = data.totalElements || 0;
        }
    }
}
