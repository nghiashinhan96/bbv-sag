import { TableCustomerDto } from './table-customer-dto';
import { BaseModel } from 'src/app/shared/models/base.model';
import { StringHelper } from 'src/app/core/utils/string.util';

export class CustomerRequestModel extends BaseModel {

    searchMode: string;
    customerNr: string;
    affiliate = '';
    companyName: string;
    sorting?: CustomerRequestModelSorting;

    page: number;
    size: number;

    constructor(data?: any) {
        super(data);
    }

    public static bindColumnFilterToModel(
        columnName,
        filterString,
        customerRequestModel: CustomerRequestModel
    ) {
        switch (columnName) {
            case TableCustomerDto.COLUMNS_KEY_NAME[0]:
                customerRequestModel.customerNr = filterString;
                break;
            case TableCustomerDto.COLUMNS_KEY_NAME[1]:
                customerRequestModel.affiliate = filterString;
                break;
            case TableCustomerDto.COLUMNS_KEY_NAME[2]:
                customerRequestModel.companyName = filterString;
                break;
            default:
                break;
        }
        return customerRequestModel;
    }

    public static bindModelToFilterColumn(
        column,
        userRequestModel: CustomerRequestModel
    ) {
        switch (column.name) {
            case TableCustomerDto.COLUMNS_KEY_NAME[0]:
                column.filtering.filterString = StringHelper.getEmptyStringIfNull(
                    userRequestModel.customerNr
                );
                break;
            case TableCustomerDto.COLUMNS_KEY_NAME[1]:
                column.filtering.filterString = StringHelper.getEmptyStringIfNull(
                    userRequestModel.affiliate
                );
                break;
            case TableCustomerDto.COLUMNS_KEY_NAME[2]:
                column.filtering.filterString = StringHelper.getEmptyStringIfNull(
                    userRequestModel.companyName
                );
                break;
            default:
                break;
        }
        return column;
    }
}

export class CustomerRequestModelSorting extends BaseModel {
    orderByOrganisationNameDesc: boolean;
    orderByCustomerNumberDesc: boolean;
    orderByAffiliateNameDesc: boolean;

    constructor(data?: any) {
        super(data);
    }

    public static matchColumnAndSort(
        userRequestModelSorting: CustomerRequestModelSorting,
        columnName,
        sortDescending
    ) {
        switch (columnName) {
            case TableCustomerDto.COLUMNS_KEY_NAME[0]:
                userRequestModelSorting.orderByCustomerNumberDesc = sortDescending;
                break;
            case TableCustomerDto.COLUMNS_KEY_NAME[1]:
                userRequestModelSorting.orderByAffiliateNameDesc = sortDescending;
                break;
            case TableCustomerDto.COLUMNS_KEY_NAME[2]:
                userRequestModelSorting.orderByOrganisationNameDesc = sortDescending;
                break;
            default:
                break;
        }
        return userRequestModelSorting;
    }
}
