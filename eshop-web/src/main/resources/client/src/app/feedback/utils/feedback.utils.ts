import { NOT_AVAILABLE } from 'src/app/core/conts/app.constant';

export class FeedbackUtils {

    public static getValue(value: string): string {
        return value || NOT_AVAILABLE;
    }

    public static getValues(values: string[], ignoreEmpty = false): string {
        if (values.some(item => item)) {
            const filtered = ignoreEmpty ? values.filter(v => !!v) : values;
            return filtered.map(item => this.getValue(item)).join(', ');
        }
        return NOT_AVAILABLE;
    }
}
