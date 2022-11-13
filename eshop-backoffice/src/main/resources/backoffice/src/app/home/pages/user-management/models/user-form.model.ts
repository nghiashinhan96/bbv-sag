
import { ProfileInfoFormModel } from './profile-info-form.model';

export class UserFormModel {
    companyName: string;
    affiliateShortName: string;
    customerNumber: string;
    addressesLink: string;
    sendMethodCode: string;
    defaultBranchName: string;
    defaultBranchId: string;
    affiliate: string;
    nr: number;
    userProfileDto: ProfileInfoFormModel;

    isLoadingOptions: boolean;
    isLoadedOptions: boolean;
    isSaving: boolean;

    isShowing: boolean;

    constructor() {
        this.isShowing = true;
    }

    reload() {
        this.isShowing = false;
        setTimeout(() => {
            this.isShowing = true;
        });
    }

    onFetchedOtions() {
        this.isLoadingOptions = false;
        this.isLoadedOptions = true;
    }

    onFetchingOtions() {
        this.isLoadingOptions = true;
    }

    onSaving() {
        this.isSaving = true;
    }
    onSaved() {
        this.isSaving = false;
    }
}
