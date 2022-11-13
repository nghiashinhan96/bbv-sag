import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as moment_ from 'moment';
import { BsModalService } from 'ngx-bootstrap/modal';
import { of, Subject } from 'rxjs';
import { catchError, finalize, map, switchMap, tap } from 'rxjs/operators';
import { AffiliateEnum, AffiliateUtil, VEHICLE_CLASS } from 'sag-common';
import { SubSink } from 'subsink';
import { LIB_VEHICLE_MOTO_SEARCH_MAKE_MODEL, LIB_VEHICLE_SEARCH_FREETEXT } from '../../constant';
import { SagSearchVehicleDialogComponent } from '../../dialogs/all-vehicle-dialog/all-vehicle-dialog.component';
import { MakeModelSearchRequest } from '../../models/make-model-search-request.model';
import { VehicleSearchRequest } from '../../models/vehicle-search-request.model';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { ArticleSearchStorageService } from '../../services/article-search-storage.service';
import { ArticleSearchService } from '../../services/article-search.service';
const moment = moment_;

@Component({
    selector: 'sag-make-model-search',
    templateUrl: './make-model-search.component.html',
    styleUrls: ['./make-model-search.component.scss']
})
export class MakeModelSearchComponent implements OnInit, OnDestroy {
    @Input() customerNumber;
    @Output() searchEmitter = new EventEmitter();
    
