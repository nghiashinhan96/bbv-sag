export class TyreFilter {
    season = '';
    width = '';
    height = '';
    radius = '';
    speedIndex?: string[] = [];
    loadIndex?: string[] = [];
    tyreSegment?: string[] = [];
    supplier = '';
    fzCategory = '';
    runflat: boolean;
    spike: boolean;
    category = '';
    crossSection = '';
    matchCode = '';
    totalElements: number;

    constructor(data?: any) {
        if (data) {
            this.width = data.width || '';
            this.height = data.height || '';
            this.radius = data.radius || '';
            this.speedIndex = data.speedIndex ? [...this.speedIndex, data.speedIndex] : [];
            this.loadIndex = data.loadIndex ? [...this.loadIndex, data.loadIndex] : [];
            this.tyreSegment = data.tyreSegment ? [...this.tyreSegment, data.tyreSegment] : [];
            this.supplier = data.supplier || '';
            this.runflat = data.runflat || false;
            this.spike = data.spike || false;
            this.matchCode = data.matchCode || '';
            this.totalElements = data.totalElements || 0;
        }
    }

    toTyreRequest(data?: any) {
        if (data) {
            this.season = data.season;
            this.fzCategory = data.fzCategory || '';
        }
        return this;
    }

    toTyreMotorRequest(data?: any) {
        if (data) {
            this.category = data.category || '';
        }
        return this;
    }

    setAdditionalFields(isChecked: boolean, fieldName: string, fieldValue: string) {
        if (fieldName === 'speed_index') {
            this.speedIndex = isChecked ? [...this.speedIndex, fieldValue] : this.speedIndex.filter(speed => speed !== fieldValue);
        } else if (fieldName === 'load_index') {
            this.loadIndex = isChecked ? [...this.loadIndex, fieldValue] : this.loadIndex.filter(load => load !== fieldValue);
        } else if (fieldName === 'tyre_segment') {
            this.tyreSegment = isChecked ? [...this.tyreSegment, fieldValue] : this.tyreSegment.filter(segment => segment !== fieldValue);
        }
    }
}
