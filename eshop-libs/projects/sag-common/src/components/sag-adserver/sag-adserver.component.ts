import { Component, ElementRef, OnInit, AfterViewInit, Input, OnDestroy, Output, EventEmitter } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { CommonConfigService } from '../../services/common-config.service';

declare var AVP: any;
declare var _avp: any;
@Component({
    selector: 'sag-common-adserver',
    templateUrl: './sag-adserver.component.html'
})
export class SagAdserverComponent implements OnInit, AfterViewInit, OnDestroy {
    @Input() sitearea: string;
    @Input() custom1: string;
    @Input() zid: number;
    @Input() mid: number;
    @Input() custom4: string;
    @Output() adsClickedEmitter = new EventEmitter<boolean>();

    affiliate = `${this.config.affiliate}`;
    public adsData: any;
    private adServerLibCodeUrl = `${this.config.adsServer}`;
    private pid: any;
    private custom2: any;
    private contextual: any;

    constructor(
        private elementRef: ElementRef,
        private translateService: TranslateService,
        private config: CommonConfigService
    ) { }

    ngOnInit() {
        this.loadingAdServerLibCode();
        this.pid = 0;
        this.custom2 = '';
        this.contextual = true;
    }

    ngAfterViewInit() {
        this.generateScriptElement();
    }

    ngOnDestroy() {
        if (AVP) {
            AVP.viewabilityRegistry = () => {
                return;
            };
        }
    }

    fireAds(event: any) {
        const iFrame: HTMLIFrameElement = (event.target).querySelector('iframe') as HTMLIFrameElement;
        if (iFrame) {
            const adsLinkAnchor: HTMLAnchorElement = iFrame.contentWindow.document.querySelector('a');
            if (adsLinkAnchor) {
                adsLinkAnchor.click();
                this.adsClickedEmitter.emit(true);
            }
        }
    }

    generateScriptElement() {
        const url = encodeURIComponent(`${this.config.baseUrl}training/sso`);
        const avp = {
            tagid: `avp_zid_${this.zid}`,
            alias: '/',
            type: 'banner',
            zid: this.zid,
            pid: this.pid,
            custom1: this.custom1,
            custom2: this.affiliate,
            custom3: this.translateService.currentLang.toUpperCase(),
            sitearea: this.sitearea,
            contextual: this.contextual,
            custom10: url,
            custom9: this.config.appToken,
            custom4: this.custom4
        };
        if (this.mid) {
            Object.assign(avp, { mid: this.mid });
        }
        _avp.push(avp);
    }

    removeLibCodeAndService(scripts) {
        let totalScript = scripts.length;
        while (totalScript--) {
            if (scripts[totalScript]) {
                scripts[totalScript].parentNode.removeChild(scripts[totalScript]);
            }
        }
    }

    initializeLibCodeAndService() {
        const avp = document.createElement('script');
        avp.text = 'var _avp = _avp || []';
        const adServerLibCode = document.createElement('script');
        adServerLibCode.type = 'text/javascript';
        adServerLibCode.src = this.adServerLibCodeUrl;
        this.elementRef.nativeElement.appendChild(avp).appendChild(adServerLibCode);
    }

    loadingAdServerLibCode() {
        this.removeLibCodeAndService(this.elementRef.nativeElement.getElementsByTagName('script'));
        this.initializeLibCodeAndService();
    }
}
