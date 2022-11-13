import { Component } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { ArticleImportType } from '../../enums/article-import-type.enum';

@Component({
    selector: 'connect-import-articles-modal',
    templateUrl: 'import-articles-modal.component.html'
})
export class ImportArticlesModalComponent {
    label = '';
    fileName = '';
    fileType: ArticleImportType;

    errorMessage = '';
    onImported: any;
    isValidFile: boolean;
    isClearInput: boolean;
    fileUpload: File;

    constructor(
        private bsModalRef: BsModalRef
    ) { }

    close() {
        this.bsModalRef.hide();
    }

    checkFileType(event: any) {
        if (!event.target.files[0]) {
            return;
        }
        this.fileUpload = event.target.files[0];
        this.fileName = this.fileUpload.name;
        const fileNameInLowerCase = this.fileName.toLowerCase();
        const csvFileRegex = /(.csv)$/;
        const excelFileRegex = /(.xls|.xlsx)$/;
        const txtFileRegex = /(.txt)$/;

        if (csvFileRegex.test(fileNameInLowerCase)) {
            this.fileType = ArticleImportType.CSV;
            this.isValidFile = true;
        } else if (excelFileRegex.test(fileNameInLowerCase)) {
            this.fileType = ArticleImportType.EXCEL;
            this.isValidFile = true;
        } else if (txtFileRegex.test(fileNameInLowerCase)) {
            this.fileType = ArticleImportType.TXT;
            this.isValidFile = true;
        }
        else {
            this.fileType = null;
            this.isValidFile = false;
        }
        this.isClearInput = false;
    }

    import() {
        if (this.fileUpload) {
            this.onImported(this.fileUpload, this.fileType);
        }
        this.bsModalRef.hide();
    }
}
