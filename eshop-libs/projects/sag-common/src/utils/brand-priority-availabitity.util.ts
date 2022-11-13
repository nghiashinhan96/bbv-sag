export class BrandPriorityAvailabilityUtil {
    public static convertResponseData(value) {
        if (!value) return;

        const array = value.split('');

        return {
            p1: array.length > 0 && Number(array[0]) === 1,
            p2: array.length > 1 && Number(array[1]) === 1,
            p3: array.length > 2 && Number(array[2]) === 1
        }
    }

    public static mapDataToRequest(data: { p1: boolean, p2: boolean, p3: boolean }) {
        return `${data.p1 === true ? 1 : 0}${data.p2 === true ? 1 : 0}${data.p3 === true ? 1 : 0}`
    }
}