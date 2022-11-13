import { DesignationModel } from "./designation.model";
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app-lang-code.constant';
import { MARGIN_VALUES } from "../enums/margin-mgt.enum";

export class MarginArticleGroupModel {
    childMarginArticleGroups: MarginArticleGroupModel[] = [];
    customArticleGroup: string;
    customArticleGroupDesc: string;
    default: boolean;
    hasChild: boolean;
    id: number;
    leafId: string;
    mapped: boolean;
    margin1: number;
    margin2: number;
    margin3: number;
    margin4: number;
    margin5: number;
    margin6: number;
    margin7: number;
    orgId: number;
    parentLeafId: string;
    sagArticleGroup: string;
    sagArticleGroupDesc: DesignationModel[] = [];
    isMapped: boolean;
    isDefault: boolean;
    sagArticleGroupDescDisplay: string;
    root: boolean;

    parentLeafIdNumber: number;
    leafIdNumber: number;

    isDoneAppendChilds: boolean = false;

    constructor (data = null) {
        if (data) {
            Object.keys(data).forEach(key => {
                this[key] = data[key];
            });

            MARGIN_VALUES.forEach(margin => {
                this[`margin${margin}`] = (data[`margin${margin}`] !== 0 && !data[`margin${margin}`]) ? '' : data[`margin${margin}`];
            });

            this.customArticleGroup = data.customArticleGroup || '';
            this.customArticleGroupDesc = data.customArticleGroupDesc || '';

            if (data.childMarginArticleGroups) {
                this.childMarginArticleGroups = (data.childMarginArticleGroups || []).map(item => new MarginArticleGroupModel(item));
            }

            if (data.sagArticleGroupDesc) {
                this.sagArticleGroupDesc = (data.sagArticleGroupDesc || []).map(item => new DesignationModel(item));
            }

            if (data.parentLeafId) {
                this.parentLeafIdNumber = Number(data.parentLeafId);
            }

            if (data.leafId) {
                this.leafIdNumber = Number(data.leafId);
            }
        }
    }

    getSagArticleGroupDesc(language: string = DEFAULT_LANG_CODE) {
        if (this.sagArticleGroupDesc.length > 0) {
            const artGroupDes = this.sagArticleGroupDesc.find(des => des.language === language);
            if (artGroupDes) {
                return artGroupDes.text.replace(/"/gi, '');
            }
        }

        return null;
    }

    getArtGrpDes(language: string = DEFAULT_LANG_CODE) {
        if (this.sagArticleGroupDesc.length > 0) {
            const artGroupDes = this.sagArticleGroupDesc.find(des => des.language === language);
            if (artGroupDes) {
                return `${this.leafId && this.leafId + ' - ' || ''}${artGroupDes.text.replace(/"/gi, '')}`;
            }
        }

        return null;
    }

    getCreateDto() {
        const { sagArticleGroup, sagArticleGroupDesc, customArticleGroup, customArticleGroupDesc, leafId, parentLeafId } = this;

        const dto = {
            sagArticleGroup,
            sagArticleGroupDesc,
            customArticleGroup,
            customArticleGroupDesc,
            leafId,
            parentLeafId
        };

        MARGIN_VALUES.forEach(margin => {
            dto[`margin${margin}`] = Number(this[`margin${margin}`]);
        });

        return dto;
    }

    getUpdateDto() {
        const dto: any = { ...this };

        MARGIN_VALUES.forEach(margin => {
            dto[`margin${margin}`] = Number(this[`margin${margin}`]);
        });

        delete dto.sagArticleGroupDescDisplay;

        return dto;
    }
}