import { ValidatorFn, AbstractControl, FormGroup, ValidationErrors } from '@angular/forms';
import { Constant } from '../../../app/core/conts/app.constant';

function extractHourAndMins(openingTime: string, lunchStartTime: string, lunchEndTime: string, closingTime: string) {
    return {
        openingTimeHour: +openingTime.split(Constant.COLON)[0],
        openingTimeMins: +openingTime.split(Constant.COLON)[1],
        lunchStartTimeHour: +lunchStartTime.split(Constant.COLON)[0],
        lunchStartTimeMins: +lunchStartTime.split(Constant.COLON)[1],
        lunchEndTimeHour: +lunchEndTime.split(Constant.COLON)[0],
        lunchEndTimeMins: +lunchEndTime.split(Constant.COLON)[1],
        closingTimeHour: +closingTime.split(Constant.COLON)[0],
        closingTimeMins: +closingTime.split(Constant.COLON)[1],
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

const openingTimePickerValidator: ValidatorFn = (control: FormGroup): ValidationErrors | null => {
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

    if (openingTime && !closingTime) {
        return { closingTimeRequired: true }
    }

    if (closingTime && !openingTime) {
        return { openingTimeRequired: true };
    }

    if ((lunchStartTime || lunchEndTime)) {
        let error = {};
        if (!openingTime) {
            error['openingTimeRequired'] = true;
        }
        if (!closingTime) {
            error['closingTimeRequired'] = true;
        }

        if (lunchEndTime && !lunchStartTime) {
            error['lunchTimeStartRequired'] = true;
        }

        if (lunchStartTime && !lunchEndTime) {
            error['lunchTimeEndRequired'] = true;
        }

        return error;
    }


    return null;
};

export {
    openingTimePickerValidator
};
