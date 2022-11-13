export class AnalyticalCardLine {
    sequence: number;
    type: string;
    nr: string;
    description: string;
    uoM: string;
    quantity: number;
    unitPrice: number;
    amountInclVAT: number;

    constructor(data?: any) {
        if (!data) {
            return;
        }
        
        this.sequence = data.sequence;
        this.type = data.type;
        this.nr = data.nr;
        this.description = data.description;
        this.uoM = data.uoM;
        this.quantity = data.quantity;
        this.unitPrice = data.unitPrice;
        this.amountInclVAT = data.amountInclVAT;
    }
}

export class AnalyticalCardDetail {
    documentNr: string;
    postingDate: string;
    dueDate: string;
    description: string;
    externalDocumentNr: string;
    salesperson: string;
    remainingAmount: number;
    totalAmountInclVAT: number;
    entryLinesNo: number;
    entryLines: AnalyticalCardLine[];

    constructor(data?: any) {
        if (!data) {
            return;
        }

        this.documentNr = data.documentNr;
        this.postingDate = data.postingDate;
        this.dueDate = data.dueDate;
        this.description = data.description;
        this.externalDocumentNr = data.externalDocumentNr;
        this.salesperson = data.salesperson;
        this.remainingAmount = data.remainingAmount;
        this.totalAmountInclVAT = data.totalAmountInclVAT;
        this.entryLinesNo = data.entryLinesNo;
        this.entryLines = (data.entryLines || []).map(item => new AnalyticalCardLine(item));
    }
}