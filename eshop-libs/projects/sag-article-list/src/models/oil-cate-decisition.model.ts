import { OilType } from '../enums/oil-type.enum';
import { OilChoice } from './oil-cate-choice.model';

export interface DecisitionTree {
    type: OilType;
    choice: OilChoice;
    application_id: null;
}
