import { ValidatorFn, AbstractControl, FormGroup, ValidationErrors } from '@angular/forms';
import { COLON } from 'src/app/core/conts/app.constant';

function invalidValueValidator(nameRe: RegExp): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
        const valid = nameRe.test(control.value || '');
        return valid ? null : { invalidValue: { value: control.value } };
    };
}

function extractHourAndMins(openingTime: string, lunchStartTime: string, lunchEndTime: string, closingTime: string) {
    return {
        openingTimeHour: +openingTime.split(COLON)[0],
        openingTimeMins: +openingTime.split(COLON)[1],
        lunchStartTimeHour: +lunchStartTime.split(COLON)[0],
        lunchStartTimeMins: +lunchStartTime.split(COLON)[1],
        lunchEndTimeHour: +lunchEndTime.split(COLON)[0],
        lunchEndTimeMins: +lunchEndTime.split(COLON)[1],
        closingTimeHour: +closingTime.split(COLON)[0],
        closingTimeMins: +closingTime.split(COLON)[1],
    };
}

function compareOpeningAndClosingTime(openingHour: number, openingMins: number, closingHour: number, closingMins: number) {
    if ((openingHour > closingHour) || openingHour === closingHour && openingMins >= closingMins) {
        return { invalidTime: true };
    }
    return null;
}

function compareAllTime(
    openingHour: number,
    openingMins: number,
    lunchOpeningHour: number,
    lunchOpeningMins: number,
    lunchClosingHour: number,
    lunchClosingMins: number,
    closingHour: number,
    closingMins: number) {

    if ((openingHour > lunchOpeningHour) || (openingHour === lunchOpeningHour && openingMins >= lunchOpeningMins)) {
        return { invalidOpeningTime: true };
    }

    if ((lunchOpeningHour > lunchClosingHour) ||
        (lunchOpeningHour === lunchClosingHour && lunchOpeningMins >= lunchClosingMins)) {
        return { invalidLunchStartTime: true };
    }

    if ((lunchClosingHour < lunchOpeningHour) ||
        (lunchClosingHour === lunchOpeningHour && lunchClosingMins <= lunchOpeningMins)) {
        return { invalidLunchEndTime: true };
    }

    if ((closingHour < lunchClosingHour) || (lunchClosingHour === closingHour && lunchClosingMins >= closingMins)) {
        return { invalidClosingTime: true };
    }

    return null;
}

const branchTimePickerValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
    const openingTime = control.get('openingTime').value;
    const lunchStartTime = control.get('lunchOpeningTime').value;
    const lunchEndTime = control.get('lunchClosingTime').value;
    const closingTime = control.get('closingTime').value;

    const branchTime = extractHourAndMins(openingTime, lunchStartTime, lunchEndTime, closingTime);

    if ((openingTime && closingTime) && (lunchStartTime && lunchEndTime)) {
        return compareAllTime(branchTime.openingTimeHour, branchTime.openingTimeMins,
            branchTime.lunchStartTimeHour, branchTime.lunchStartTimeMins,
            branchTime.lunchEndTimeHour, branchTime.lunchEndTimeMins,
            branchTime.closingTimeHour, branchTime.closingTimeMins);
    }

    if ((openingTime && closingTime) && (!lunchStartTime && !lunchEndTime)) {
        return compareOpeningAndClosingTime(branchTime.openingTimeHour, branchTime.openingTimeMins,
            branchTime.closingTimeHour, branchTime.closingTimeMins);
    }

    if (!lunchStartTime && lunchEndTime) {
        return { lunchTimeStartRequired: true };
    }

    if (lunchStartTime && !lunchEndTime) {
        return { lunchTimeEndRequired: true };
    }

    return null;
};

export {
    invalidValueValidator,
    branchTimePickerValidator
};
