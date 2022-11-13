
export class SalesManagementConstant {
    public static readonly URL_SALES_LIST_PAGE = '/home/sales';
    public static readonly URL_SALES_CREATING_PAGE = '/home/sales/create';
    public static readonly URL_SALES_EDITING_PAGE = '/home/sales/edit';
    public static readonly SORT_FIELD_MAP = {
        personalNumber: 'orderDescByPersonalNumber',
        firstName: 'orderDescByFirstName',
        lastName: 'orderDescByLastName',
        primaryContactEmail: 'orderDescByPrimaryContactEmail',
        legalEntityId: 'orderDescBylegalEntityId',
        genderOptions: 'orderDescByGender'
    };
}
