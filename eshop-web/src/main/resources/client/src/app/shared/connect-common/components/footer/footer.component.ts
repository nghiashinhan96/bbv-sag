import { Component, OnDestroy, OnInit } from '@angular/core';
import { AffiliateEnum } from 'sag-common';
import { environment } from 'src/environments/environment';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { BUILD_ENVIRONMENT } from 'src/app/core/conts/build-env.constant';
import { TranslateService } from '@ngx-translate/core';
import { SubSink } from 'subsink';

@Component({
    selector: 'connect-footer',
    templateUrl: './footer.component.html',
    styleUrls: ['./footer.component.scss']
})
export class FooterComponent implements OnInit, OnDestroy {
    buildEnv: string;
    info: any;
    cz = AffiliateEnum.CZ;
    sb = AffiliateEnum.SB;
    ehcz = AffiliateEnum.EH_CZ;
    ehaxcz = AffiliateEnum.EH_AX_CZ;
    private subs = new SubSink();

    constructor(
        public appStorage: AppStorageService,
        private translate: TranslateService
    ) { }

    ngOnInit() {
        this.info = this.buildFooterInfo();
        this.subs.sink = this.translate.onLangChange.subscribe(langs => {
            this.info = this.buildFooterInfo();
        });
        this.buildEnv = this.getEnvDescription();
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    private getEnvDescription(): string {
        return BUILD_ENVIRONMENT[environment.env];
    }

    buildFooterInfo() {
        const czUrls = {
            terms: 'FOOTER.TERM_OF_SERVICE.LINK.CZ',
            privacy: 'https://s3.exellio.de/connect_media/stahlgruber-cz/pdfs/GDPR.pdf'
        };
        const data = [
            {
                affiliate: AffiliateEnum.TECHNO_CH,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.TECHNOMAG',
                    privacy: `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs/technomag-datenschutz.pdf`,
                    disclaimer: `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs/technomag-haftungsausschluss.pdf`
                },
                copy: 'FOOTER.COMPANY_INFO_TECHNOMAG'
            },
            {
                affiliate: AffiliateEnum.DEREND_CH,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.DERENDINGER_CH',
                    privacy: 'http://www.derendinger.ch/de/footer-menu/datenschutz.html',
                    disclaimer: 'http://www.derendinger.ch/_Resources/Persistent/'
                        + '0ebfe4927db8a3fa6ec40860c1c5477c5c17465f/haftungsausschluss.pdf.pdf'
                },
                copy: 'FOOTER.COMPANY_INFO_DERENDINGER'
            },
            {
                affiliate: AffiliateEnum.DEREND_AT,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.DERENDINGER_AT',
                    privacy: `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs/derendinger-at-datenschutz.pdf`,
                    disclaimer: 'https://www.wkoecg.at/Web/Ecg.aspx'
                        + '?FirmaID=13e87f4f-4d8d-4feb-a2ea-e1e71994e11d&AspxAutoDetectCookieSupport=1'
                },
                copy: 'FOOTER.COMPANY_INFO_DERENDINGER_AT'
            },
            {
                affiliate: AffiliateEnum.MATIK_AT,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.MATIK_AT',
                    privacy: `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs/Matik_Datenschutz.pdf`,
                    disclaimer: `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs/Impressum.pdf`
                },
                copy: 'FOOTER.COMPANY_INFO_MATIK'
            },
            {
                affiliate: AffiliateEnum.MATIK_CH,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.MATIK_CH',
                    privacy: `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs/Matik_Datenschutz.pdf`,
                    disclaimer: `https://s3.exellio.de/connect_media/${environment.affiliate}/pdfs/Impressum.pdf`
                },
                copy: 'FOOTER.COMPANY_INFO_MCH'
            },
            {
                affiliate: AffiliateEnum.WBB,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.WBB',
                    privacy: 'http://www.waelchli-bollier.ch/',
                    disclaimer: 'http://www.waelchli-bollier.ch/de/footer-menu/disclaimer.html'
                },
                copy: 'FOOTER.COMPANY_INFO_WBB'
            },
            {
                affiliate: AffiliateEnum.RBE,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.REMCO',
                    privacy: 'https://shop.remco.be/javax.faces.resource/haftungsausschluss.pdf.xhtml?ln=documents/rbe/pdfs/nl',
                    disclaimer: 'https://shop.remco.be/javax.faces.resource/haftungsausschluss.pdf.xhtml?ln=documents/rbe/pdfs/nl'
                },
                copy: 'FOOTER.COMPANY_INFO_RBE'
            },
            {
                affiliate: AffiliateEnum.EH_CH,
                urls: {
                    terms: 'FOOTER.TERM_OF_SERVICE.LINK.EH_CH',
                    privacy: this.translate.instant('FOOTER.DATA_PROTECTION.LINK.EH_CH'),
                    disclaimer: this.translate.instant('FOOTER.DISCLAIMER.LINK.EH_CH')
                }
            },
            {
                affiliate: AffiliateEnum.CZ,
                urls: czUrls
            },
            {
                affiliate: AffiliateEnum.EH_CZ,
                urls: czUrls
            },
            {
                affiliate: AffiliateEnum.AXCZ,
                urls: czUrls
            },
            {
                affiliate: AffiliateEnum.EH_AX_CZ,
                urls: czUrls
            }
        ];

        return data.find((item) => item.affiliate === environment.affiliate);
    }
}
