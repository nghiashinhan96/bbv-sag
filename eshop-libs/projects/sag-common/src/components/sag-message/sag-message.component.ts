import { Component, OnInit, Input } from '@angular/core';
import { SagMessageData } from '../../models/sag-common-message.data';

@Component({
    selector: 'sag-common-message',
    templateUrl: './sag-message.component.html'
})
export class SagMessageComponent implements OnInit {

    @Input() set message(val: string | string[]) {
        if (Array.isArray(val)) {
            this.content = val;
        } else {
            this.content = [val];
        }
    }
    @Input() type: 'ERROR' | 'WARNING' | 'SUCCESS' | 'INFO' = 'WARNING';
    @Input() params: any = {};
    @Input() icon: string;
    @Input() customClass: string = '';
    @Input() size2x: boolean = true;
    @Input() set data(val: SagMessageData) {
        if (val) {
            this.params = val.params;
            this.type = val.type;
            this.icon = val.icon;
            if (Array.isArray(val.message)) {
                this.content = val.message;
            } else {
                this.content = [val.message];
            }
        }
    }
    content = [] as string[];
    constructor() { }

    ngOnInit() {
    }

}
