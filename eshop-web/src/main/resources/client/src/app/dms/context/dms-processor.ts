import { ExportOrder } from '../models/export-order.model';
import { DmsService } from '../services/dms.service';

export abstract class DmsProcessor extends DmsService {

    abstract export(exportOrder: ExportOrder): any;

    abstract getReturnBaksetErrorMessage(): string;
}
