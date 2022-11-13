import { AbstractControl, ValidatorFn } from '@angular/forms';
import { EMPTY_STR } from 'angular-mydatepicker';
import { REGEX } from '../conts/app.constant';
export class Validator {
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
            case 'decimalNumber':
                return this.allowDecimalNumber(e, (e.target as any).value);
        }
        return true;
    }

    // validate for type is number character
    static ValidateNumber(e: KeyboardEvent) {
        return !e.shiftKey
            && ((e.keyCode >= 48 && e.keyCode <= 57)
                || (e.keyCode >= 96 && e.keyCode <= 105)); // number only and it must not be space
    }

    static validateNumberInput(e: KeyboardEvent, maxLength: number) {
        return (
            (!e.shiftKey &&
                (this.isDigitPad(e) || this.isDigitNumpad(e)) &&
                this.validateMaxLengthWithoutSpace(e, maxLength)) ||
            this.isBackspaceOrTab(e) ||
            this.isArrowKey(e) ||
            this.isDelKey(e) ||
            this.isPaste(e)
        );
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
        return !Validator.allowDot(e) ? Validator.allowNumber(e) : value.indexOf('.') === -1; // only one decimal point allowed
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

        if (!control.value || control.value.match(REGEX.EMAIL_REGEX)) {
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

    static validateAlphanumeric(e: KeyboardEvent, maxLength: number) {
        return (
            (e.shiftKey && this.isAlphabet(e)) ||
            this.isBracket(e) ||
            (!e.shiftKey &&
                (this.isAlphabet(e) || this.isDigitPad(e) || this.isDigitNumpad(e)) &&
                this.validateMaxLengthWithoutSpace(e, maxLength)) ||
            this.isAtSign(e) ||
            this.isPaste(e) ||
            this.isBackspaceOrTab(e) ||
            this.isDotSign(e) ||
            this.isPlusOrMinusSign(e) ||
            this.isArrowKey(e) ||
            this.isDelKey(e)
        );
    }

    static validateMaxLengthWithoutSpace(e: any, maxLength: number) {
        let value = e.target.value;
        value = value.replace(/\s/g, '');
        return value.length < maxLength;
    }

    static validateAlphanumericWithSpace(e: KeyboardEvent, maxLength: number) {
        return (
            (e.shiftKey && this.isAlphabet(e)) ||
            this.isBracket(e) ||
            (!e.shiftKey &&
                (this.isAlphabet(e) || this.isDigitPad(e) || this.isDigitNumpad(e)) &&
                this.validateMaxLengthWithoutSpace(e, maxLength)) ||
            this.isAtSign(e) ||
            this.isDelKey(e) ||
            this.isBackspaceOrTab(e) ||
            this.isDotSign(e) ||
            this.isPlusOrMinusSign(e) ||
            this.isArrowKey(e) ||
            this.isSpace(e)
        );
    }

    private static isAlphabet(e: KeyboardEvent) {
        return e.keyCode >= 65 && e.keyCode <= 90;
    }

    private static isDigitPad(e: KeyboardEvent) {
        return e.keyCode >= 48 && e.keyCode <= 57;
    }

    private static isDigitNumpad(e: KeyboardEvent) {
        return e.keyCode >= 96 && e.keyCode <= 105;
    }

    private static isArrowKey(e: KeyboardEvent) {
        return e.keyCode >= 37 && e.keyCode <= 40;
    }

    private static isBackspaceOrTab(e: KeyboardEvent) {
        return e.keyCode === 8 || e.keyCode === 9;
    }

    private static isPlusOrMinusSign(e: KeyboardEvent) {
        return (
            e.keyCode === 107 ||
            e.keyCode === 109 ||
            (e.shiftKey && e.keyCode === 187) ||
            (e.shiftKey && e.keyCode === 189)
        );
    }

    private static isDotSign(e: KeyboardEvent) {
        return e.keyCode === 190;
    }

    private static isAtSign(e: KeyboardEvent) {
        return e.shiftKey && e.keyCode === 50;
    }

    private static isSpace(e: KeyboardEvent) {
        return e.keyCode === 32;
    }

    private static isDelKey(e: KeyboardEvent) {
        return e.keyCode === 46;
    }

    private static isBracket(e: KeyboardEvent) {
        return e.shiftKey && (e.keyCode === 57 || e.keyCode === 48);
    }

    private static isPaste(e: KeyboardEvent) {
        return e.ctrlKey && e.keyCode === 86;
    }

    static invalidValueValidator(nameRe: RegExp, canEmpty = false): ValidatorFn {
        return (control: AbstractControl): { [key: string]: any } | null => {
            const valid = nameRe.test(control.value || '');
            if (canEmpty && control.value === EMPTY_STR) {
                return null;
            }
            return valid ? null : { invalidValue: { value: control.value } };
        };
    }

    static barcodeValidator(control) {
        if (control.value && control.value.indexOf('*') > -1) {
            return { invalidBarcode: true };
        } else {
            return null;
        }
    }
}
