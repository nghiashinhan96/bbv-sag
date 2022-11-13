import { AffiliateUtil, MarkedHtmlPipe, ProjectId, SAG_AVAIL_DISPLAY_STATES } from 'sag-common';
import { Component, OnInit, Input } from '@angular/core';
import { AVAILABILITY_INFO, CZ_AVAIL_STATE } from '../../enums/availability-info.enum';
import { ArticleAvailabilityModel } from '../../models/article-availability.model';
import { LibUserSetting } from '../../models/lib-user-price-setting.model';
import { AvailabilityUtil } from '../../utils/availability.util';
import { TranslateService } from '@ngx-translate/core';
import { ArticleDetailConfigService } from './../../services/article-detail-config.service';
import { ArticleModel } from '../../models/article.model';
import { ArticleAvailLocationItemModel } from '../../models/article-avail-location-item.model';

@Component({
  selector: 'sag-article-avail-popup-availability',
  templateUrl: './article-avail-popup-availability.component.html',
  styleUrls: ['./article-avail-popup-availability.component.scss']
})
export class SagArticleAvailPopupAvailabilityComponent implements OnInit {
  @Input() set availabilities(data: ArticleAvailabilityModel[]) {
    if (!this.isSb) {
      this.availData = this.buildDisplayTimeForAvailabilities(data);
      this.updateTourOrPickUpMode(this.availData);
    } else {
      this.locations = data && data[0] && data[0].location.items || [];
    }
  }
  @Input() userSetting: LibUserSetting;
  @Input() affiliateCode: string;
  @Input() user: any;
  @Input() article: ArticleModel;

  AVAIL_STATE_24_HOURS;
  AVAIL_STATE_24_HOURS_CZ;
  AVAIL_STATE_NO_STOCK = AVAILABILITY_INFO.AVAIL_STATE_NO_STOCK;
  availData: ArticleAvailabilityModel[];
  isTourMode: boolean;
  isPickupMode: boolean;
  languageCode: string;
  branchName: string;
  detailAvailTextDisplay: string;
  isEhCz: boolean;
  isEh: boolean;
  isCH: boolean;
  isEhCh: boolean;
  isSb: boolean;
  PROJECT_ID = ProjectId;
  projectId = ProjectId.CONNECT;
  availDisplayStates = SAG_AVAIL_DISPLAY_STATES;
  locations: ArticleAvailLocationItemModel[] = [];

  constructor(
    private markedHtmlPipe: MarkedHtmlPipe,
    private translateService: TranslateService,
    private config: ArticleDetailConfigService
  ) {
    this.detailAvailTextDisplay = this.translateService.instant('ARTICLE.24_HOURS');
  }

  ngOnInit() {
    this.isCH = AffiliateUtil.isAffiliateCH(this.affiliateCode);
    this.isEhCh = AffiliateUtil.isEhCh(this.affiliateCode);
    this.isEhCz = AffiliateUtil.isEhCz(this.affiliateCode);
    this.isSb = AffiliateUtil.isSb(this.config.affiliate);
    this.isEh = this.isEhCh || this.isEhCz;
    this.languageCode = this.config.appLangCode || this.config.defaultLangCode;
    this.branchName = this.config.branchName;
    this.projectId = this.config.projectId;

    if (this.isEhCz) {
      this.AVAIL_STATE_24_HOURS = CZ_AVAIL_STATE.NON_ORDERALE;
      this.AVAIL_STATE_24_HOURS_CZ = AVAILABILITY_INFO.AVAIL_STATE_INORDER_24_HOURS;
    } else {
      this.AVAIL_STATE_24_HOURS = AVAILABILITY_INFO.AVAIL_STATE_INORDER_24_HOURS;
    }

    if (!this.isEhCz) {
      this.detailAvailTextDisplay = this.getDetailAvailTextByLangCode();
    }

    if(this.isSb) {
      this.locations = this.availData && this.availData[0] && this.availData[0].location.items || [];
    }
  }

  getAvailColor(availability: ArticleAvailabilityModel, state?: string) {
    if (AffiliateUtil.isAffiliateCZ9(this.affiliateCode)) {
      return '';
    }

    if (availability.conExternalSource) {
      state = this.availDisplayStates.NOT_AVAILABLE;
    }

    if (!state) {
      state = AvailabilityUtil.getAvailStateWhenHaveTime(availability);
    }

    return AvailabilityUtil.getAvailColorByAvailState(this.userSetting && this.userSetting.availDisplaySettings, this.affiliateCode, state);
  }

  getDetailAvailTextByLangCode() {
    if (this.userSetting && this.userSetting.detailAvailText) {
      const currentText = this.userSetting.detailAvailText.find(item => item.langIso.toLocaleLowerCase() === this.languageCode);

      if (currentText) {
        return this.markedHtmlPipe.transform(currentText.content);
      }
    }

    return this.detailAvailTextDisplay;
  }

  getSpecialNotesWithDeliveryTour(artAvailabilitie: ArticleAvailabilityModel) {
    if (this.userSetting && this.userSetting.availDisplaySettings) {
      return AvailabilityUtil.initSpecialNotesWithDeliveryTour(this.userSetting.availDisplaySettings, artAvailabilitie, this.affiliateCode, true);
    }

    return '';
  }

  updateTourOrPickUpMode(availabilities) {
    if (!!availabilities && availabilities.length > 0) {
      const availability = availabilities[0] as ArticleAvailabilityModel;
      this.isTourMode = false;
      this.isPickupMode = false;
      if (availability.sendMethodCode) {
        this.isPickupMode = availability.sendMethodCode === AVAILABILITY_INFO.PICKUP;
        this.isTourMode = availability.sendMethodCode === AVAILABILITY_INFO.TOUR;
      }
    }
  }

  private buildDisplayTimeForAvailabilities(availabilities: ArticleAvailabilityModel[]) {
    return (availabilities || []).filter(avail => !!avail).map(avail => {
      avail.dateFormat = avail.formattedCETArrivalDate || '';
      avail.timeFormat = avail.formattedCETArrivalTime || '';
      return avail;
    });
  }

}
