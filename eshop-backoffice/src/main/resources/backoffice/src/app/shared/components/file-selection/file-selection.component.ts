import {
    Component,
    OnInit,
    Output,
    EventEmitter,
    Input,
    ViewChild,
    ElementRef,
} from '@angular/core';
import { NotificationModel } from '../../models/notification.model';

@Component({
    selector: 'backoffice-file-selection',
    templateUrl: './file-selection.component.html',
    styleUrls: ['./file-selection.component.scss'],
})
export class FileSelectionComponent implements OnInit {
    @Input() accepts = null;
    @Input() maxSize = -1;
    @Input() label = 'Accepted file type: .png, .jpeg, .jpg';
    @Output() fileSelectedHandler = new EventEmitter<any>();
    selectedFile: File;
    notifier: NotificationModel;
    @ViewChild('file', { static: true }) file: ElementRef;

    constructor() { }

    ngOnInit() { }

    fileChanged($event) { }

    upload(file: File) {
        this.notifier = null;
        const fileType = file.type;
        if (
            (!!this.accepts && this.accepts.indexOf(fileType) === -1) ||
            (this.maxSize !== -1 && this.selectedFile.size > this.maxSize)
        ) {
            this.fileSelectedHandler.emit(null);
            this.selectedFile = null;
            this.file.nativeElement.value = null;
            this.notifier = {
                messages: ['MESSAGES.INVALID_FILE_FORMAT'],
                status: false,
            };
            return;
        }
        this.fileSelectedHandler.emit(file);
    }
}
