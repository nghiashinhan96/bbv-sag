export class CatalogIframeUtil {
    public static updateCatalogIframeTop(renderer: any, document: any, iframeSelector: string, initHeight: number) {
        const topMessage = document.querySelector('.top-message');
        if (!!topMessage) {
            const message = topMessage.querySelector('.banner') || topMessage.querySelector('.alert');
            const unicatCatelog = document.querySelector(iframeSelector);
            let height = initHeight;

            if (!!message && !!unicatCatelog) {
                height = Number(message.offsetHeight) + height;
            }

            renderer.setStyle(unicatCatelog, 'top', `${height}px`);
        }
    }
}
