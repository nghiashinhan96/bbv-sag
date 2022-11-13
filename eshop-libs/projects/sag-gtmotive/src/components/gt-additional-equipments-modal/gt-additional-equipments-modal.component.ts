import { Component, Input, OnInit } from '@angular/core';
import { uniq } from 'lodash';
import { SagGtmotiveConfigService } from '../../services/gtmotive-config.service';

@Component({
    selector: 'sag-gtmotive-additional-equipments-modal',
    templateUrl: './gt-additional-equipments-modal.component.html'
})
export class SagGtmotiveAdditionalEquipmentsModalComponent implements OnInit {
    @Input() set additionalEquipments(value) {
        this.filterDateEquipments(value);
    }
    @Input() gtEquipments = [];
    @Input() filteredAdditionalEquips = [];
    @Input() dateEquipments = [];
    @Input() searchPartsWithNewEquips: any;
    @Input() selectDate: any;
    @Input() close: any;

    collectedAdditionalEquips = [];
    checkedEquipments = [];
    dateOptions: any = {
        showFooterToday: true,
        focusInputOnDateSelect: false
    };

    isShownEquipMessage: boolean;
    disabled = true;

    constructor(
        private config: SagGtmotiveConfigService,
    ) { }

    ngOnInit() {
        this.dateOptions.dateFormat = 'yyyy.mm.dd';
    }

    updateAdditionalEquips(event, equipOption) {
        const isExist = this.collectedAdditionalEquips.some(el =>
            equipOption.incompatibilityGroup &&
            el.incompatibilityGroup &&
            equipOption.incompatibilityGroup === el.incompatibilityGroup
        );

        if (isExist) {
            if (event.target.checked) {
                this.isShownEquipMessage = true;
                event.target.checked = false;
            } else {
                this.isShownEquipMessage = false;
                const index = this.collectedAdditionalEquips.indexOf(equipOption);
                if (index !== -1) {
                    this.collectedAdditionalEquips.splice(index, 1);
                }
            }
        } else {
            this.collectedAdditionalEquips.push(equipOption);
        }

        if (this.collectedAdditionalEquips.length) {
            this.disabled = false;
        } else {
            this.disabled = true;
        }
    }

    onSearchPartsWithNewEquips() {
        const equips = this.collectedAdditionalEquips.map(item => item.applicability.code);
        if (this.searchPartsWithNewEquips) {
            const spinner = this.config.spinner.start('sag-gtmotive-additional-equipments-modal');
            this.searchPartsWithNewEquips(equips, () => {
                this.config.spinner.stop(spinner);
            });
        }
    }

    onSelectDate(value) {
        if (this.selectDate) {
            this.selectDate(value);
        }
    }

    onClose() {
        if (this.close) {
            this.close();
        }
    }

    private filterDateEquipments(additionalEquipments: any[]) {
        if (!additionalEquipments || !additionalEquipments.length) {
            return;
        }

        additionalEquipments.forEach(family => {
            if (family.subFamilies && family.subFamilies.length) {
                family.subFamilies.forEach(subFamily => {
                    if (this.isDateEquipment(family.code, subFamily.code) && this.dateEquipments.indexOf(family) === -1) {
                        this.dateEquipments.push(family);
                    } else if (this.isValidEquipments(subFamily.equipmentOptions)) {
                        if (this.filteredAdditionalEquips.indexOf(family) === -1) {
                            this.filteredAdditionalEquips.push(family);
                        }

                        const checkedEquipments = this.findCheckedEquipments(this.gtEquipments, subFamily.equipmentOptions);
                        if (checkedEquipments.length) {
                            subFamily.equipmentOptions.forEach(opt => {
                                if (checkedEquipments.indexOf(opt.applicability.code) !== -1) {
                                    opt.checked = true;
                                    this.updateAdditionalEquips({ target: { checked: true } }, opt);
                                }
                            });
                            this.checkedEquipments = uniq([...this.checkedEquipments, ...checkedEquipments]);
                        }
                    }
                });
            }
        });
    }

    private findCheckedEquipments(vehicleEquipments: Array<any>, equipmentOptions: Array<any>) {
        const result = [];
        equipmentOptions.forEach(equip => {
            const equipmentCode = equip.applicability.code;
            if (vehicleEquipments.indexOf(equipmentCode) !== -1) {
                result.push(equipmentCode);
            }
        });
        return result;
    }

    private isDateEquipment(family: string, subFamily: string): boolean {
        return family === 'RNG' && subFamily === 'FEC';
    }

    private isValidEquipments(equipmentOptions: Array<any>) {
        return !equipmentOptions.some(equipmentOption => !equipmentOption.applicability);
    }
}
