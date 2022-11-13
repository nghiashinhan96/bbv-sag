import { Input, Component, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';

@Component({
    selector: 'sag-common-save-button',
    templateUrl: 'save-button.component.html',
    styleUrls: ['save-button.component.scss']
})
export class SagSaveButtonComponent {
    @Input() loading = false;
    @Input() disabled = false;
    @Input() text = 'COMMON_LABEL.SAVE';
    @Input() icon: string;
    @Input() params = {};
    @Input() css = 'btn-primary';
    @Input() btnType = 'button';
    @Input() hideTextOnLoading = false;
    @Output() save = new EventEmitter();
    @Input() name: string;
    constructor(private cd: ChangeDetectorRef) { }

    onSave() {
        this.loading = true;
        this.save.emit(() => {
            setTimeout(() => {
                this.loading = false;
                this.cd.detectChanges();
            });
        });
    }
}
