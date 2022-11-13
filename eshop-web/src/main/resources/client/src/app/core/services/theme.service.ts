import { Injectable } from '@angular/core';
import cssVars from 'css-vars-ponyfill';
import { UserDetail } from '../models/user-detail.model';
import { LoadingStyleutil } from './../utils/loading-style.util';
import { ThemeEnum } from '../enums/theme.enum';
import { AffiliateService } from './affiliate.service';
import { environment } from 'src/environments/environment';
import { Title } from '@angular/platform-browser';
import { AffiliateUtil } from 'sag-common';
import { CountryUtil } from '../utils/country.util';
import { TranslateService } from '@ngx-translate/core';

const URL_TAGS = ['--background', '--logo', '--pre-loader'];
@Injectable({
    providedIn: 'root'
})
export class ThemeService {
    private bgLink = '';
    private saleFaviconLink = 'assets/favicons/sag/favicon.ico';
    private saleBgLink = 'url(assets/images/background/bg-sag.jpg)';
    private saleLogo = 'url(assets/images/sag-logo-blue.png)';
    private variables;
    private primaryBackgroundByLang = {};
    isCz = AffiliateUtil.isCz(environment.affiliate);
    isSb = AffiliateUtil.isSb(environment.affiliate);
    isAxCz = AffiliateUtil.isAxCz(environment.affiliate);
    isCzBased = this.isCz || this.isAxCz;
    primaryColor = '';
    constructor(
        private affilite: AffiliateService,
        private title: Title,
        private translateService: TranslateService
    ) { }

    setTheme(json: any) {
        this.primaryColor = json && json.theme_color_1 || '';
        const container = document.querySelector('html');
        this.variables = {
            '--saleLogo': this.saleLogo
        };

        const langs = CountryUtil.getSupportedLangCodes(environment.affiliate);
        langs.forEach(lang => {
            let val = json[`background_image_${lang}`] || '';
            val = `url(${val.replace('../', 'assets/')})`;
            this.primaryBackgroundByLang[lang] = val;
        });

        Object.keys(json).forEach(k => {
            const key = ThemeEnum[k];

            if (!!key) {
                let val = json[k];
                if (URL_TAGS.indexOf(key) !== -1) {
                    val = `url(${val.replace('../', 'assets/')})`;
                }
                if (key === '--background') {
                    this.bgLink = val;
                }
                if (key === '--logo') {
                    if (this.isSb) {
                        this.variables['--saleLogo'] = val;
                    }
                }
                this.variables[key] = val;
                container.style.setProperty(key, val);
            }
        });
        if (AffiliateUtil.isAffiliateCZ10(environment.affiliate)) {
            container.style.setProperty('background-size', '100%');
            container.style.setProperty('background-position', 'top center');
        }
        cssVars({ variables: this.variables, updateURLs: false });
        LoadingStyleutil.updateFavicon();
        this.setDefaultTitle(json.title);
    }

    updateSaleView() {
        const container = document.querySelector('html');
        Object.assign(this.variables, { '--background': this.isCzBased ? this.bgLink : this.saleBgLink });
        container.style.setProperty('--background', this.isCzBased ? this.bgLink : this.saleBgLink);
        cssVars({ variables: this.variables, updateURLs: false });
        LoadingStyleutil.updateFavicon(this.saleFaviconLink);
    }

    updateNormalView() {
        const container = document.querySelector('html');
        Object.assign(this.variables, { '--background': this.bgLink });
        container.style.setProperty('--background', this.bgLink);
        cssVars({ variables: this.variables, updateURLs: false });
        LoadingStyleutil.updateFavicon();
    }

    updatePrimaryBackground(userDetail?: UserDetail) {
        const container = document.querySelector('html');

        if (userDetail && (userDetail.salesUser || userDetail.isSalesOnBeHalf)) {
            Object.assign(this.variables, { '--primary-background': 'url()' });
            container.style.setProperty('--primary-background', 'url()');
            Object.assign(this.variables, { '--preload-background': 'url()' });
            container.style.setProperty('--preload-background', 'url()');
            return;
        }
        
        const lang = this.translateService.currentLang;
        const langs = CountryUtil.getSupportedLangCodes(environment.affiliate);
        const preloadBackgrounds = [];
        langs.forEach(key => {
            if (this.primaryBackgroundByLang[key]) {
                if (key === lang) {
                    Object.assign(this.variables, { '--primary-background': this.primaryBackgroundByLang[key] });
                    container.style.setProperty('--primary-background', this.primaryBackgroundByLang[key]);
                } else {
                    preloadBackgrounds.push(this.primaryBackgroundByLang[key]);
                }
            }
        });
        if (preloadBackgrounds.length > 0) {
            const value = preloadBackgrounds.join(', ');
            Object.assign(this.variables, { '--preload-background': value });
            container.style.setProperty('--preload-background', value);
        }
    }

    updateTheme(userDetail?: UserDetail) {
        let affiliate = environment.affiliate;
        let isUpdateTheme = true;

        if (userDetail) {
            // is sales User or Onbehalf customer
            if (userDetail.salesUser || userDetail.isSalesOnBeHalf) {
                this.updateSaleView();
                affiliate = userDetail.affiliateShortName;
                isUpdateTheme = false;
            } else if (userDetail.isFinalUserRole) {
                this.updateNormalView();
                affiliate = userDetail.collectionShortname;
                isUpdateTheme = true;
            } else {
                this.updateNormalView();
                affiliate = userDetail.affiliateShortName;
                isUpdateTheme = false;
            }
        }
        if (!!affiliate) {
            this.affilite.getAffiliateSettings(affiliate).subscribe(settings => {
                if (isUpdateTheme) {
                    this.setTheme(settings);
                }
                this.title.setTitle(settings.title);
            });
        }
    }

    private setDefaultTitle(title) {
        const affiliate = title || environment.affiliate;
        this.title.setTitle(affiliate);
    }
}
