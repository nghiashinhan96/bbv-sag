import { Injectable } from '@angular/core';
import { DmsProcessor } from './dms-processor';
import { DmsService } from '../services/dms.service';
import { ExportOrder } from '../models/export-order.model';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class MetBrowserProcessor extends DmsService implements DmsProcessor {

    public export(exportOrder: ExportOrder): Promise<any> {
        return this.prepareExportData(exportOrder).pipe(
            map((() => {
                this.removeDmsInfo();
                this.download();
                return '';
            })),
            catchError(() => {
                return of(this.getReturnBaksetErrorMessage());
            })
        ).toPromise();
    }

    public getReturnBaksetErrorMessage() {
        return 'DMS.ERROR.EXPORT_FAILED';
    }

}
