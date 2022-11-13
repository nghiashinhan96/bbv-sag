import { Component, OnInit, Input } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { TranslateService } from '@ngx-translate/core';
import { VideoPlayerComponent } from '../video-player/video-player.component';
import { InfoLink } from '../../info-link/info-link.model';

@Component({
    selector: 'connect-shop-info-modal',
    templateUrl: './shop-info-modal.component.html',
    styleUrls: ['./shop-info-modal.component.scss']
})
export class ShopInfoModalComponent implements OnInit {

    private readonly THULE_LABEL = 'Kaufhilfe Thule';

    @Input() isDerendChCustomer: boolean;
    @Input() isTechnoChCustomer: boolean;
    @Input() isRbeCustomer: boolean;
    @Input() isWbbCustomer: boolean;
    @Input() isAtCustomer: boolean;
    @Input() isDerendAtCustomer: boolean;
    @Input() isMatikAtCustomer: boolean;
    @Input() isMatikChCustomer: boolean;
    @Input() mediaDirectory: string;
    @Input() thuleExternalUrl: string;

    timestamp = new Date().getTime();

    ddatInfoLinks: InfoLink[] = [];
    matInfoLinks: InfoLink[] = [];
    thuleInfoLink: InfoLink;

    remcoInfoLink1: string;
    remcoInfoLink2: string;
    remcoInfoLink3: string;

    private videoUrl = '';
    constructor(
        public bsModalRef: BsModalRef,
        private translation: TranslateService,
        private modalService: BsModalService
    ) { }

    ngOnInit() {
        const currentLang = this.translation.currentLang;

        this.setInformationLinkWithLang(currentLang);

        this.videoUrl = `https://s3.exellio.de/connect_media/common/videos/ferodo/Ferodo-Directional-Brakepads_${currentLang}.mp4`;

        const automatikGetLink = new InfoLink('Automatik-Getriebe-Spülgerät Dynco',
            'https://www.motorex-dynco.com/automatik-getriebe-spuelgeraet.html', '_blank');
        const hellaCatalogLink = new InfoLink('Hella Kataloge', 'https://www.hella.com/hella-at/Kataloge-1390.html#', '_blank');
        const motorexLink = new InfoLink('Motorex', 'https://www.motorex.com/de-ch/', '_blank');
        const motorexOilLink = new InfoLink('Motorex OIL FINDER', 'https://www.motorex.com/de-ch/verkauf-beratung/oil-finder/', '_blank');
        const toolIsLink = new InfoLink('Toolis SW-Stahl', 'https://www.tool-is.com/catalog/', '_blank');
        const toolNewsLink = new InfoLink('TOOLNEWS SW-Stahl', `${this.mediaDirectory}/SW-Stahl-ToolNews.pdf`, '_blank');
        const betaToolsLink = new InfoLink('Werkzeug-Katalog Beta', 'https://www.beta-tools.com/', '_blank');
        const workshopEquipmentLink = new InfoLink('Werkstattausrüstung', 'http://siems-klein.at/de/', '_blank');
        const thuleLink = new InfoLink(this.THULE_LABEL, 'https://www.thule.com/de-de/buyers-guide', '_blank');
        const osramFinderLink = new InfoLink('Osram Fahrzeuglampen Finder', 'https://www.osram.de/apps/gvlrg/de_DE', '_blank');
        const haynesProSchulungsvideoLink = new InfoLink('Haynes Pro Schulungsvideo', 'https://www.youtube.com/watch?v=SfK5T_FxUJE&t=1s', '_blank');
        const hellaGutmannLink = new InfoLink('Hella-Gutmann', 'https://www.hella-gutmann.com/de/for-workshops/service-loesungen/hgs-data', '_blank');
        const hgsMacsdiaLink = new InfoLink('HGS macsDIA', 'https://www.hella-gutmann.com/de/for-workshops/service-loesungen/macsdia', '_blank');
        this.ddatInfoLinks = [
            hellaCatalogLink,
            motorexLink,
            motorexOilLink,
            automatikGetLink,
            toolIsLink,
            toolNewsLink,
            betaToolsLink,
            workshopEquipmentLink,
            new InfoLink('VARTA Business Portal', 'https://www.varta-automotive.de/de-de/business-portal/anmeldung', '_blank'),
            haynesProSchulungsvideoLink,
            thuleLink,
            new InfoLink(
                'Brink AHV',
                'https://brink.eu/de-de/anhaengerkupplungen/typ-anhaengerkupplungen/abnehmbare-anhaengerkupplung/',
                '_blank'),
            osramFinderLink,
            new InfoLink('Technische Informationen für Ferodo Bremsteile', 'https://www.ferodo.de/support.html', '_blank'),
            new InfoLink('Exide Technologies', 'https://www.exide.com/', '_blank'),
            new InfoLink('Digitales Serviceheft',
                'https://www.derendinger.at/de/dienstleistungen/digitales-serviceheft.html',
                '_blank'),
            hellaGutmannLink,
            hgsMacsdiaLink,
            new InfoLink('ALLES RUND UMS RAD', 'https://www.yumpu.com/de/document/read/55601064/alles-rund-ums-rad', '_blank'),
            new InfoLink('Verbrauchsmaterial',
                'https://www.yumpu.com/de/document/read/56539798/verbrauchsmaterialienkatalog',
                '_blank'),
            new InfoLink('Werkstatteinrichtungen & Werkzeuge',
                'https://www.derendinger.at/de/dienstleistungen/werkstatteinrichtung-and-werkzeug.html', '_blank'),
        ];
        this.matInfoLinks = [
            hellaCatalogLink,
            motorexLink,
            motorexOilLink,
            automatikGetLink,
            toolIsLink,
            toolNewsLink,
            betaToolsLink,
            workshopEquipmentLink,
            haynesProSchulungsvideoLink,
            thuleLink,
            osramFinderLink,
            hellaGutmannLink,
            hgsMacsdiaLink
        ];
        this.setupThuleDealerUrl();
    }

