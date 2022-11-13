import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';

import { LibUserSetting } from './../../models/lib-user-price-setting.model';
import { ArticleAvailabilityModel } from './../../models/article-availability.model';
import { ArticleDetailConfigService } from './../../services/article-detail-config.service';
import { TranslateService } from '@ngx-translate/core';

import { MarkedHtmlPipe, AffiliateUtil, SagAvailDisplaySettingModel, SAG_AVAIL_DISPLAY_STATES, SAG_AVAIL_DISPLAY_OPTIONS } from 'sag-common';
import { AvailabilityUtil } from '../../utils/availability.util';
@Component({
  selector: 'sag-article-avail',
  templateUrl: './article-avail.component.html',
  styleUrls: ['./article-avail.component.scss']
})
export class SagArticleAvailComponent implements OnInit, OnChanges {
  @Input() userSetting: LibUserSetting;
  @Input() isArticle24h: boolean;
  @Input() nonAvailableSymbol: string = 'fa-exclamation';
  @Input() isSofort: boolean;
  @Input() artAvailabilities: ArticleAvailabilityModel[];
  @Input() stock: any;

  languageCode: string;
  listAvailTextDisplay: string;
  isCH: boolean;
  isSb: boolean;
  isEhCz: boolean;
  defaultLocationDisplay: any;
  indicatorText: string = '';
  isEhCz9: boolean;
  isAffiliateCZ9: boolean;
  availDisplaySettings: SagAvailDisplaySettingModel[];
  availDisplayStates = SAG_AVAIL_DISPLAY_STATES;
  availDisplayOptions = SAG_AVAIL_DISPLAY_OPTIONS;

  constructor(
    private config: ArticleDetailConfigService,
    private markedHtmlPipe: MarkedHtmlPipe,
    private translateService: TranslateService
  ) {
    this.isSb = AffiliateUtil.isSb(this.config.affiliate);
    this.isCH = AffiliateUtil.isAffiliateCH(this.config.affiliate);
    this.isEhCz = AffiliateUtil.isEhCz(this.config.affiliate);
    this.isEhCz9 = AffiliateUtil.isEhCz9(this.config.affiliate);
    this.isAffiliateCZ9 = AffiliateUtil.isAffiliateCZ9(this.config.affiliate);
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.artAvailabilities && !changes.artAvailabilities.firstChange) {
      if (this.isSb) {
        this.defaultLocationDisplay = changes.artAvailabilities.currentValue[0];
        this.indicatorText = this.getIndicatorText(this.defaultLocationDisplay.location.name);
      } else {
        if (this.isArticle24h) {
          this.listAvailTextDisplay = this.getListAvailTextByLangCode();
        }
      }
    }
  }

  ngOnInit() {
    this.languageCode = this.config.appLangCode || this.config.defaultLangCode;
    if (this.isSb) {
      this.defaultLocationDisplay = (this.artAvailabilities && this.artAvailabilities.length > 0 && this.artAvailabilities[0]) || null;
      this.indicatorText = this.defaultLocationDisplay && this.getIndicatorText(this.defaultLocationDisplay.location.name) || '';
    } else {
      if (this.isArticle24h) {
        this.listAvailTextDisplay = this.getListAvailTextByLangCode();
      }
      this.availDisplaySettings = this.userSetting && this.userSetting.availDisplaySettings || [];
    }
  }

  getIndicatorText(locationName) {
    if (!locationName) {
      return '';
    } else {
      return this.translateService.instant(`ARTICLE.AVAILABILITY.${locationName}`);
    }
  }

  get specialNotes() {
    let text = ''
    if (this.artAvailabilities && this.artAvailabilities.length > 0) {
      text = AvailabilityUtil.initInStockNoteForTour(this.availDisplaySettings, this.artAvailabilities, this.stock, this.config.affiliate, this.config.appLangCode);
    }

    if (text) {
      return this.markedHtmlPipe.transform(text);
    }

    return '';
  }

  getAvailDisplayText(availability) {
    let state = '';

    if (availability.conExternalSource) {
      state = this.availDisplayStates.NOT_AVAILABLE;
    }
    const text = AvailabilityUtil.getAvailTextDisplayWhenHaveTime(this.availDisplaySettings, availability, this.config.appLangCode, this.config.affiliate, state);
    if (text) {
      return this.markedHtmlPipe.transform(text);
    }

    return '';
  }

  getAvailColor(availability, state?: string) {
    if (this.isAffiliateCZ9) {
      return '';
    }

    if (availability.conExternalSource) {
      state = this.availDisplayStates.NOT_AVAILABLE;
    }

    if (!state) {
      state = AvailabilityUtil.getAvailStateWhenHaveTime(availability);
    }

    return AvailabilityUtil.getAvailColorByAvailState(this.availDisplaySettings, this.config.affiliate, state);
  }

  getListAvailTextByLangCode() {
    if (!this.userSetting.dropShipmentAvailability && !this.userSetting.availIcon && !this.isEhCz9) {
      if (this.userSetting && this.userSetting.listAvailText && this.userSetting.listAvailText.filter(item => !item.content).length === 0) {
        const currentText = this.userSetting.listAvailText.find(item => item.langIso.toLocaleLowerCase() === this.languageCode);

        if (currentText) {
          return this.markedHtmlPipe.transform(currentText.content);
        }
      }
    }

    return this.nonAvailableSymbol;
  }
}
