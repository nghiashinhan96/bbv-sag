import * as queryString from 'query-string';

export class BuildParamsUtil {
    public static buildParams(qrs) {
        let query = '';

        query = '?' + `${queryString.stringify(qrs)}`;

        return query;
    }
}