import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { EMPTY_STR } from 'angular-mydatepicker';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { timer,Observable } from 'rxjs';
import { take } from 'rxjs/operators';

@Component({
  selector: 'connect-margin-import-modal',
  templateUrl: './margin-import-modal.component.html',
  styleUrls: ['./margin-import-modal.component.scss']
})
export class MarginImportModalComponent implements OnInit {

    @Input() importObserver: (data) => Observable<any>;

    sourceFile: any;
    fileName: string;
    isUploading: boolean;
    isUploaded = true;
    uploadMessage: string;
    uploadPercentage = 0;
    isNotImported = true;
    preFixName = 'default';
    label: string;

    title = "WSS.IMPORT_EXPORT";
    isShowExport = true;
    isShowDownloadTemplate = true;
    onExport: () => void | Promise<any>;
    onDownloadTemplate: () => void | Promise<any>;
    onUploadComplete: () => void | Promise<any>;

    constructor(
        public bsModalRef: BsModalRef,
        public translateService: TranslateService) { }

    ngOnInit() {
        this.label = this.translateService.instant('COMMON_LABEL.NO_FILE_CHOSEN');
    }

    getFile(event) {
        this.sourceFile = event.target;
        this.fileName = this.sourceFile ? this.sourceFile.files.length > 0 ? this.sourceFile.files[0].name : EMPTY_STR
            : EMPTY_STR;
        this.isNotImported = this.sourceFile ? false : true;
    }

    exportData() {
        if (this.onExport) {
            this.onExport();
        }
    }

    uploadFile(isOverridden?: boolean) {
        if (!this.importObserver) {
            return;
        }
        const files: FileList = this.sourceFile.files;
        this.isUploading = true;
        this.uploadPercentage = 0;
        this.uploadMessage = '';
        this.importObserver(files.item(0))
            .subscribe(res => {
                this.uploadPercentage = 100;
                this.isUploaded = true;
                this.uploadMessage = 'WSS.UPLOAD_SUCCESSFULLY';
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
            if (this.onUploadComplete) {
                this.onUploadComplete();
            }
        });
    }

    closeUploadModal() {
        this.resetImportModal();
        this.bsModalRef.hide();
    }

    downloadTemplate() {
        if (this.onDownloadTemplate) {
            this.onDownloadTemplate();
        }
    }

    private resetImportModal() {
        this.uploadPercentage = 0;
        this.uploadMessage = EMPTY_STR;
        this.isUploading = false;
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
