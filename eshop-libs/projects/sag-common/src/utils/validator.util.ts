import { SAG_COMMON_EMAIL_REGEX } from '../constants/sag-common.constant';

// @dynamic
export class SagValidator {
    static getValidatorErrorMessage(validatorName: string, validatorValue?: any) {
        const config = {
            required: { message: 'SETTINGS.PROFILE.REQUIRED_FIELD' },
            invalidEmailAddress: { message: 'SETTINGS.PROFILE.INVALID_EMAIL' },
            invalidPassword: { message: 'SETTINGS.PROFILE.PASSWORD_CHANGE.INVALID' },
            minlength: {
                message: 'SETTINGS.PROFILE.PASSWORD_CHANGE.MIN_LENGTH', param: {
                    param: validatorValue.requiredLength
                }
            },
            passwordNotMatch: { message: 'SETTINGS.PROFILE.PASSWORD_CHANGE.NOT_MATCH' },
            invalidUsername: { message: 'SETTINGS.PROFILE.INVALID_USERNAME', param: { min: 1, max: 50 } },
            invalidHourlyRate: { message: 'SETTINGS.PROFILE.INVALID_HOURLY_RATE' }
        };

        return config[validatorName];
    }

    // Validate key input for multiple types
    static Validates(e: KeyboardEvent, types: string) {
        let status = false;
        // Should change the seperator to (, ) for clearer HTML code which uses this validation ?
        const typeArr = types.split(',') || [];

        // dont' care about specical key
        if (this.SpecialKey(e)) {
            return true;
        }

        typeArr.forEach(x => {
            status = this.Validate(e, x) || status;
        });

        return status;
    }

    // Validate key input for only one type
    static Validate(e: KeyboardEvent, type: string) {
        switch (type) {
            case 'number':
                return this.ValidateNumber(e);
        }
        return true;
    }

    // validate for type is number character
    static ValidateNumber(e: KeyboardEvent) {
        return !e.shiftKey
            && ((e.keyCode >= 48 && e.keyCode <= 57)
                || (e.keyCode >= 96 && e.keyCode <= 105)); // number only and it must not be space
    }

    // check special key command
    static SpecialKey(e: KeyboardEvent) {
        return (e.keyCode >= 8 && e.keyCode <= 46
            && e.keyCode !== 32
        );
    }

    public static allowNumber(event) {
        const isControlKeys = event.charCode === 8 || event.charCode === 0 || event.charCode === 13;
        return isControlKeys ? null : event.charCode >= 48 && event.charCode <= 57;
    }

    public static allowDecimalNumber(e, value) {
        return !this.allowDot(e) ? this.allowNumber(e) : value.indexOf('.') === -1; // only one decimal point allowed
    }

    public static allowDot(event) {
        return event.charCode === 46;
    }

    public static validatePercent(control) {
        const regex = /^\d+(\.\d{0,2})?$/;
        if (!control.value || !control.value.toString().match(regex)
            || !(control.value > 0)
            || !(control.value <= 100)) {
            return ({ 'invalid percent': true });
        }
        return null;
    }

    public static validateAmountForDiscount(control) {
        const regex = /^\d+(\.\d{0,2})?$/;
        if (!control.value || !control.value.toString().match(regex)
            || !(control.value > 0.00)
            || !(control.value <= 9999.99)) {
            return { 'invalid amount': true };
        }
        return null;
    }

    public static validateForPositiveNumber(control) {
        if (isNaN(control.value) || Number(control.value) <= 0) {
            return { 'invalid positive number': true };
        }
        return null;
    }

    static passwordValidator(control) {
        // (?=.*\d)                 - Assert a string has at least one number
        // (?=.*[a-zA-Z])           - Assert a string has at least one letter
        // [a-zA-Z0-9\\#$�\/! @\-.] - Assert that password only contains defined characters
        // {8,14}                   - Assert password is between 8 and 14 characters
        if (control.value.match(/^(?=.*\d)(?=.*[a-zA-Z])([a-zA-Z0-9\\#$�\/! @\-.]{8,14})$/)) {
            return null;
        } else {
            return { invalidPassword: true };
        }
    }

    static passwordMatchesValidator(control) {
        if (control.controls.newPassword.value !== control.controls.confirmPassword.value) {
            return { passwordNotMatch: true };
        }
        return null;
    }

    static emailValidator(control) {
        // RFC 2822 compliant regex
        // tslint:disable-next-line:max-line-length

        if (!control.value || control.value.match(SAG_COMMON_EMAIL_REGEX)) {
            return null;
        } else {
            return { invalidEmailAddress: true };
        }
    }

    static userNameValidator(control) {
        if (control.value.match(/^.{1,50}$/)) {
            return null;
        } else {
            return { invalidUsername: true };
        }
    }

    static removeWhiteSpace(input: string) {
        return input.replace(/\s+/g, '');
    }

    static removeNonAlphaCharacter(input: string) {
        // Alphanumeric is a combination of alphabetic and numeric characters,
        // and is used to describe the collection of Latin letters and Arabic digits or a text constructed from the collection.
        // Alpha Characters :
        // Case Sensitive - (A-Z =26 )+ (a-z=26) =52
        // Case Insensitive - A-Z =26
        // Numeric Characters : 0-9 = 10
        return input.replace(/[^A-Za-z0-9 \* säüößéèàùâêîôûëïç]/g, '');
    }
}
