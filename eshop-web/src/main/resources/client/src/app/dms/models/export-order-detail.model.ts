export class ExportOrderDetail {
  articleNumber: string;
  totalGrossPriceIncl: string;
  totalGrossPrice: string;
  grossPrice: string;
  description: string;
  quantity: number;
  totalNetPriceInclVat: string;
  totalNetPrice: string;
  netPrice: string;
  totalUvpeIncl: string; // Column 10. total of Suggested Retail Price include vat
  totalUvpe: string; // Column 11. total of Single price UVPE
  uvpe: string; // Column 12. single UVPE price
  articleId: number;
  brand: string;
  artAdditionalDescription: string;
  fromBranchTime: string;
  deliveryStatus: string;
  deliveryNote: string;

  constructor(data?) {
    if (!data) {
      return;
    }

    this.articleNumber = data.articleNumber;
    this.totalGrossPriceIncl = data.totalGrossPriceIncl;
    this.totalGrossPrice = data.totalGrossPrice;
    this.grossPrice = data.grossPrice;
    this.description = data.description;
    this.quantity = data.quantity;
    this.totalNetPriceInclVat = data.totalNetPriceInclVat;
    this.totalNetPrice = data.totalNetPrice;
    this.netPrice = data.netPrice;
    this.totalUvpeIncl = data.totalUvpeIncl;
    this.totalUvpe = data.totalUvpe;
    this.uvpe = data.uvpe;
    this.articleId = data.articleId;
    this.brand = data.brand;
    this.artAdditionalDescription = data.artAdditionalDescription;
    this.fromBranchTime = data.fromBranchTime;
    this.deliveryStatus = data.deliveryStatus;
    this.deliveryNote = data.deliveryNote;
  }

}
