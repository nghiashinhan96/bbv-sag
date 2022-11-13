export class ArticleSort {
    ascessdingSupplier: boolean;
    ascessdingGrossPrice: boolean;
    ascessdingNetPrice: boolean;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        this.ascessdingSupplier = data.ascessdingSupplier;
        this.ascessdingGrossPrice = data.ascessdingGrossPrice;
        this.ascessdingNetPrice = data.ascessdingNetPrice;
    }

    sortBySupplier() {
        const value = this.ascessdingSupplier;
        this.reset();
        switch(value) {
            case false:
                this.ascessdingSupplier = true;
                break;
            case true:
                this.ascessdingSupplier = undefined;
                break;
            case undefined:
            default:
                this.ascessdingSupplier = false;
                break;
        }
    }

    sortByGrossPrice() {
        const value = this.ascessdingGrossPrice;
        this.reset();
        switch(value) {
            case false:
                this.ascessdingGrossPrice = true;
                break;
            case true:
                this.ascessdingGrossPrice = undefined;
                break;
            case undefined:
            default:
                this.ascessdingGrossPrice = false;
                break;
        }
    }

    sortByNetPrice() {
        const value = this.ascessdingNetPrice;
        this.reset();
        switch(value) {
            case false:
                this.ascessdingNetPrice = true;
                break;
            case true:
                this.ascessdingNetPrice = undefined;
                break;
            case undefined:
            default:
                this.ascessdingNetPrice = false;
                break;
        }
    }

    private reset() {
        this.ascessdingSupplier = undefined;
        this.ascessdingGrossPrice = undefined;
        this.ascessdingNetPrice = undefined;
    }
}
