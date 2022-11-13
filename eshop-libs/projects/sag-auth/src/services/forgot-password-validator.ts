import { Injectable } from '@angular/core';
import { SAG_COMMON_EMAIL_REGEX } from 'sag-common';

@Injectable()
export class ForgotPasswordValidator {

    public validateInputBeforeSendCode(inputUsername: string, inputEmail: string) {
        if (this.isInputEmpty(inputUsername) && this.isInputEmpty(inputEmail)) {
            return 'FORGOT_PASSWORD.ERROR_MESSAGE.INPUT_REQUIRED';
        }
        if (!this.isInputEmpty(inputEmail) && !this.isValidEmailFormat(inputEmail)) {
            return 'FORGOT_PASSWORD.ERROR_MESSAGE.EMAIL_INPUT_REQUIRED';
        }
        return '';
    }

    isInputEmpty(input) {
        return input == null || input.trim() === '';
    }

    isValidEmailFormat(email: string) {
        return SAG_COMMON_EMAIL_REGEX.test(email);
    }
}
