import { StringHelper } from 'src/app/core/utils/string.util';

export class TableUserDetailRow {

    public id: string;
    public affiliate: string;
    public companyName: string;
    public customerNumber: string;
    public username: string;
    public email: string;
    public telephone: string;
    public role: string;
    public status: string;
    public editBtn?: any;
    public deleteBtn?: any;

    constructor(data?) {
        if (!data) {
            this.editBtn = '';
            this.deleteBtn = '';
            return;
        }
        this.id = data.id;
        this.affiliate = data.affiliate;
        this.companyName = data.companyName;
        this.customerNumber = data.customerNumber;
        this.username = data.username;
        this.email = data.email;
        this.telephone = data.telephone;
        this.role = data.role;
        this.status = data.status;
        this.editBtn = data.editBtn;
        this.deleteBtn = data.deleteBtn;
    }

    public static createTranslatedRow(user, translateService) {
        const id = StringHelper.getEmptyStringIfNull(user.id);
        const companyName = StringHelper.getEmptyStringIfNull(user.organisationName);
        const customerNumber = StringHelper.getEmptyStringIfNull(user.organisationCode);
        const username = StringHelper.getEmptyStringIfNull(user.userName);
        const email = StringHelper.getEmptyStringIfNull(user.email);
        const telephone = StringHelper.getEmptyStringIfNull(user.telephone);
        const role = translateService.instant(
            'COMMON.LABEL.ROLE.' + StringHelper.getEmptyStringIfNull(user.roleName)
        );
        const affiliate = StringHelper.getEmptyStringIfNull(user.affiliate);
        status = '';
        if (user.isUserActive) {
            status = translateService.instant('COMMON.LABEL.ACTIVE');
        } else {
            status = translateService.instant('COMMON.LABEL.INACTIVE');
        }

        return new TableUserDetailRow({
            id,
            affiliate,
            companyName,
            customerNumber,
            username,
            email,
            telephone,
            role,
            status
        });
    }
}
