import { UserProfileType } from './user-profile-type.model';
import { UserProfileLanguage } from './user-profile-language.model';
import { UserProfileSalutation } from './user-profile-salutation.model';

export interface UpdateProfileModel {
    id: number;
    salutationId: number;
    userName: string;
    surName: string;
    firstName: string;
    email: string;
    phoneNumber: string;
    languageId: number;
    hourlyRate: string;
    accessUrl: string;
    typeId?: string;
}

export class UserProfile {
    id: number;
    vinCall: string;
    license: string;
    userName: string;
    surName: string;
    firstName: string;
    salutations: UserProfileSalutation[] = [];
    salutationId: number;
    email: string;
    phoneNumber: string;
    languages: UserProfileLanguage[] = [];
    languageId: number;
    types: UserProfileType[] = [];
    typeId: number;
    accessUrl: string;
    vatConfirm: boolean;
    emailConfirmation: boolean;
    hourlyRate: number;
    userType: string;

    constructor(data?: any) {
        if (data) {
            this.id = data.id;
            this.vinCall = data.vinCall;
            this.license = data.license;
            this.userName = data.userName;
            this.surName = data.surName;
            this.firstName = data.firstName;
            this.salutations = this.mappingSalutions(data.salutations);
            this.salutationId = data.salutationId;
            this.email = data.email;
            this.phoneNumber = data.phoneNumber;
            this.languages = data.languages;
            this.languageId = data.languageId;
            this.types = this.mappingTypes(data.types);
            this.typeId = data.typeId;
            this.accessUrl = data.accessUrl;
            this.vatConfirm = data.vatConfirm;
            this.emailConfirmation = data.emailConfirmation;
            this.hourlyRate = data.hourlyRate;
            this.userType = data.userType;
        }
    }

    mappingSalutions(salutations: UserProfileSalutation[]) {
        return salutations && salutations.map(salutation => new UserProfileSalutation(salutation));
    }

    mappingLanguages(languages: UserProfileLanguage[]) {
        return languages && languages.map(language => new UserProfileLanguage(language));
    }

    mappingTypes(types: UserProfileType[]) {
        return types && types.map(type => new UserProfileType(type));
    }

    toUpdateProfileRequest(request: any) {
        return {
            id: request.id,
            salutationId: request.salutationIds,
            userName: request.userName,
            surName: request.surName,
            firstName: request.firstName,
            email: request.email,
            phoneNumber: request.phoneNumber,
            languageId: request.languageId,
            hourlyRate: request.hourlyRate,
            accessUrl: request.accessUrl,
        } as UpdateProfileModel;
    }
}
