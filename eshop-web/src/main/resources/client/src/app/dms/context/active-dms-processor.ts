import { Injectable } from '@angular/core';
import { ExportOrder } from '../models/export-order.model';
import { DmsProcessor } from './dms-processor';

export abstract class ActiveDmsProcessor extends DmsProcessor {

}
