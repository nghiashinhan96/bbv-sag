export class ArrayUtil {
    public static groupBy(array, f) {
        const groups = {};
        array.forEach(o => {
            const group = f(o);
            groups[group] = groups[group] || [];
            groups[group].push(o);
        });
        return Object.keys(groups).map(group => {
            return groups[group];
        });
    }
}
