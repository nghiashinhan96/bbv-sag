import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { LocalStorage } from 'ngx-webstorage';
import { UserService } from '../core/services/user.service';
import { UserDetail } from '../core/models/user-detail.model';
import { Constant } from '../core/conts/app.constant';
import { TabsetComponent } from 'ngx-bootstrap/tabs';
import { AnalyticLoggingService } from '../analytic-logging/services/analytic-logging.service';
import { AnalyticEventType } from '../analytic-logging/enums/event-type.enums';
import { AdditionalInfo } from '../shared/connect-common/components/additional-info/additional-info.model';
import { AppStorageService } from '../core/services/app-storage.service';
import { ADS_TARGET_NAME } from 'sag-common';

@Component({
    selector: 'connect-tyre',
    templateUrl: './tyres.component.html',
    styleUrls: ['./tyres.component.scss']
})
export class TyresComponent implements OnInit, AfterViewInit {
    user: UserDetail;

    tyresIsUpdated = false;

    showSystemMessage = false;
    readonly adsTargetName = ADS_TARGET_NAME.TYRE;
    additionalInfo = this.getAdditionalInfo();

    @ViewChild('tyresSearch', { static: false }) tyresSearch: TabsetComponent;

    MOTOR_TYRE = Constant.MOTOR_TYRE;
    PKW_TYRE = Constant.PKW_TYRE;
    constructor(
        private translateService: TranslateService,
        private analyticService: AnalyticLoggingService,
        private userService: UserService,
        private appStorage: AppStorageService
    ) { }

    ngOnInit() {
        this.user = this.userService.userDetail;
        this.showSystemMessage = this.user && !(this.user.salesUser || this.user.isSalesOnBeHalf);
    }

    keepSelectedTab(mode) {
        this.appStorage.tyreSearchHistoryMode = {
            key: this.user.custNr,
            value: mode
        };
    }

    ngAfterViewInit() {
        if (this.user && this.user.custNr) {
            const tyreFilterMode = this.appStorage.tyreSearchHistoryMode;
            const mode = tyreFilterMode && tyreFilterMode[this.user.custNr] || Constant.PKW_TYRE;
            this.tyresSearch.tabs.forEach(tab => {
                if (mode === Constant.MOTOR_TYRE) {
                    tab.active = true;
                }
            });
        }
    }

    sendAdsClick() {
        const activeTab = this.tyresSearch.tabs.filter((tab) => tab.active);
        const selectedTab = activeTab.length ? activeTab[0].heading : '';
        this.analyticService.sendAdsEvent(AnalyticEventType.TYRE_EVENT, selectedTab);
    }

    private getAdditionalInfo(): AdditionalInfo[] {
        const lang = this.translateService.currentLang;
        return [
            {
                url: `https://s3.exellio.de/connect_media/common/pdfs/tyres/${lang}/re_01.pdf`,
                text: 'SEARCH.SEARCH_TYRES.TYRE_TECHNICAL_TERMS'
            },
            {
                url: `https://s3.exellio.de/connect_media/common/pdfs/tyres/${lang}/re_02.pdf`,
                text: 'SEARCH.SEARCH_TYRES.SPEED_CATEGORIES'
            },
            {
                url: `https://s3.exellio.de/connect_media/common/pdfs/tyres/${lang}/re_04.pdf`,
                text: 'SEARCH.SEARCH_TYRES.MATURITY_INDEX'
            },
            {
                url: `https://s3.exellio.de/connect_media/common/pdfs/tyres/${lang}/re_05.pdf`,
                text: 'SEARCH.SEARCH_TYRES.COMPARISON_TATBLE'
            },
            {
                url: `https://s3.exellio.de/connect_media/common/pdfs/tyres/${lang}/re_06.pdf`,
                text: 'SEARCH.SEARCH_TYRES.WHEELS'
            },
            {
                url: `https://s3.exellio.de/connect_media/common/pdfs/tyres/${lang}/re_07.pdf`,
                text: 'SEARCH.SEARCH_TYRES.POTS_AND_PANS'
            },
            {
                url: `https://s3.exellio.de/connect_media/common/pdfs/tyres/${lang}/re_08.pdf`,
                text: 'SEARCH.SEARCH_TYRES.REPAIR_MATERIAL'
            }
        ];
    }
}
