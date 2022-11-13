import { Component, Input } from '@angular/core';
import { AdditionalInfo } from './additional-info.model';

@Component({
    selector: 'connect-additional-info',
    templateUrl: 'additional-info.component.html'
})
export class AdditionalInfoComponent {
    @Input() title: string;
    @Input() links: AdditionalInfo[] = [];

    constructor() { }
}
