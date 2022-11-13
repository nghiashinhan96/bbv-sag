export interface SagInContextSearchRequest {
    vehIds: string;
    genArtIds: string;
    size: number;
    selectedOilIds?: string[];
    selectedCategoryIds?: string[];
    selectedFromQuickClick?: boolean;
}
