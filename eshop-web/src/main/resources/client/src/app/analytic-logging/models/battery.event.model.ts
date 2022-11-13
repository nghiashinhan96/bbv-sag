import { MetadataLogging } from './analytic-metadata.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { BasketItemSource } from './basket-item-source.model';

export class BatteriesEvent extends MetadataLogging {
    eventType = Constant.BATTERIES_EVENT;
    batVoltage: number;
    batAmpere: number;
    batLength: number;
    batWidth: number;
    batHeigth: number;
    batCircuit: string;
    batPolarity: string;
    batWithStartstop: boolean;
    batWithoutStartstop: boolean;
    batArticleIdResult: string[];
    batArticleNameResult: string[];
    batNumberOfHits: number;
    batAdvertisementClicked: boolean;
    basketItemSourceId?: string;
    basketItemSourceDesc?: string;

    constructor(metadata: MetadataLogging | any, user: UserDetail, data: any, numberOfHits?: number, basketItemSource?: BasketItemSource) {
        super(metadata, user);
        if (data) {
            this.batVoltage = data.voltage;
            this.batAmpere = +data.ampereHour;
            this.batLength = +data.length;
            this.batWidth = +data.width;
            this.batHeigth = +data.height;
            this.batCircuit = data.interconnection;
            this.batPolarity = data.typeOfPole;
            this.batWithStartstop = data.withStartStop;
            this.batWithoutStartstop = data.withoutStartStop;
            this.batArticleIdResult = data.articleIdResult;
            this.batArticleNameResult = data.articleNameResult;
            this.batNumberOfHits = numberOfHits;
        }
        if (basketItemSource) {
            this.basketItemSourceId = basketItemSource.basketItemSourceId;
            this.basketItemSourceDesc = basketItemSource.basketItemSourceDesc;
        }
    }

    toAdsEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            bat_advertisement_clicked: this.batAdvertisementClicked
        };
    }

    toEventRequest() {
        const request = super.toRequest();

        return {
            ...request,
            bat_voltage: this.batVoltage,
            bat_ampere: this.batAmpere,
            bat_length: this.batLength,
            bat_width: this.batWidth,
            bat_heigth: this.batHeigth,
            bat_circuit: this.batCircuit,
            bat_polarity: this.batPolarity,
            bat_with_startstop: this.batWithStartstop,
            bat_without_startstop: this.batWithoutStartstop,
            bat_article_id_result: this.batArticleIdResult,
            bat_article_name_result: this.batArticleNameResult,
            bat_number_of_hits: this.batNumberOfHits,
            basket_item_source_id: this.basketItemSourceId,
            basket_item_source_desc: this.basketItemSourceDesc
        };
    }
}
