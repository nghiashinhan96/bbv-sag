import { ArticleImage } from './article-image.model';
import { getImageThumb, getImages, AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { ArticleAvailabilityModel, ArticleModel } from 'sag-article-detail';

export class FinalCustomerOrderItem {
    articleDesc: string;
    brand: string;
    genArtDescription: string;
    grossPrice: number;
    grossPriceWithVat: number;
    images: ArticleImage[] = [];
    netPrice: number;
    productAddon: string;
    reference: string;
    salesQuantity: number;
    supplier: string;
    vehicleDesc: string;
    type: string;
    thumbnail: any;
    articleId: string;
    quantity: number;
    article: ArticleModel;
    vinType: string;
    availabilities?: ArticleAvailabilityModel[];
    deliveryInformation: any;
    finalCustomerNetPrice: number;
    finalCustomerNetPriceWithVat: number;

    constructor(data?) {
        if (data) {
            this.articleDesc = data.articleDesc;
            this.brand = data.brand;
            this.genArtDescription = data.genArtDescription;
            this.grossPrice = data.grossPrice;
            this.grossPriceWithVat = data.grossPriceWithVat;
            this.netPrice = data.netPrice;
            this.productAddon = data.productAddon;
            this.reference = data.reference;
            this.salesQuantity = data.salesQuantity;
            this.supplier = data.supplier;
            this.vehicleDesc = data.vehicleDesc;
            this.type = data.type;
            this.articleId = data.articleId;
            this.quantity = data.quantity;
            this.availabilities = data.availabilities;

            if (data.images) {
                data.images.forEach(element => {
                    this.images.push(new ArticleImage(element));
                });
            }

            this.quantity = data.quantity;

            this.article = new ArticleModel({
                ...data.article,
                images: this.images
            });
            this.vinType = this.type === 'VIN' ? (AffiliateUtil.isBaseAT(environment.affiliate) ? 'vin-10' : 'vin-20') : '';
            this.thumbnail = this.getImagesSource();
            this.finalCustomerNetPrice = data.finalCustomerNetPrice;
            this.finalCustomerNetPriceWithVat = data.finalCustomerNetPriceWithVat;
        }
    }

    getImagesSource() {
        const source = this.images || [];
        const images = getImages(source);
        let thumbnail = null;

        if (images.length) {
            let thumb = getImageThumb(source);
            if (!thumb) {
                thumb = images[0].ref;
            }
            thumbnail = {
                'background-image': `url('${thumb}')`
            };
        }

        return thumbnail;
    }
}
