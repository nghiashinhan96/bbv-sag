import { LabourTimeHelper } from './labour-time-helper';
import { Component, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { LabourTimeService } from '../../services/labour-time.service';
import { SagCurrencyPipe } from 'sag-currency';

@Component({
    selector: 'sag-haynespro-labour-time',
    templateUrl: './labour-time.component.html'
})
export class SagLabourTimeComponent implements OnChanges {
    @Input() labourModel: any;
    @Input() currentStateVatConfirm;
    @Output() deleteLabourTime = new EventEmitter<any>();

    constructor(
        private labourService: LabourTimeService,
        private currency: SagCurrencyPipe
    ) { }

    ngOnChanges() {
        if (this.labourModel) {
            this.labourModel.forEach(labour => {
                if (this.currentStateVatConfirm) {
                    labour.labourRateWithTime = this.currency.transform(
                        LabourTimeHelper.getLabourRateWithTime(labour.time, labour.labourRateWithVat));
                } else {
                    labour.labourRateWithTime = this.currency.transform(
                        LabourTimeHelper.getLabourRateWithTime(labour.time, labour.labourRate));
                }
            });
        }
    }

    deleteLabourTimeRecord(index, item: any) {
        this.labourModel.splice(index, 1);
        this.labourService.deleteLabourRecord(this.labourModel.vehicleId, item.awNumber).subscribe(res => {
            this.deleteLabourTime.emit(this.labourModel);
        }, () => {
            console.log('error delete labour time record');
        });
    }
}
