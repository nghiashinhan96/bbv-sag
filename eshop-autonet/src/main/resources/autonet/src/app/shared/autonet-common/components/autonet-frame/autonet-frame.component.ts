import { Component, OnInit, Input, ViewChild, ElementRef } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AppHelperUtil } from 'src/app/core/utils/helper.util';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'autonet-frame',
    templateUrl: './autonet-frame.component.html',
    styleUrls: ['./autonet-frame.component.scss']
})
export class AutonetFrameComponent implements OnInit {

    @Input() url: string;
    @Input() set params(p) {
        if (p) {
            const url = AppHelperUtil.objectToUrl(this.url, p);
            this.src = this.sanitizer.bypassSecurityTrustResourceUrl(url);
        }
    }
    @Input() width = '100%';
    @Input() height = 'calc(100% -36px)';
    @Input() customClass;

    src: SafeResourceUrl;
    private interval;
    private spinner;

    @ViewChild('frame', { static: true }) frame: ElementRef;
    constructor(private sanitizer: DomSanitizer) { }

    ngOnInit() {
        this.frame.nativeElement.style.height = this.height;
        this.spinner = SpinnerService.start('autonet-frame');
        this.checking();
    }

    private checking() {
        this.interval = setInterval(() => {
            const iWindow = this.frame.nativeElement;
            const iWindowContent = iWindow && iWindow.contentWindow;
            if (!!iWindowContent) {

                clearInterval(this.interval);
                SpinnerService.stop(this.spinner);

            }
        }, 1000);
    }
}
