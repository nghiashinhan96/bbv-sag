import { environment } from 'src/environments/environment';

export class LoadingStyleutil {
    private static favicon: HTMLLinkElement;

    public static updateFavicon(url?: string) {
        this.removeFavicon();
        const favicon = document.createElement('link');
        favicon.rel = 'icon';
        favicon.type = 'image/x-icon';
        favicon.id = 'affiliate_favicon';
        favicon.href = this.getAffiliateFaviconUrl(url);
        document.getElementsByTagName('head')[0].appendChild(favicon);
    }

    public static getAffiliateFaviconUrl(url?: string) {
        if (!!url) {
            return url;
        }
        return `assets/favicons/${environment.shortAffiliate}/favicon.ico`;
    }

    private static removeFavicon() {
        const links = document.getElementsByTagName('link');
        const head = document.getElementsByTagName('head')[0];
        for (const link of Array.from(links)) {
            if (link.getAttribute('rel') === 'icon') {
                head.removeChild(link);
            }
        }
    }
}
