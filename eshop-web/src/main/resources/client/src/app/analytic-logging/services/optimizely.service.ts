import { Injectable } from "@angular/core";

declare var ConnectOptimizely: any;

@Injectable({ providedIn: 'root' })
export class OptimizelyService {
    constructor() { }

    setUserData(loggedUser) {
        try {
            const currencyIsoCode = loggedUser.customer && loggedUser.customer.currency;
            ConnectOptimizely.setCurrencyCode(currencyIsoCode);
        } catch (error) { }
    }

    initScript(settings: any) {
        if (!settings || !settings.optimizely_id) {
            return;
        }
        try {
            if (ConnectOptimizely.initiated) {
                return;
            }

            ConnectOptimizely.setCurrencyCode(settings && settings.currency);

            document.head.appendChild(this.generateLinkMeta('preload', `//cdn.optimizely.com/js/${settings.optimizely_id}.js`, 'script'));
            document.head.appendChild(this.generateLinkMeta('preconnect', '//logx.optimizely.com'));

            document.head.appendChild(this.generateScript('', '', `//cdn.optimizely.com/js/${settings.optimizely_id}.js`));

            ConnectOptimizely.initiated = true;
        } catch (error) { }
        
    }

    addRevenue(orders) {
        try {
            let subTotalWithNet = 0;
            (orders || []).forEach(order => {
                if (!order.errorMsg) {
                    subTotalWithNet += order.subTotalWithNet;
                }
            });
            ConnectOptimizely.addRevenue({ subTotalWithNet });
        } catch (error) { }
    }

    private generateLinkMeta(rel: string, href: string, as?: string) {
        const el = document.createElement('link');
        el.rel = rel;
        el.href = href;
        if (as) {
            el.as = as;
        }
        return el;
    }

    private generateScript(id: string, type: string, src: string, async = false) {
        const el = document.createElement('script');
        if (id) {
            el.id = id;
        }
        if (type) {
            el.type = type;
        }
        el.src = src;
        el.async = async;
        return el;
    }
}