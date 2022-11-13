export default class BranchesUtils {
    public static buildDataTimePicker() {
        let hours = 0;
        const result = [];
        while (hours < 24) {
            const threshold = 30;
            const formatHour = hours < 10 ? `0${hours}` : hours.toString();
            let intialMins = 0;
            while (intialMins < 60) {
                const formatMins = intialMins < 10 ? `0${intialMins}` : intialMins.toString();
                result.push({ hour: formatHour, mins: formatMins, formatText: `${formatHour}:${formatMins}` });
                intialMins += threshold;
            }
            hours++;
        }

        return result;
    }
}
