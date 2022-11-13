export class VatTypeDisplayUtil {
    public static convertResponseData(value) {
        if (!value) return;

        const array = value.split('');

        return {
            list: array.length > 0 && Number(array[0]) === 1,
            detail: array.length > 1 && Number(array[1]) === 1,
        }
    }

    public static mapDataToRequest(data: { list: boolean, detail: boolean }) {
        return `${data.list === true ? 1 : 0}${data.detail === true ? 1 : 0}`
    }
}