    openVideo() {
        this.modalService.show(VideoPlayerComponent, {
            ignoreBackdropClick: true,
            initialState: {
                url: this.videoUrl,
                videoTitle: 'INFORMATIONS_DATA_IT.FERODO_VIDEO'
            },
            backdrop: true,
            class: 'modal-lg'
        });
        this.bsModalRef.hide();
    }

    private setInformationLinkWithLang(lang) {
        switch (lang) {
            case 'de':
                this.setInformationLink('https://www.bpw.de/mybpw/login.php?l=de',
                    'https://portal.saf-axles.com/index-de.html',
                    'https://www.aspoeck.com/de');
                break;
            case 'fr':
                this.setInformationLink('https://www.bpw.de/mybpw/login.php?l=fr',
                    'https://portal.saf-axles.com/index-fr.html',
                    'https://www.aspoeck.com/en');
                break;
            case 'nl':
                this.setInformationLink('https://www.bpw.de/en/service/after-sales-spare-parts/spare-parts-lists',
                    'https://portal.saf-axles.com/index-en.html',
                    'https://www.aspoeck.com/en');
                break;
        }
    }
    private setInformationLink(...links) {
        this.remcoInfoLink1 = links[0];
        this.remcoInfoLink2 = links[1];
        this.remcoInfoLink3 = links[2];
    }

    private setupThuleDealerUrl() {
        if (!this.thuleExternalUrl) {
            return;
        }
        const thuleDealerUrl = this.thuleExternalUrl;
        this.thuleInfoLink = new InfoLink('INFORMATIONS_DATA.THULE_LINK', thuleDealerUrl, '_blank');

        const updatedDdatInfoLinks = this.replaceThuleDealerInfoLinkForExistingInfoLinks(
            this.ddatInfoLinks, this.thuleInfoLink);
        if (updatedDdatInfoLinks) {
            this.ddatInfoLinks = updatedDdatInfoLinks;
        }
        const updatedMatInfoLinks = this.replaceThuleDealerInfoLinkForExistingInfoLinks(
            this.matInfoLinks, this.thuleInfoLink);
        if (updatedMatInfoLinks) {
            this.matInfoLinks = updatedMatInfoLinks;
        }
    }

    private replaceThuleDealerInfoLinkForExistingInfoLinks(infoLinks: InfoLink[] = [], replaceInfoLink: InfoLink) {
        if (!infoLinks) {
            return null;
        }
        const foundIdx = infoLinks.findIndex((infoLnk, idx) => infoLnk.label === this.THULE_LABEL);
        if (foundIdx < 0) {
            return null;
        }
        infoLinks[foundIdx] = replaceInfoLink;
        return infoLinks;
    }
}
