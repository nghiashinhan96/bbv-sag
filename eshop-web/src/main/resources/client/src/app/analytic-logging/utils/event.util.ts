import { pickBy } from 'lodash';

export class EventUtil {
    static normalize(data: any) {
        return pickBy({ ...data }, (value) => {
            return value !== ' ' && value !== '' && value !== null && value !== undefined;
        });
    }
}
