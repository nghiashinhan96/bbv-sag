import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { EMPTY_STR } from 'angular-mydatepicker';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { timer } from 'rxjs';
import { take } from 'rxjs/operators';
import { OpeningDayCalendarService } from '../../services/opening-day-calendar.service';

@Component({
    selector: 'connect-import-opening-day-modal',
    templateUrl: './import-opening-day-modal.component.html',
    styleUrls: ['./import-opening-day-modal.component.scss']
})
export class ImportOpeningDayModalComponent implements OnInit {

    sourceFile: any;
    fileName: string;
    isUploading: boolean;
    isUploaded = true;
    uploadMessage: string;
    uploadPercentage = 0;
    isNotOverride = true;
    isNotImported = true;
    preFixName = 'default';
    label: string;

    constructor(
        public bsModalRef: BsModalRef,
        public translateService: TranslateService,
        public openingDayService: OpeningDayCalendarService) { }

    ngOnInit() {
        this.label = this.translateService.instant('COMMON_LABEL.NO_FILE_CHOSEN');
    }

    getFile(event) {
        this.sourceFile = event.target;
        this.fileName = this.sourceFile ? this.sourceFile.files.length > 0 ? this.sourceFile.files[0].name : EMPTY_STR
            : EMPTY_STR;
        this.isNotImported = this.sourceFile ? false : true;
    }

    overrideImport() {
        this.isUploaded = true;
        this.isUploading = false;
        this.uploadFile(true);
    }

    uploadFile(isOverridden?: boolean) {
        const files: FileList = this.sourceFile.files;
        this.isUploading = true;
        this.uploadPercentage = 0;
        this.uploadMessage = '';
        this.openingDayService.uploadOpeningDayFile(files.item(0), isOverridden)
            .subscribe(res => {
                this.uploadPercentage = 100;
                this.isUploaded = true;
                this.uploadMessage = 'WSS.UPLOAD_SUCCESSFULLY';
                this.isNotOverride = true;
                this.isNotImported = true;
                this.initiateTimer(1000);
            }, error => {
                this.isUploaded = false;
                this.uploadPercentage = 100;
                this.handleError(error);
            });
    }

    initiateTimer(duration: number) {
        return timer(duration).pipe(take(1)).subscribe(() => {
            this.bsModalRef.hide();
        });
    }

    closeUploadModal() {
        this.resetImportModal();
        this.bsModalRef.hide();
    }

    private resetImportModal() {
        this.uploadPercentage = 0;
        this.uploadMessage = EMPTY_STR;
        this.isUploading = false;
        this.isNotOverride = true;
        this.isNotImported = true;
        this.fileName = EMPTY_STR;
    }

    private handleError(error: any): void {
        let errorJson: any;
        if (!error) {
            this.uploadMessage = 'OPENING_DAY.NOT_UPLOAD_SUCCESSFULLY';
            return;
        }
        errorJson = error.error;
        switch (errorJson.error_code) {
            case 'IMPORT_DUPLICATED_OPENING_DAYS':
                this.uploadMessage = `OPENING_DAY.IMPORT_DUPLICATED_OPENING_DAYS`;
                this.isNotOverride = false;
                break;
            case 'CSV_SERVICE_ERROR':
                this.uploadMessage = 'OPENING_DAY.NOT_UPLOAD_SUCCESSFULLY';
                break;
            default:
                this.uploadMessage = 'OPENING_DAY.NOT_UPLOAD_SUCCESSFULLY';
                break;
        }
    }
}
