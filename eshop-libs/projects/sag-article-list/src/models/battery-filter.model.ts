export class BatteryFilter {
    voltages: string[] = [];
    ampereHours: string[] = [];
    lengths: string[] = [];
    widths: string[] = [];
    heights: string[] = [];
    interconnections: string[] = [];
    typeOfPoles: string[] = [];
    withoutStartStop: boolean;
    withStartStop: boolean;
    totalElements: number;

    constructor(data?: any) {
        if (data) {
            this.voltages = data.voltage ? [...this.voltages, data.voltage] : [];
            this.ampereHours = data.ampereHour ? [...this.ampereHours, data.ampereHour] : [];
            this.lengths = data.length ? [...this.lengths, data.length] : [];
            this.widths = data.width ? [...this.widths, data.width] : [];
            this.heights = data.height ? [...this.heights, data.height] : [];
            this.interconnections = data.interconnection ? [...this.interconnections, data.interconnection] : [];
            this.typeOfPoles = data.typeOfPole ? [...this.typeOfPoles, data.typeOfPole] : [];
            this.withoutStartStop = data.withoutStartStop || false;
            this.withStartStop = data.withStartStop || false;
            this.totalElements = data.totalElements || 0;
        }
    }

    setAdditionalFields(isChecked: boolean, fieldName: string, fieldValue: string) {
        if (fieldName === 'voltages') {
            this.voltages = isChecked ? [...this.voltages, fieldValue] : this.voltages.filter(vol => vol !== fieldValue);
        } else if (fieldName === 'ampere_hours') {
            this.ampereHours = isChecked ? [...this.ampereHours, fieldValue] : this.ampereHours.filter(h => h !== fieldValue);
        } else if (fieldName === 'interconnections') {
            this.interconnections = isChecked ?
                [...this.interconnections, fieldValue] :
                this.interconnections.filter(inter => inter !== fieldValue);
        } else if (fieldName === 'type_of_poles') {
            this.typeOfPoles = isChecked ? [...this.typeOfPoles, fieldValue] : this.typeOfPoles.filter(pole => pole !== fieldValue);
        }
    }
}
