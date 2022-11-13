import { DesignationModel } from './designation.model';
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app-lang-code.constant';

export class MarginSupplierArtGroupModel {
    wssArtGrpTree: {
        artgrp: string,
        leafid: string,
        parentid: string,
        sort: string
    };
    wssDesignations: DesignationModel[];

    constructor(data = null) {
        if(data) {
            this.wssArtGrpTree = data.wssArtGrpTree;
            this.wssDesignations = data.wssDesignations;
        }
    }

    displayName(langCode = DEFAULT_LANG_CODE) {
        const currentLang = this.wssDesignations.find(designation => designation.language === langCode);

        if(currentLang) {
            return `${this.wssArtGrpTree.artgrp} - ${currentLang.text.replace(/"/gi, '')}`;
        }

        return null;
    }

    getCurrentName(langCode = DEFAULT_LANG_CODE) {
        const currentLang = this.wssDesignations.find(designation => designation.language === langCode);

        if(currentLang) {
            return `${currentLang.text.replace(/"/gi, '')}`;
        }

        return null;
    }
}