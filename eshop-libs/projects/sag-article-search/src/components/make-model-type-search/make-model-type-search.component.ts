import { Component, OnInit, Output, EventEmitter, ViewChild, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { of } from 'rxjs';
import { yearValidator } from '../../services/article-search.validator';
import { map } from 'rxjs/internal/operators/map';
import { debounceTime, finalize } from 'rxjs/operators';
import * as moment_ from 'moment';
import { ArticleSearchService } from '../../services/article-search.service';
import { AffiliateEnum, AffiliateUtil, ProjectId, VEHICLE_CLASS } from 'sag-common';
import { NgSelectComponent } from '@ng-select/ng-select';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { ArticleSearchStorageService } from '../../services/article-search-storage.service';
const moment = moment_;
@Component({
    selector: 'sag-search-make-model-type',
    templateUrl: './make-model-type-search.component.html',
    styleUrls: ['./make-model-type-search.component.scss']
})
export class SagSearchMakeModelTypeComponent implements OnInit, OnChanges {
    @Input() customerNumber;
    @Output() searchEmitter = new EventEmitter();
    @Output() focusEmitter = new EventEmitter();

    @ViewChild('modelCodeControl', { static: false }) modelCodeControl: NgSelectComponent;
    @ViewChild('fuelTypeControl', { static: false }) fuelTypeControl: NgSelectComponent;
    @ViewChild('typeCodeControl', { static: false }) typeCodeControl: NgSelectComponent;

    make$;
    model$;
    type$;
    fuel$;

    firstLoadedMake = true;
    firstLoadedModel = true;
    firstLoadedFuel = true;
    firstLoadedType = true;

    isLoadingMakeOptions = false;
    isLoadingModelOptions = false;
    isLoadingTypeOptions = false;
    isModelInputInvalid = false;
    isTypeInputInvalid = false;

    selectedBrandKey = '';
    selectedModelKey = '';
    selectedFuelKey = '';
    selectedTypeKey = '';

    searchForm: FormGroup;
    errorMessage: string;

    cz = AffiliateEnum.CZ;
    ehcz = AffiliateEnum.EH_CZ;
    sb = AffiliateEnum.SB;
    isSb = AffiliateUtil.isSb(this.config.affiliate);
    axcz = AffiliateEnum.AXCZ;
    isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate);
    isAxCz = AffiliateUtil.isAxCz(this.config.affiliate);
    isCzBased = this.isCz || this.isAxCz;
    ehaxcz = AffiliateEnum.EH_AX_CZ;
    isCH = AffiliateUtil.isAffiliateCH(this.config.affiliate);
    isAT = AffiliateUtil.isAffiliateAT(this.config.affiliate);
    isAffiliateApplyMotorbikeShop = AffiliateUtil.isAffiliateApplyMotorbikeShop(this.config.affiliate);
    projectId = ProjectId.CONNECT;
    PROJECT_ID = ProjectId;
    history: any;

    private readonly MODEL_MAX_LENGTH = 90;
    private readonly TYPE_MAX_LENGTH = 120;

    constructor(
        private fb: FormBuilder,
        private searchService: ArticleSearchService,
        private config: ArticleSearchConfigService,
        private storage: ArticleSearchStorageService
    ) {
        this.projectId = this.config.projectId;
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.customerNumber && !changes.customerNumber.firstChange) {
            this.initSearchHistory();
        }
    }

    ngOnInit() {
        this.searchForm = this.fb.group({
            vehicleType: VEHICLE_CLASS.PC,
            makeCode: [null, Validators.required],
            modelCode: [null, Validators.required],
            typeCode: [null, Validators.required],
            fuelType: null,
            yearFrom: ['', yearValidator]
        });

        this.initSearchHistory();

        this.searchForm.get('vehicleType').valueChanges.subscribe(makeId => {
            this.resetMakeCode();
            this.resetYear();
            this.resetModel();
            this.resetFuel();
            this.resetType();
            this.make$ = this.loadMakeOptionsData();
            this.onFocus();
        });

        this.searchForm.get('makeCode').valueChanges.subscribe(makeId => {
            this.resetYear();
            this.resetModel();
            this.resetFuel();
            this.resetType();
            if (makeId) {
                this.model$ = this.searchModel();
                this.openSelectControl(this.modelCodeControl);
            }
        });

        this.searchForm.get('yearFrom').valueChanges
            .pipe(debounceTime(600))
            .subscribe(year => {
                this.setHistoryData('yearFrom', year);
                this.resetModel();
                this.resetFuel();
                this.resetType();
                this.model$ = this.searchModel();
            });

        this.searchForm.get('modelCode').valueChanges.subscribe(modelId => {
            this.resetFuel();
            this.resetType();
            if (modelId) {
                this.isModelInputInvalid = false;
                this.type$ = this.searchFuelAndType();
                this.openSelectControl(this.fuelTypeControl);
            }
        });

        this.searchForm.get('fuelType').valueChanges.subscribe(fuel => {
            this.resetType();
            this.type$ = this.searchType();
            if (fuel) {
                this.openSelectControl(this.typeCodeControl);
            }
        });

        this.typeCodeInput.valueChanges.subscribe(type => {
            if (type) {
                this.isTypeInputInvalid = false;
            }
        })
    }

    initSearchHistory() {
        this.history = this.storage.makeModelTypeCarsHistory;
        if (this.history && this.history[this.customerNumber]) {
            const { makeCode, yearFrom, modelCode, fuelType, typeCode } = this.history[this.customerNumber];
            if (makeCode) {
                this.make$ = of([makeCode]);
            }
            if (modelCode) {
                this.model$ = of([modelCode]);
            }
            if (fuelType) {
                this.fuel$ = of([fuelType]);
            }
            if (typeCode) {
                this.type$ = of([typeCode]);
            }
            this.searchForm.patchValue({
                vehicleType: VEHICLE_CLASS.PC,
                makeCode: makeCode && makeCode.id,
                yearFrom,
                modelCode: modelCode && modelCode.id,
                fuelType: fuelType && fuelType.id,
                typeCode: typeCode && typeCode.id
            }, { emitEvent: false });
        } else {
            this.history = this.history || {};
            this.searchForm.patchValue({
                vehicleType: VEHICLE_CLASS.PC,
                makeCode: null,
                yearFrom: null,
                modelCode: null,
                fuelType: null,
                typeCode: null
            }, { emitEvent: false });
        }
    }

    checkValidMakeModelTypeInput() {
        if (this.makeCodeInput.value) {
            this.isModelInputInvalid = this.modelCodeInput.invalid;
            this.isTypeInputInvalid = this.typeCodeInput.invalid;
            return !this.isModelInputInvalid && !this.isTypeInputInvalid;
        }
        return true;
    }

    seachData() {
        if (this.isValid(this.searchForm) && this.checkValidMakeModelTypeInput()) {
            const body = this.searchForm.value;
            this.storage.makeModelTypeCarsHistory = this.history;
            this.searchEmitter.emit({body, searchKey: this.generateSearchKey()});
        }

    }

    generateSearchKey() {
        const data = this.history && this.history[this.customerNumber] || {};
        const { makeCode, modelCode, fuelType, typeCode } = data;
        const make = this.selectedBrandKey || (makeCode && makeCode.label) || '';
        const model = this.selectedModelKey || (modelCode && modelCode.label) || '';
        const fuel = this.selectedFuelKey || (fuelType && fuelType.label) || '';
        const type = this.selectedTypeKey || (typeCode && typeCode.label) || '';
        return JSON.stringify([make, model, fuel, type]);
    }

    loadMakeOptions() {
        if (this.firstLoadedMake) {
            this.make$ = this.loadMakeOptionsData();
        }
        this.onFocus();
    }

    loadModelOptions() {
        if (this.firstLoadedModel) {
            this.model$ = this.searchModel();
        }
    }

    loadFuelOptions() {
        if (this.firstLoadedFuel) {
            this.fuel$ = this.searchFuelAndType();
        }
    }

    loadTypeOptions() {
        if (this.firstLoadedType) {
            this.type$ = this.searchType();
        }
    }

    onFocus() {
        this.focusEmitter.emit();
    }

    private loadMakeOptionsData() {
        this.isLoadingMakeOptions = true;
        const { vehicleType, yearFrom } = this.searchForm.value;
        return this.searchService.getMakeModelTypeData({ vehicleType, yearFrom }).pipe(
            finalize(() => {
                this.isLoadingMakeOptions = false;
                this.firstLoadedMake = false;
            }),
            map((res: any) => {
                let notAddedGapYet = true;
                // res required sorted by top attribute and alpha
                return (res.MAKE || []).map(({ makeId, make, top }) => {
                    return { id: makeId, label: make, top };
                }).reduce((result: any[], item: any) => {
                    if (item.top) {
                        result.push(item);
                    } else if (notAddedGapYet) {
                        notAddedGapYet = false;
                        // if have top list
                        if (result.length > 0) {
                            result.push({
                                id: 'gap',
                                label: '',
                                disabled: true,
                                top: false
                            });
                        }
                        result.push(item);
                    } else {
                        result.push(item);
                    }
                    return result;
                }, []);
            }));
    }

    get yearControl() {
        return this.searchForm.get('yearFrom');
    }

    get modelCodeInput() {
        return this.searchForm && this.searchForm.get('modelCode');
    }

    get fuelTypeInput() {
        return this.searchForm && this.searchForm.get('fuelType');
    }

    get typeCodeInput() {
        return this.searchForm && this.searchForm.get('typeCode');
    }

    get makeCodeInput() {
        return this.searchForm && this.searchForm.get('makeCode');
    }

    resetForm() {
        this.searchForm.reset({ vehicleType: 'pc' }, { emitEvent: false });
        this.isModelInputInvalid = false;
        this.isTypeInputInvalid = false;
    }

    private searchModel() {
        const data = { ...this.searchForm.value };
        this.isLoadingModelOptions = true;
        data.modelCode = null;
        data.fuelType = null;
        data.typeCode = null;
        return this.searchService.getMakeModelTypeData(data)
            .pipe(
                finalize(() => {
                    this.isLoadingModelOptions = false;
                    this.firstLoadedModel = false;
                }),
                map((res: any) => {
                    return (res.MODEL || []).map(({ modelId, model, modelDateBegin, modelDateEnd }) => {
                        const begin = moment(modelDateBegin, 'YYYYMM').format('MM/YYYY');
                        const end = modelDateEnd ? moment(modelDateEnd, 'YYYYMM').format('MM/YYYY') : '';
                        let label = `${model} [${begin} - ${end}]`;
                        if (label.length > this.MODEL_MAX_LENGTH) {
                            label = label.substring(0, this.MODEL_MAX_LENGTH) + '...';
                        }
                        return { id: modelId, label };
                    });
                })
            );
    }

    private searchFuelAndType() {
        this.isLoadingTypeOptions = true;
        const data = { ...this.searchForm.value };
        data.fuelType = null;
        data.typeCode = null;
        return this.searchService.getMakeModelTypeData(data)
            .pipe(
                finalize(() => {
                    this.isLoadingTypeOptions = false;
                    this.firstLoadedFuel = false;
                }),
                map((res: any) => {
                    const type = this.mapTypeList(res);
                    const fuel = (res.FUEL || []).map((item) => {
                        return { id: item, label: item };
                    });
                    this.fuel$ = of(fuel);
                    return type;
                })
            );
    }

    private searchType() {
        this.isLoadingTypeOptions = true;
        const data = { ...this.searchForm.value };
        data.typeCode = null;
        return this.searchService.getMakeModelTypeData(data)
            .pipe(
                finalize(() => {
                    this.isLoadingTypeOptions = false;
                    this.firstLoadedType = false;
                }),
                map((res: any) => {
                    return this.mapTypeList(res);
                })
            );
    }

    private resetModel() {
        this.searchForm.get('modelCode').setValue(null, { emitEvent: false });
        this.model$ = of(null);
        this.isModelInputInvalid = false;
    }

    private resetType() {
        this.searchForm.get('typeCode').setValue(null, { emitEvent: false });
        this.type$ = of(null);
        this.isTypeInputInvalid = false;
    }

    private resetFuel() {
        this.searchForm.get('fuelType').setValue(null, { emitEvent: false });
        this.fuel$ = of(null);
    }

    private resetYear() {
        this.searchForm.get('yearFrom').setValue('', { emitEvent: false });
    }

    private resetMakeCode() {
        this.searchForm.get('makeCode').setValue(null, { emitEvent: false });
    }

    private isValid(dataForm: FormGroup) {
        let focusKey = '';
        if (dataForm.invalid) {
            Object.keys(dataForm.controls).forEach(key => {
                const control = dataForm.get(key);
                if (control.invalid && !focusKey) {
                    focusKey = key;
                }
                control.markAsDirty();
                control.markAsTouched();
            });
            if (focusKey) {
                document.querySelector('[formcontrolname=' + focusKey + ']').scrollIntoView(
                    {
                        behavior: 'smooth',
                        block: 'center',
                        inline: 'center'
                    }
                );
            }
            return false;
        }
        return true;
    }

    openSelectControl(control: NgSelectComponent) {
        if ((this.isCzBased || this.isSb) && control) {
            control.open();
        }
    }

    handleKeydown = (event) => {
        if (event.keyCode === 13) {
            const sub = this.searchForm.valueChanges.subscribe(() => {
                this.seachData();
                sub.unsubscribe();
            });
        }
    }

    onFuelChange(event) {
        this.selectedFuelKey = event && event.label || '';
        this.setHistoryData('fuelType', event);
    }

    onBrandChange(event) {
        this.selectedBrandKey = event && event.label || '';
        this.setHistoryData('makeCode', event);
    }

    onModelCodeChange(event) {
        this.selectedModelKey = event && event.label || '';
        this.setHistoryData('modelCode', event);
    }

    onTypeChange(event) {
        this.selectedTypeKey = event && event.label || '';
        this.setHistoryData('typeCode', event);
    }

    private mapTypeList(res) {
        return (res && res.TYPE || []).map(({ vehId, vehicleName, vehiclePowerKw, vehicleEngineCode, vehicleCapacityCcTech, vehiclePowerHp }) => {
            let label = `${vehicleName} ${vehiclePowerKw} KW ${vehicleEngineCode}`;
            let infos = [];
            let infosText: string;
            if (vehicleCapacityCcTech) {
                infos.push(`${vehicleCapacityCcTech}cc`);
            }
            if (vehiclePowerHp) {
                infos.push(`${vehiclePowerHp} PS`);
            }
            infosText = ` (${infos.join(' / ')})`;
            if (label.length > this.TYPE_MAX_LENGTH) {
                label = label.substring(0, this.TYPE_MAX_LENGTH) + '...';
            }
            return { id: vehId, label, infosText };
        });
    }

    private setHistoryData(key, value) {
        if (!this.history[this.customerNumber]) {
            this.history[this.customerNumber] = {};
        }
        this.history[this.customerNumber][key] = value;
    }
}
