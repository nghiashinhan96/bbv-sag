import { Injectable } from '@angular/core';

import { isEmpty } from 'lodash'
import { TranslateService } from '@ngx-translate/core';

import { messageLocationTypeEnum, messageTypeEnum } from '../../../../core/enums/enums';
import { StringHelper } from 'src/app/core/utils/string.util';

/**
 * This service handle bussiness logic for message-creating component
 * It just should be used inside component, please do not use it outside the component
 * Purpose to reduce complicated logic of the the component and easier to write UT
 */
@Injectable()
export class MessageSavingService {

    constructor(
        private translateService: TranslateService
    ) { }

    // This part preparing options for selecting
    public getLocationTypes(locationTypesInput: Array<any>) {
        const locationTypes: Array<any> = [];
        if (isEmpty(locationTypesInput)) {
            return locationTypes;
        }
        locationTypesInput.forEach(item => {
            locationTypes.push({ value: item.id.toString(), label: this.getShowedText(item.locationType), roleTypes: item.roleTypes });
        });
        return locationTypes;
    }

    public getSupportedAffiliates(supportedAffiliatesInput: Array<any>) {
        const supportedAffiliates: Array<any> = [];
        if (isEmpty(supportedAffiliatesInput)) {
            return supportedAffiliates;
        }
        supportedAffiliatesInput.forEach(item => {
            supportedAffiliates.push({ selected: false, value: item.shortName.toString(), label: item.companyName });
        });
        return supportedAffiliates;
    }

    public getRoleTypes(roleTypesInput: Array<any>) {
        const roleTypes: Array<any> = [];
        if (isEmpty(roleTypesInput)) {
            return roleTypes;
        }
        roleTypesInput.forEach(item => {
            roleTypes.push({ value: item.id.toString(), label: this.getShowedText(item.roleType), accessRights: item.accessRights });
        });
        return roleTypes;
    }

    public getUserGroupRole(userGroupsInput: Array<any>) {
        const userGroups: Array<any> = [];
        if (isEmpty(userGroupsInput)) {
            return userGroups;
        }
        userGroupsInput.forEach(item => {
            userGroups.push({ value: item.id.toString(), label: this.getShowedText(item.userGroupKey), areas: item.areas });
        });
        return userGroups;
    }

    public getTypes(typesInput: Array<any>) {
        const types: Array<any> = [];
        if (isEmpty(typesInput)) {
            return typesInput;
        }
        typesInput.forEach(item => {
            types.push({ value: item.id.toString(), label: this.getShowedText(item.type) });
        });
        return types;
    }

    public getVisibilities(visibilitiesInput: Array<any>) {
        const visibilities: Array<any> = [];
        if (isEmpty(visibilitiesInput)) {
            return visibilities;
        }
        visibilitiesInput.forEach(item => {
            visibilities.push({ value: item.id.toString(), label: this.getShowedText(item.visibility) });
        });
        return visibilities;
    }

    public getStyles(stylesInput: Array<any>) {
        const styles: Array<any> = [];
        if (isEmpty(stylesInput)) {
            return styles;
        }
        stylesInput.forEach(item => {
            styles.push({ value: item.id.toString(), label: this.getShowedText(item.style) });
        });
        return styles;
    }

    public getAreas(areasInput: Array<any>) {
        const areas: Array<any> = [];
        if (isEmpty(areasInput)) {
            return areas;
        }
        areasInput.forEach(item => {
            areas.push({ value: item.id.toString(), label: this.getShowedText(item.area), area: item.area, subAreas: item.subAreas });
        });
        return areas;
    }

    public getSubAreas(areas: Array<any>, selectedAreaId: number) {
        const subAreas: Array<any> = [];
        const selectedArea = areas.find(item => item.value == selectedAreaId);
        selectedArea.subAreas.forEach(item => {
            subAreas.push({ value: item.id.toString(), label: this.getShowedText(item.subArea) });
        });
        return subAreas;
    }

    public isSelectedAffiliateAsLocationType(masterData: any, locationId: number) {
        return this.isSelectedLocationOption(masterData, locationId, messageLocationTypeEnum.AFFILIATE.toString());
    }

    public isSelectedCustomerAsLocationType(masterData: any, locationId: number) {
        return this.isSelectedLocationOption(masterData, locationId, messageLocationTypeEnum.CUSTOMER.toString());
    }

    public isSelectedPanelAsBlockType(masterData: any, typeId: number) {
        return masterData
            && masterData.types
            && masterData.types.find(item => (item.id == typeId) && (item.type === messageTypeEnum.PANEL.toString()));
    }

    public isSelectedRoleHasMoreThanOneGroup(groups: Array<any>) {
        return groups && groups.length && groups.length > 1;
    }

    public isSelectedAreaHasMoreThanOneSubArea(subAreas: Array<any>) {
        return subAreas && subAreas.length && subAreas.length > 1;
    }

    // this part for preparing data for saving
    public getLocationValue(masterData: any, model: any, locationTypeId: number) {
        const selectedLocationType = masterData.locationTypes.find(item => item.id == locationTypeId);
        switch (selectedLocationType.locationType) {
            case messageLocationTypeEnum.AFFILIATE.toString():
                return model.affiliateShortName;
            case messageLocationTypeEnum.CUSTOMER.toString():
                return StringHelper.removeNonDigits(model.customerNr);
            default:
                return null;
        }
    }

    public getAccessRightId(roleTypes: Array<any>, model: any, userTypeId: number) {
        const selectedUserType = roleTypes.find(item => item.value == userTypeId);
        if (selectedUserType && selectedUserType.accessRights && selectedUserType.accessRights.length === 1) {
            return selectedUserType.accessRights[0].id.toString();
        }
        return model.accessRightId;
    }

    public getSubAreaId(masterData: any, model: any, areaId: number, isSelectedBanner: boolean) {
        // get all areas and not depends on user group
        const areas: Array<any> = [];
        masterData.locationTypes.forEach(location => {
            location.roleTypes.forEach(type => {
                type.accessRights.forEach(accessRight => {
                    accessRight.areas.forEach(area => {
                        if (!(areas && areas.length && areas.find(item => item.area == area.area))) {
                            areas.push({ id: area.id.toString(), area: area.area, subAreas: area.subAreas });
                        }
                    });
                });
            });
        });

        const selectedArea = isSelectedBanner ? areas.find(item => item.area == 'ALL') : areas.find(item => item.id == areaId);
        if (selectedArea && selectedArea.subAreas && selectedArea.subAreas.length === 1) {
            return selectedArea.subAreas[0].id.toString();
        }
        return model.subAreaId;
    }

    public getLabelFromValue(value: number, formList: Array<any> = []): any {
        const foundItem = formList.find(item => item.value == value);
        return foundItem ? foundItem.label : '';
    }

    private isSelectedLocationOption(masterData: any, locationId: number, locationType: string) {
        return masterData
            && masterData.locationTypes
            && masterData.locationTypes.find(item => (item.id == locationId) && (item.locationType === locationType));
    }

    private getShowedText(key: string) {
        return this.translateService.instant('MESSAGE.' + key);
    }
}
