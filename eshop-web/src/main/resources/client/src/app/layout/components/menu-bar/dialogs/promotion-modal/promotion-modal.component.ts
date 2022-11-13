import { Component, OnInit, Input } from '@angular/core';
import { APP_LANG_CODE_FR, APP_LANG_CODE_IT } from 'src/app/core/conts/app-lang-code.constant';
import { TranslateService } from '@ngx-translate/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { InfoLink } from '../../info-link/info-link.model';

@Component({
    selector: 'connect-promotion-modal',
    templateUrl: './promotion-modal.component.html',
    styleUrls: ['./promotion-modal.component.scss']
})
export class PromotionModalComponent implements OnInit {

    @Input() isDerendChCustomer: boolean;
    @Input() isTechnoChCustomer: boolean;
    @Input() isRbeCustomer: boolean;
    @Input() isWbbCustomer: boolean;
    @Input() isAtCustomer: boolean;
    @Input() isDerendAtCustomer: boolean;
    @Input() isMatikChCustomer: boolean;
    @Input() mediaDirectory: string;
    @Input() isMatikAtCustomer: boolean;
    @Input() isCz: boolean;

    timestamp = new Date().getTime();
    links: InfoLink[] = [];

    constructor(
        public bsModalRef: BsModalRef,
        private translation: TranslateService
    ) { }

    ngOnInit() {
        this.links = this.buildLinks();
    }

    private buildLinks(): InfoLink[] {
        const targetMode = '_blank';
        if (this.isDerendChCustomer) {
            this.buildMediaLink('/KS-Tools_LL18-03D_CHF.pdf');
            return [
                new InfoLink('PROMOTIONS_DATA.DD_LINK_1',
                    this.buildMediaLink('/D-Store_Aktionen_DE.pdf', false), targetMode),
                new InfoLink('PROMOTIONS_DATA.DD_LINK_1_TYRES',
                    'https://s3.exellio.de/connect_media/adverts/Aktion-DD.pdf', targetMode),
                new InfoLink('PROMOTIONS_DATA.DD_LINK_2',
                    this.buildMediaLink('/D-Store_Aktionen_FR.pdf', false), targetMode),
                new InfoLink('PROMOTIONS_DATA.DD_LINK_2_TYRES',
                    'https://s3.exellio.de/connect_media/adverts/Aktion-DD.pdf', targetMode),
                new InfoLink('PROMOTIONS_DATA.DD_LINK_3',
                    this.buildMediaLink('/D-Store_Aktionen_IT.pdf', false), targetMode),
                new InfoLink('PROMOTIONS_DATA.DD_LINK_3_TYRES',
                    'https://s3.exellio.de/connect_media/adverts/Aktion-DD.pdf', targetMode)
            ];
        }
        if (this.isTechnoChCustomer) {
            this.buildMediaLink('/KS-Tools_LL18-03D_CHF.pdf');
            return [
                new InfoLink('PROMOTIONS_DATA.TM_LINK_1',
                    this.buildMediaLink('/TM_Aktionen eShop_04-2018_d_72dpi.pdf', false), targetMode),
                new InfoLink('PROMOTIONS_DATA.TM_LINK_1_TYRES',
                    'https://s3.exellio.de/connect_media/adverts/Aktion-TM.pdf', targetMode),
                new InfoLink('PROMOTIONS_DATA.TM_LINK_2',
                    this.buildMediaLink('/TM_Aktionen eShop_04-2018_f_72dpi.pdf', false), targetMode),
                new InfoLink('PROMOTIONS_DATA.TM_LINK_2_TYRES',
                    'https://s3.exellio.de/connect_media/adverts/Aktion-TM.pdf', targetMode),
                new InfoLink('PROMOTIONS_DATA.TM_LINK_3',
                    this.buildMediaLink('/TM_Aktionen eShop_04-2018_f_72dpi.pdf', false), targetMode),
                new InfoLink('PROMOTIONS_DATA.TM_LINK_3_TYRES',
                    'https://s3.exellio.de/connect_media/adverts/Aktion-TM.pdf', targetMode)
            ];
        }
        if (this.isRbeCustomer) {
            return [
                new InfoLink('PROMOTIONS_DATA.RBE_LINK_1',
                    this.buildMediaLink('/PROMO_lumag___NOV_-_DEC_17.pdf', false), targetMode)
            ];
        }
        if (this.isDerendAtCustomer) {
            return [
                new InfoLink('PROMOTIONS_DATA.AT_LINK_1',
                    this.buildMediaLink('/focus_aktuell.pdf', true), targetMode),
                new InfoLink('PROMOTIONS_DATA.AT_LINK_2',
                    this.buildMediaLink('/DD-AT-MonatsAktionen.PDF', true), targetMode)
            ];
        }
        if (this.isMatikAtCustomer) {
            return [
                new InfoLink('PROMOTIONS_DATA.AT_LINK_2',
                    this.buildMediaLink('/DD-AT-MonatsAktionen.PDF', true), targetMode),
                new InfoLink('Saisonale Angebote',
                    this.buildMediaLink('/MATIK-Saisonale.pdf', true), targetMode)
            ];
        }
        if (this.isWbbCustomer) {
            return [
                new InfoLink('PROMOTIONS_DATA.DD_LINK_1',
                    this.buildMediaLink('/KS-Tools_LL18-03D_CHF.pdf'), targetMode),
                new InfoLink('PROMOTIONS_DATA.DD_LINK_2',
                    this.buildMediaLink('/KS-Tools_LL18-03FR_CHF.pdf'), targetMode),
                new InfoLink('PROMOTIONS_DATA.DD_LINK_3',
                    this.buildMediaLink('/KS-Tools_LL18-03FR_CHF.pdf'), targetMode)
            ];
        }
        if (this.isMatikChCustomer) {
            return [
                new InfoLink('PROMOTIONS_DATA.MT_LINK_1',
                    this.buildMchPromotionLink(this.translation.currentLang), targetMode)
            ];
        }
        if (this.isCz) {
            return [
                new InfoLink('PROMOTIONS_DATA.CZ_LINK_1',
                    this.buildMediaLink('/stahlgruber_hity-2020-2021.pdf'), targetMode),
                new InfoLink('PROMOTIONS_DATA.CZ_LINK_2',
                    this.buildMediaLink('/Akce Osram.pdf'), targetMode)
            ];
        }
        return [];
    }

    private buildMchPromotionLink(userLanguage: string): string {
        switch (userLanguage) {
            case APP_LANG_CODE_FR:
            case APP_LANG_CODE_IT:
                return this.buildMediaLink(`/Matik_Aktuell_FR.pdf`, true);
            default:
                return this.buildMediaLink(`/Matik_Aktuell_DE.pdf`, true);
        }
    }

    private buildMediaLink(path: string, appendTimestamp?: boolean): string {
        if (appendTimestamp) {
            return `${this.mediaDirectory}${path}?ver=${this.timestamp}`;
        }
        return `${this.mediaDirectory}${path}`;
    }
}
