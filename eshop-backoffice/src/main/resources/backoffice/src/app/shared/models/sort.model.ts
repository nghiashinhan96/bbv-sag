import { SORT_TYPE } from 'src/app/core/enums/enums';

export class SortModel {
    field: string;
    direction: SORT_TYPE;

    constructor(field: string, direction: SORT_TYPE) {
        this.field = field;
        this.direction = direction;
    }
}
