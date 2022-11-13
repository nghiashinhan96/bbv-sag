import { Component, OnInit, Input } from '@angular/core';
import { SafeResourceUrl, DomSanitizer } from '@angular/platform-browser';

@Component({
    selector: 'autonet-advertise',
    templateUrl: './advertise.component.html',
    styleUrls: ['./advertise.component.scss']
})
export class AdvertiseComponent implements OnInit {

    @Input() customClass: string;
    @Input() url = '';
    @Input() params: any = {};
    src: SafeResourceUrl;
    constructor(private sanitizer: DomSanitizer) { }

    ngOnInit() {
        const query = Object.keys(this.params).reduce((queryString: string, k: string) => {
            if (!!this.params[k]) {
                const val = `${k}=${encodeURIComponent(this.params[k])}`;
                if (!queryString) {
                    return val;
                }
                return `${queryString}&${val}`;
            }
            return queryString;
        }, '');
        this.src = this.sanitizer.bypassSecurityTrustResourceUrl(`${this.url}?${query}`);
    }

}
