import { BatteryFilter } from './battery-filter.model';

export class BatteryFilterRequest {
    voltages: string[] = [];
    // tslint:disable-next-line: variable-name
    ampere_hours: string[] = [];
    lengths: string[] = [];
    widths: string[] = [];
    heights: string[] = [];
    interconnections: string[] = [];
    // tslint:disable-next-line: variable-name
    type_of_poles: string[] = [];
    // tslint:disable-next-line: variable-name
    without_start_stop: boolean;
    // tslint:disable-next-line: variable-name
    with_start_stop: boolean;
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
            this.type_of_poles = [...this.type_of_poles, ...data.typeOfPoles];
            this.without_start_stop = data.withoutStartStop || false;
            this.with_start_stop = data.withStartStop || false;
            this.total_elements = data.totalElements || 0;
        }
    }
}