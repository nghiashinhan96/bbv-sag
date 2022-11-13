import { UserDetail } from 'src/app/core/models/user-detail.model';
import { ANALYTIC_METADATA } from '../enums/metadata.enums';
import { upperFirst, upperCase } from 'lodash';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

export class MetadataLogging {
    customerNr: string;
    '@timestamp': number;
    affiliateId: string;
    userId: number;
    countryCode: string;
    source: string;
    eventType: string;
    sessionId: string;

    constructor(data: any, user: UserDetail) {
        if (data) {
            this['@timestamp'] = data['@timestamp'];
            this.affiliateId = data.affiliateId;
            this.countryCode = this.getCountryCode();
            this.eventType = data.eventType;
            this.sessionId = data.sessionId;
        }

        if (user) {
            if (user.custNr) {
                this.customerNr = user.custNr;
            }
            this.userId = user.salesId || user.id;
            this.source = (user.salesId || user.salesUser) ? ANALYTIC_METADATA.SALES.toString() : ANALYTIC_METADATA.CUSTOMER.toString();
            this.affiliateId = this.extractAffiliateCountryCode(user.affiliateShortName);
        }
    }

    toRequest() {
        return {
            customer_nr: this.customerNr,
            '@timestamp': this['@timestamp'],
            affiliate_id: this.affiliateId,
            user_id: this.userId,
            country_code: this.countryCode,
            source: this.source,
            event_type: this.eventType,
            session_id: this.sessionId
        };
    }

    public getCountryCode() {
        if (AffiliateUtil.isAffiliateCZ9(environment.affiliate) || AffiliateUtil.isAffiliateCZ10(environment.affiliate)) {
            return 'CZ';
        }
        if (AffiliateUtil.isSb(environment.affiliate)) {
            return 'RS';
        }
        return AffiliateUtil.isAffiliateAT(environment.affiliate) ? 'AT' : 'CH';
    }

    private extractAffiliateCountryCode(affiliateShortName: string) {
        const affiliateAndCountryCode = (affiliateShortName || '').split('-');

        if (affiliateAndCountryCode.length > 1) {
            return `${upperFirst(affiliateAndCountryCode[0])} ${upperCase(affiliateAndCountryCode[1])}`;
        }

        return upperFirst(affiliateAndCountryCode[0]);
    }
}
