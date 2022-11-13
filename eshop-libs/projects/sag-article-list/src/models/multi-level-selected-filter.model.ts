export interface MultiLevelSelectedFilter {
    id: Map<string, string>; // key is id and value is uuid
    categoryName: string;
    criteriaIds: Map<string, string>; // key is id and value is uuid
    criteriaValueIds: Map<string, string>; // key is id and value is uuid
    deletedCriteriaVallue: string[];
}

export class MultiLevelSelectedFilterModel implements MultiLevelSelectedFilter {
    id: Map<string, string>;
    categoryName: string;
    criteriaIds: Map<string, string>;
    criteriaValueIds: Map<string, string>;
    deletedCriteriaVallue: string[];

    constructor(data?: any) {
        if (data) {
            this.id = data && data.id;
            this.categoryName = this.formatCategoryName((data && data.categoryName));
            this.criteriaIds = data && data.criteriaIds;
            this.criteriaValueIds =  data && data.criteriaValueIds;
            this.deletedCriteriaVallue = data && data.deletedCriteriaVallue;
        }
    }

    formatCategoryName(input: string) {
        if (input) {
            return input.substring(0, input.lastIndexOf('('));
        }
    }
}
