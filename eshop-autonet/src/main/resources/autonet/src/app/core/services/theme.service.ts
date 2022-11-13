import { Injectable } from '@angular/core';
import { ThemeEnum } from '../enums/theme.enum';
import { AffiliateService } from './affiliate.service';
import { environment } from 'src/environments/environment';
import cssVars from 'css-vars-ponyfill';
import { UserDetail } from '../models/user-detail.model';

const URL_TAGS = ['--background', '--logo'];
@Injectable({
    providedIn: 'root'
})
export class ThemeService {

    constructor(private affilite: AffiliateService) { }

    setTheme(json: any) {
        const variables = {};
        Object.keys(json).forEach(k => {
            const key = ThemeEnum[k];
            if (!!key) {
                let val = json[k];
                if (URL_TAGS.indexOf(key) !== -1) {
                    val = `url(${val.replace('../', 'assets/')})`;
                }
                variables[key] = val;
                document.body.style.setProperty(key, val);
            }
        });
        cssVars({ variables });
    }

    updateSaleView() {

    }

    updateTheme(userSetting?: UserDetail) {

        let affiliate = environment.affiliate;
        if (userSetting) {
            // is sales User or Onbehalf customer
            if (userSetting.salesUser) {
                this.updateSaleView();
                affiliate = null;
            } else {
                if (userSetting.isFinalUserRole) {
                    affiliate = userSetting.collectionShortname;
                } else {
                    affiliate = null;
                }
            }
        }
        if (!!affiliate) {
            this.affilite.getAffiliateSettings(affiliate).subscribe(settings => {
                this.setTheme(settings);
            });
        }
    }
}
