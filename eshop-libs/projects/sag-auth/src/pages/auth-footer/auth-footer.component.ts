import { Component, OnInit, Input, TemplateRef } from '@angular/core';

@Component({
    selector: 'sag-auth-footer',
    templateUrl: './auth-footer.component.html',
    styleUrls: ['./auth-footer.component.scss']
})
export class SagAuthFooterComponent implements OnInit {
    @Input() footerTemplateRef: TemplateRef<any>;

    constructor() { }

    ngOnInit() {
    }

}
