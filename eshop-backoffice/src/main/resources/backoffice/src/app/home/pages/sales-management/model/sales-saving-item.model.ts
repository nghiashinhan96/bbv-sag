import { EMPTY_STRING } from 'src/app/core/conts/app.constant';
import { GENDER } from 'src/app/core/enums/enums';

export class SalesSavingItemModel {
    firstName: string;
    lastName: string;
    primaryContactEmail: string;
    personalNumber: string;
    gender: string;
    legalEntityId: string;

    constructor(data?) {
        if (!data) {
            return;
        }

        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.primaryContactEmail = data.primaryContactEmail;
        this.personalNumber = data.personalNumber;
        this.gender = data.gender;
        this.legalEntityId = data.legalEntityId;
    }

    public static getDefaultModel() {
        return new SalesSavingItemModel({
            firstName: EMPTY_STRING,
            lastName: EMPTY_STRING,
            personalNumber: EMPTY_STRING,
            primaryContactEmail: EMPTY_STRING,
            legalEntityId: EMPTY_STRING,
            gender: GENDER.MALE.toString()
        });
    }

    public equals(target: SalesSavingItemModel) {
        return this.firstName === target.firstName
            && this.lastName === target.lastName
            && this.primaryContactEmail === target.primaryContactEmail
            && this.personalNumber === target.personalNumber
            && this.gender === target.gender
            && this.legalEntityId === target.legalEntityId;
    }
}

