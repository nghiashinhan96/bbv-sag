import { TranslateService } from '@ngx-translate/core';
import { AbstractControl, FormControl, FormGroup, ValidatorFn } from '@angular/forms';
import { BIGGEST_INT, MAX_OF_SMALL_INT } from '../conts/app.constant';

/* tslint:disable */
const emailRegex: RegExp = /^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$/;
export class BOValidator {
    public static passwordValidator(control) {
        // (?=.*\d)                 - Assert a string has at least one number
        // (?=.*[a-zA-Z])           - Assert a string has at least one letter
        // [a-zA-Z0-9\\#$£\/! @\-.] - Assert that password only contains defined characters
        // {8,14}                   - Assert password is between 8 and 14 characters
        if (
            control.value.match(
                /^(?=.*\d)(?=.*[a-zA-Z])([a-zA-Z0-9\\#$£\/! @\-.]{8,14})$/
            )
        ) {
            return false;
        } else {
            return { invalidPassword: true };
        }
    }

    public static passwordMatchesValidator(control) {
        if (control.controls.pass1.value !== control.controls.pass2.value) {
            return { passwordNotMatch: true };
            
        }
        return false;
    }

    public static getValidatorErrorMessage(
        validatorName: string,
        translateService: TranslateService,
        validatorValue?: any
    ) {
        let config = {
            required: translateService.instant("PASSWORD.REQUIRED_FIELD"),
            invalidPassword: translateService.instant("PASSWORD.INVALID"),
            minlength: translateService.instant("PASSWORD.MIN_LENGTH", {
                param: validatorValue.requiredLength,
            }),
            passwordNotMatch: translateService.instant("PASSWORD.NOT_MATCH"),
            invalidEmailAddress: translateService.instant(
                "COMMON.MESSAGE.INVALID_EMAIL"
            ),
            invalidHourlyRate: translateService.instant(
                "COMMON.MESSAGE.INVALID_HOURLY_RATE"
            ),
        };

        return config[validatorName];
    }

    static userNameValidator(control) {
        if (control.value.match(/^.{1,50}$/)) {
            return null;
        } else {
            return { invalidUsername: true };
        }
    }

    static emailValidator(control) {
        // RFC 2822 compliant regex
        if (control.value === "" || control.value.match(emailRegex)) {
            return null;
        } else {
            return { invalidEmailAddress: true };
        }
    }

    static validateHourlyRate(control) {
        if (
            control.value != null &&
            (Number(control.value) < 0 || control.value > 999999999)
        ) {
            return { invalidHourlyRate: true };
        }
        return null;
    }

    static validateQuantity(control) {
        if (control.value === null || control.value > BIGGEST_INT) {
            return { invalidQuantity: true };
        }

        let value: String = control.value.toString();
        if (value.match(/^[1-9]+[0-9]*/) && Number.isInteger(Number(value))) {
            return null;
        } else {
            return { invalidQuantity: true };
        }
    }

    static isSmallInteger(control) {
        if (control.value === null || control.value > MAX_OF_SMALL_INT) {
            return { invalidSmallInteger: true };
        }
        return null;
    }

    static validateNumber(e: KeyboardEvent, maxLength: number) {
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

    static validateMaxLengthWithoutSpace(e: any, maxLength: number) {
        let value = e.target.value;
        value = value.replace(/\s/g, "");
        return value.length < maxLength;
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

    public static validateForPositiveNumber(control) {
        if (isNaN(control.value) || Number(control.value) <= 0) {
            return { "invalid positive number": true };
        }
        return null;
    }

    public static validateForVatNumber(control) {
        if (isNaN(control.value) || Number(control.value) < 0 || Number(control.value) > 100) {
            return { "invalid_range_vat": true };
        }
        return null;
    }

    public static durationValidator(c: FormControl) {
        const DURATION_REGEXP = new RegExp("[0-9][0-9]:[0-9][0-9]");
        return DURATION_REGEXP.test(c.value)
            ? null
            : {
                duration: {
                    valid: false,
                },
            };
    }

    public static hoursValidator(c: FormControl) {
        const val = c.value;
        if (!val) {
            return null;
        }
        const num = Number(val);
        if (Number.isNaN(num) || val < 0) {
            return {
                hoursValidator: true,
            };
        }
        return null;
    }

    public static minutesValidator(c: FormControl) {
        const val = c.value;
        if (!val) {
            return null;
        }
        const num = Number(val);
        if (Number.isNaN(num) || val < 0 || val > 59) {
            return {
                minutesValidator: true,
            };
        }
        return null;
    }

    public static daysValidator(c: FormControl) {
        const val = c.value;
        if (!val) {
            return null;
        }
        const num = Number(val);
        if (Number.isNaN(num) || val < 0) {
            return {
                minutesValidator: true,
            };
        }
        return null;
    }

    static emailDomain(control) {
        const allowedDomain = [
            'sag-ag.ch',
            'matik.ch',
            'technomag.ch',
            'waelchlibollier.ch',
            'derendinger.ch',
            'derendinger.at',
            'hella-austria.at',
            'matik.at',
            'sag-austria.at',
            'bbv.ch',
            'bbv.vn'
        ];

        if (!control.value || allowedDomain.find(domain => control.value.endsWith(domain))) {
            return null;
        } else {
            return { invalidEmailAddress: true };
        }
    }

    static invalidValueValidator(nameRe: RegExp, canEmpty = false): ValidatorFn {
        return (control: AbstractControl): { [key: string]: any } | null => {
            const valid = nameRe.test(control.value || '');
            if (canEmpty && control.value === '') {
                return null;
            }
            return valid ? null : { invalidValue: { value: control.value } };
        };
    }

    public static noWhitespaceValidator(control: FormControl) {
        const isWhitespace = (control.value || '').trim().length === 0;
        const isValid = !isWhitespace;
        return isValid ? null : { whitespace: true };
    }

    public static requiredFieldWithConditionsValidator(mainField: string, handleConditionsFunc: (formGroup: FormGroup) => boolean) {
        return (control: FormGroup): { [key: string]: any } | null => {
            if (handleConditionsFunc(control) && !control.controls[mainField].value) {
                return { [mainField + 'IsRequired']: true };
            }
            return null;
        }
    }
}