    make$;
    cubicCapacity$;
    model$;
    yearFrom$;
    isLoadingMakeOptions = false;
    isLoadingCubicCapacityOptions = false;
    isLoadingModelOptions = false;
    isLoadingYearFromOptions = false;
    isModelInputInvalid = false;
    isCubicCapacitylInputInvalid = false;
    isFormYearInputInvalid = false;
    selectedBrandKey = '';
    selectedModelKey = '';
    selectedCubicCapacityKey = '';
    selectedYearFromKey = '';
    firstLoadedMake = true;
    firstLoadedCc = true;
    firstLoadedModel = true;
    firstLoadedYear = true;
    defaultVehicleType = VEHICLE_CLASS.MB;
    axCz = AffiliateEnum.AXCZ;
    ehaxcz = AffiliateEnum.EH_AX_CZ;
    ehcz = AffiliateEnum.EH_CZ;
    cz = AffiliateEnum.CZ;
    isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate);
    isAxCz = AffiliateUtil.isAxCz(this.config.affiliate);
    isCzBased = this.isCz || this.isAxCz;
    searchForm: FormGroup;
    errorMsg: string;
    private subs = new SubSink();
    isSearchingVehicleTypeCode = false;
    currentDisabledVehicleSubClass = '';
    isShowTypeCodeSearch = false;
    history: any;

    constructor(
        private fb: FormBuilder,
        private searchService: ArticleSearchService,
        private config: ArticleSearchConfigService,
        private modalService: BsModalService,
        private storage: ArticleSearchStorageService
    ) {}

    ngOnInit() {
        this.searchForm = this.fb.group({
            vehicleType: this.defaultVehicleType,
            road: true,
            mx: true,
            scooter: true,
            atv: true,
            makeCode: [null, Validators.required],
            modelCode: [null, Validators.required],
            yearFrom: [null, Validators.required],
            cubicCapacity: [null, Validators.required],
            vehicleCode: ['']
        });

        this.history = this.storage.makeModelTypeMotoHistory;
        if (this.history && this.history[this.customerNumber]) {
            const { road, mx, scooter, atv, makeCode, cubicCapacity, modelCode, yearFrom } = this.history[this.customerNumber];
            if (makeCode) {
                this.make$ = of([makeCode]);
            }
            if (cubicCapacity) {
                this.cubicCapacity$ = of([cubicCapacity]);
            }
            if (modelCode) {
                this.model$ = of([modelCode]);
            }
            if (yearFrom) {
                this.yearFrom$ = of([yearFrom]);
            }
            this.searchForm.patchValue({
                vehicleType: this.defaultVehicleType,
                road,
                mx,
                scooter,
                atv,
                makeCode: makeCode && makeCode.id,
                cubicCapacity: cubicCapacity && cubicCapacity.id,
                modelCode: modelCode && modelCode.id,
                yearFrom: yearFrom && yearFrom.id
            }, { emitEvent: false })
        } else {
            this.history = this.history || {};
        }

        this.subs.sink = this.searchForm.get('road').valueChanges.subscribe(value => {
            this.onMotorbikeSubClassChange();
            this.applyDisabledRuleVehicleSubClass(); 
        });
        this.subs.sink = this.searchForm.get('mx').valueChanges.subscribe(value => {
            this.onMotorbikeSubClassChange();
            this.applyDisabledRuleVehicleSubClass();
        });
        this.subs.sink = this.searchForm.get('scooter').valueChanges.subscribe(value => {
            this.onMotorbikeSubClassChange();
            this.applyDisabledRuleVehicleSubClass();
        });
        this.subs.sink = this.searchForm.get('atv').valueChanges.subscribe(value => {
            this.onMotorbikeSubClassChange();
            this.applyDisabledRuleVehicleSubClass();
        });

        this.subs.sink = this.searchForm.get('makeCode').valueChanges.subscribe(makeId => {
            this.resetCubicCapacity();
            this.resetModel();
            this.resetYearFrom();
            if (makeId) {
                this.cubicCapacity$ = this.searchCubicCapacity();
            }
        });

        this.subs.sink = this.searchForm.get('cubicCapacity').valueChanges.subscribe(cubicCapacity => {
            this.resetModel();
            this.resetYearFrom();
            if (cubicCapacity) {
                this.isCubicCapacitylInputInvalid = false;
                this.model$ = this.searchModel();
            }
        });

        this.subs.sink = this.searchForm.get('modelCode').valueChanges.subscribe(modelId => {
            this.resetYearFrom();
            if (modelId) {
                this.isModelInputInvalid = false;
                this.yearFrom$ = this.searchYearFrom();
            }
        });

        this.subs.sink = this.searchForm.get('yearFrom').valueChanges.subscribe(yearFrom => {
            if (yearFrom) {
                this.isFormYearInputInvalid = false;
            }
        });
    }

    private applyDisabledRuleVehicleSubClass() {
        const formValue = this.searchForm.getRawValue();
        const vehicleSubClassArray = [
            {
                name: 'road',
                value: formValue.road
            },
            {
                name: 'mx',
                value: formValue.mx
            },
            {
                name: 'scooter',
                value: formValue.scooter
            },
            {
                name: 'atv',
                value: formValue.atv
            }
        ];
        const filterResults = vehicleSubClassArray.filter(item => item.value === true);
        if (filterResults.length === 1) {
            const filterResult = filterResults[0];
            this.searchForm.get(filterResult.name).disable({emitEvent: false});
            this.currentDisabledVehicleSubClass = filterResult.name;
        } else if (this.currentDisabledVehicleSubClass !== '') {
            this.searchForm.get(this.currentDisabledVehicleSubClass).enable({emitEvent: false});
            this.currentDisabledVehicleSubClass = '';
        }
    }

    private onMotorbikeSubClassChange() {
        this.resetMakeCode();
        this.resetCubicCapacity();
        this.resetModel();
        this.resetYearFrom();
        this.make$ = this.loadMakeOptionsData();
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    searchData() {
        if (this.isCzBased || !this.isShowTypeCodeSearch) {
            this.checkFormValidation();
        } else {
            if ((this.searchForm.get('vehicleCode').value || '').trim()) {
                this.handleTheCaseThatFormIsValid(true);
            } else {
                this.checkFormValidation();
            }
        }
    }

    private checkFormValidation() {
        if (this.searchForm.invalid) {
            this.errorMsg = 'SEARCH.ERROR_MESSAGE.REQUIRED_FIELD';
            this.checkValidMakeModelTypeInput();
        } else {
            this.handleTheCaseThatFormIsValid();
        }
    }

    private handleTheCaseThatFormIsValid(isSearchByVehicleCode?: boolean) {
        const body = this.searchForm.getRawValue();
        this.setHistoryData('road', body.road);
        this.setHistoryData('mx', body.mx);
        this.setHistoryData('scooter', body.scooter);
        this.setHistoryData('atv', body.atv);
        this.storage.makeModelTypeMotoHistory = this.history;
        let searchType = '';
        if (isSearchByVehicleCode) {
            const queryParams = {
                size: 10,
                page: 0
            };
            searchType = LIB_VEHICLE_SEARCH_FREETEXT;
            this.isSearchingVehicleTypeCode = true;
            this.subs.sink = this.searchFreeText({vehicleCode: this.searchForm.get('vehicleCode').value}, queryParams).subscribe(res => {
                if (!!res) {
                    this.searchEmitter.emit({
                        searchType,
                        ...res
                    });
                }
            });
        } else {
            searchType = LIB_VEHICLE_MOTO_SEARCH_MAKE_MODEL;
            this.searchEmitter.emit({
                searchType,
                body,
                searchKey: this.generateSearchKey()
            });
        }
    }

    loadMakeOptions() {
        if (this.firstLoadedMake) {
            this.make$ = this.loadMakeOptionsData();
        }
    }

    loadCcOptions() {
        if (this.firstLoadedCc) {
            this.cubicCapacity$ = this.searchCubicCapacity();
        }
    }

    loadModelOptions() {
        if (this.firstLoadedModel) {
            this.model$ = this.searchModel();
        }
    }

    loadYearOptions() {
        if (this.firstLoadedYear) {
            this.yearFrom$ = this.searchYearFrom();
        }
    }

    private loadMakeOptionsData() {
        this.isLoadingMakeOptions = true;
        const { vehicleType, road, mx, scooter, atv } = this.searchForm.getRawValue();
        const data = new MakeModelSearchRequest({ vehicleType, road, mx, scooter, atv });
        return this.searchService.getMakeModelTypeData(data).pipe(
            finalize(() => {
                this.isLoadingMakeOptions = false;
                this.firstLoadedMake = false;
            }),
            map((res: any) => {
                const make = (res.MAKE || []).map(({ makeId, make }) => {
                    return { id: makeId, label: make };
                });
                make.sort((item1, item2) => {
                    if (item1.label < item2.label) { return -1; }
                    if (item1.label > item2.label) { return 1; }
                    return 0;
                });
                return make;
            }));
    }

    private searchCubicCapacity() {
        const formValue = this.searchForm.getRawValue();
        const data: MakeModelSearchRequest = new MakeModelSearchRequest(formValue);
        data.cubicCapacity = null;
        data.modelCode = null;
        this.isLoadingCubicCapacityOptions = true;
        return this.searchService.getMakeModelTypeData(data)
            .pipe(
                finalize(() => {
                    this.isLoadingCubicCapacityOptions = false;
                    this.firstLoadedCc = false;
                }),
                map((res: any) => {
                    const cubicCapacity = (res.CUBIC_CAPACITY || []).map(({ capacity }) => {
                        return { id: capacity, label: capacity };
                    });
                    return cubicCapacity;
                })
            );
    }

    private searchYearFrom() {
        const formValue = this.searchForm.getRawValue();
        const data: MakeModelSearchRequest = new MakeModelSearchRequest(formValue);
        this.isLoadingYearFromOptions = true;
        return this.searchService.getMakeModelTypeData(data)
            .pipe(
                finalize(() => {
                    this.isLoadingYearFromOptions = false;
                    this.firstLoadedYear = false;
                }),
                map((res: any) => {
                    const yearFrom = (res.YEAR || []).map(({ vehId , year }) => {
                        return { id: vehId, label: year };
                    });
                    return yearFrom;
                })
            );
    }

    private searchModel() {
        const formValue = this.searchForm.getRawValue();
        const data: MakeModelSearchRequest = new MakeModelSearchRequest(formValue);
        data.modelCode = null;
        this.isLoadingModelOptions = true;
        return this.searchService.getMakeModelTypeData(data)
            .pipe(
                finalize(() => {
                    this.isLoadingModelOptions = false;
                    this.firstLoadedModel = false;
                }),
                map((res: any) => {
                    const model = (res.MODEL || []).map(({ modelId, model, modelDateBegin, modelDateEnd }) => {
                        return { id: modelId, label: model };
                    });
                    return model;
                })
            );
    }

    private resetMakeCode() {
        this.searchForm.get('makeCode').setValue(null, { emitEvent: false });
    }

    private resetModel() {
        this.searchForm.get('modelCode').setValue(null, { emitEvent: false });
        this.model$ = of(null);
        this.isModelInputInvalid = false;
    }

    private resetCubicCapacity() {
        this.searchForm.get('cubicCapacity').setValue(null, { emitEvent: false });
        this.cubicCapacity$ = of(null);
        this.isCubicCapacitylInputInvalid = false;
    }

    private resetYearFrom() {
        this.searchForm.get('yearFrom').setValue(null, { emitEvent: false });
        this.yearFrom$ = of(null);
        this.isFormYearInputInvalid = false;
    }

    get makeCodeInput() {
        return this.searchForm && this.searchForm.get('makeCode');
    }

    get modelCodeInput() {
        return this.searchForm && this.searchForm.get('modelCode');
    }

    get cubicCapacityInput() {
        return this.searchForm && this.searchForm.get('cubicCapacity');
    }

    get yearFromInput() {
        return this.searchForm && this.searchForm.get('yearFrom');
    }

    onCubicCapacityChange(event) {
        this.selectedCubicCapacityKey = event && event.label || '';
        this.setHistoryData('cubicCapacity', event);
    }

    onBrandChange(event) {
        this.selectedBrandKey = event && event.label || '';
        this.setHistoryData('makeCode', event);
    }

    onModelCodeChange(event) {
        this.selectedModelKey = event && event.label || '';
        this.setHistoryData('modelCode', event);
    }

    onYearFromChange(event) {
        this.selectedYearFromKey = event && event.label || '';
        this.setHistoryData('yearFrom', event);
    }

    resetUnfocusInput(isCameFromVehicleCode?: boolean) {
        if (isCameFromVehicleCode) {
            const currentVehicleCode = this.searchForm.get('vehicleCode').value;
            this.searchForm.reset({
                vehicleType: this.defaultVehicleType,
                vehicleCode: currentVehicleCode,
                road: true,
                mx: true,
                scooter: true,
                atv: true
            }, { emitEvent: false });
            this.isModelInputInvalid = false;
            this.isCubicCapacitylInputInvalid = false;
            this.isFormYearInputInvalid = false;
        } else {
            this.searchForm.get('vehicleCode').reset('', { emitEvent: false });
        }
    }

    private checkValidMakeModelTypeInput() {
        if (this.makeCodeInput.value) {
            this.isModelInputInvalid = this.modelCodeInput.invalid;
            this.isCubicCapacitylInputInvalid = this.cubicCapacityInput.invalid;
            this.isFormYearInputInvalid = this.yearFromInput.invalid;
            return !this.isModelInputInvalid && !this.isCubicCapacitylInputInvalid && !this.isFormYearInputInvalid;
        }
        return true;
    }

    private getSearchFreeTextRes(vehicleId: string, info: string, totalElements: number) {
        return {
            analytic: {
                searchTerm: info,
                numberOfHits: totalElements
            },
            search: vehicleId,
            info
        };
    }

    private searchFreeText(requestBody, queryParams) {
        const info = requestBody.vehicleCode;
        const body: any = new VehicleSearchRequest().buildVehicleCodeRequest(requestBody);
        body.vehicleClass = VEHICLE_CLASS.MB;
        // Fahrzeugdaten accepts uppercase only
        return this.searchService.searchVehicles(body, queryParams).pipe(
            tap(() => this.isSearchingVehicleTypeCode = false),
            switchMap((res: any) => {
                if (res && res.vehicles && res.vehicles.content && res.vehicles.content.length > 1) {
                    // show popup of muitlpe
                    // return { swissPlate: info, vehicles: res.vehicles.content };
                    const obs = new Subject();
                    const modelRef = this.modalService.show(SagSearchVehicleDialogComponent, {
                        ignoreBackdropClick: true,
                        initialState: {
                            vehicles: res.vehicles.content
                        }
                    });
                    modelRef.content.closing = (vehicleId) => {
                        obs.next(this.getSearchFreeTextRes(vehicleId, info, res.vehicles.totalElements));
                        obs.complete();
                    };
                    return obs.asObservable();
                }
                if (res && res.vehicles && res.vehicles.content && res.vehicles.content.length > 0) {
                    const vehicleId = res.vehicles.content[0].vehid;
                    return of(this.getSearchFreeTextRes(vehicleId, info, res.vehicles.totalElements));
                } else {
                    return of(this.handleErrorMessage({ error: 404 }, info));
                }
            }),
            catchError(error => of(this.handleErrorMessage(error, info))));
    }

    private handleErrorMessage(error, search) {
        // turn off the spinner
        this.isSearchingVehicleTypeCode = false;
        let code = error.error_code || error.code || error.status;
        if (!code) {
            code = error.error;
        }
        switch (code) {
            case 404:
            case 400:
                this.errorMsg = 'SEARCH.ERROR_MESSAGE.VEHICLE_NOT_FOUND';
                break;
            case 'access_denied':
                this.errorMsg = 'SEARCH.ERROR_MESSAGE.PERMISSION';
                break;
            case 'NOT_FOUND_VEHICLE':
                this.errorMsg = 'SEARCH.VIN_ERROR_MSG.NOT_FOUND_VEHICLE';
                break;
            default:
                this.errorMsg = 'ERROR_500';
        }
        return null;
    }

    private generateSearchKey() {
        const data = this.history && this.history[this.customerNumber] || {};
        const { makeCode, cubicCapacity, modelCode, yearFrom } = data;
        const make = this.selectedBrandKey || (makeCode && makeCode.label) || '';
        const cc = this.selectedCubicCapacityKey || (cubicCapacity && cubicCapacity.label) || '';
        const model = this.selectedModelKey || (modelCode && modelCode.label) || '';
        const year = this.selectedYearFromKey || (yearFrom && yearFrom.label) || '';
        return JSON.stringify([make, cc, model, year]);
    }

    private setHistoryData(key, value) {
        if (!this.history[this.customerNumber]) {
            this.history[this.customerNumber] = {};
        }
        this.history[this.customerNumber][key] = value;
    }
}
