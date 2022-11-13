import { GTMOTIVE_ERROR_MSGS } from './../../enums/gtmotive-error-msg.enums';
import { Component, OnInit, Output, EventEmitter, Input, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs/internal/Observable';
import { VehicleMake } from '../../models/vehicle-make.model';
import { VinPackage } from '../../models/vin-package.model';
import { ArticleSearchConfigService } from '../../services/article-search-config.service';
import { VehicleSearchRequest } from '../../models/vehicle-search-request.model';
import { tap } from 'rxjs/internal/operators/tap';
import { map } from 'rxjs/internal/operators/map';
import { catchError, finalize, switchMap } from 'rxjs/operators';
import {
    LIB_VEHICLE_SEARCH_FREETEXT,
    LIB_VEHICLE_SEARCH_DESC_YEAR,
    LIB_VEHICLE_SEARCH_VIN,
    LIB_VEHICLE_SEARCH_VIN_LENGTH,
    LIB_VEHICLE_VIN_10_NAME,
    LIB_VEHICLE_VIN_20_NAME,
    LIB_VEHICLE_SEARCH_MAKE_MODEL_TYPE
} from '../../constant';
import { VinSecurityRequest } from '../../models/vin-security-request.model';

import { Subject, of } from 'rxjs';
import { yearValidator, requireVehicleNameValidator } from '../../services/article-search.validator';
import { TranslateService } from '@ngx-translate/core';
import { AffiliateEnum, AffiliateUtil, ProjectId } from 'sag-common';
import { ArticleSearchService } from '../../services/article-search.service';
import { SagSearchVehicleDialogComponent } from '../../dialogs/all-vehicle-dialog/all-vehicle-dialog.component';
import { SagSearchMakeModelTypeComponent } from '../make-model-type-search/make-model-type-search.component';
import { VIN_MODE } from '../../enums/article-search.enums';
import { BsModalService } from 'ngx-bootstrap/modal';

@Component({
    selector: 'sag-search-vehicle',
    templateUrl: './vehicle-search.component.html',
    styleUrls: ['./vehicle-search.component.scss']
})
export class SagSearchVehicleComponent implements OnInit, AfterViewInit {
    @Input() isFinalUserRole;
    @Input() isSalesOnBeHalf: boolean;
    @Input() hasVinSearchPermission: boolean;
    @Input() dmsSearchQuery;
    @Input() set customerNumber(val: number) {
        this.custNr = val;
        this.vinLicenseAmount = null;
        if (val) {
            this.searchService.searchVinLincense(val).pipe(
                finalize(() => {
                    if (this.dmsSearchQuery) {
                        this.processDms(this.dmsSearchQuery);
                    }
                })).subscribe(vinLicenseAmount => {
                    this.vinLicenseAmount = vinLicenseAmount;
                    this.hasVinLicenseAmountForSearch = this.checkHasVinPermission();
                    if (!this.hasVinSearchPermission) {
                        return;
                    }
                    if (this.vinLicenseAmount || this.isSalesOnBeHalf) {
                        if (!this.isCzBased) {
                            this.vehicleSearchForm.get('vehicleVin').enable();
                            this.vehicleSearchForm.get('vinMode').enable();
                            this.fillVinSearchInputAutomatically();
                        }
                    }
                });
        }
    }
    @Input() language: string;
    @Input() isShownVehicleSearch = true;
    @Input() enableVin = true;
    @Input() autoFilledVinValue = '';
    @Input() isShownMakeModelTypeSearch: boolean = true;
    @Input() isShownSearchTerm: boolean = true;

    @Output() vehicleSearchEmitter = new EventEmitter();
    @Output() vinPackagePurchaseEmitter = new EventEmitter();
    @Output() vehicleSearchEventEmitter = new EventEmitter();

    @ViewChild('makeModelType', { static: false }) makeModelType: SagSearchMakeModelTypeComponent;

    isDmsVersion3: boolean;
    vinLicenseAmount = null;
    hasVinLicenseAmountForSearch: boolean;

    vehicleSearchForm: FormGroup;
    searchingOther = false;
    searchingVin = false;

    isATAffiliates: boolean;
    vehicleMakes: VehicleMake[] = [];

    errorMsg: string;
    inValidField = '';
    readonly VEHICLE_CODE_SEARCH = LIB_VEHICLE_SEARCH_FREETEXT;
    readonly VEHICLE_DESCRIPTION_SEARCH = LIB_VEHICLE_SEARCH_DESC_YEAR;
    readonly VEHICLE_VIN_SEARCH = LIB_VEHICLE_SEARCH_VIN;
    readonly VEHICLE_MAKE_MODEL_TYPE_SEARCH = LIB_VEHICLE_SEARCH_MAKE_MODEL_TYPE;

    private selectedSearchType: string;
    private vinPackages: VinPackage[];

    isOpen = false;
    @ViewChild('topGroup', { static: true }) topGroup: ElementRef;

    isCH = AffiliateUtil.isAffiliateCH(this.config.affiliate);
    isAT = AffiliateUtil.isAffiliateAT(this.config.affiliate);
    cz = AffiliateEnum.CZ;
    axCz = AffiliateEnum.AXCZ;
    isCz = AffiliateUtil.isAffiliateCZ(this.config.affiliate);
    isAxCz = AffiliateUtil.isAxCz(this.config.affiliate);
    isCzBased = this.isCz || this.isAxCz;
    ehcz = AffiliateEnum.EH_CZ;
    ehaxcz = AffiliateEnum.EH_AX_CZ;
    tabIndexes: any = {};
    vinMode = VIN_MODE;
    sb = AffiliateEnum.SB;
    isSb = AffiliateUtil.isSb(this.config.affiliate);
    isAutonet: boolean = false;

    custNr: number;

    constructor(
        private fb: FormBuilder,
        private searchService: ArticleSearchService,
        private config: ArticleSearchConfigService,
        private modalService: BsModalService,
        private translateService: TranslateService
    ) {
        this.initForm();
    }

    ngOnInit() {
        if (this.isCzBased) {
            this.fillVinSearchInputAutomatically();
        }
        if (this.hasVinSearchPermission) {
            this.searchService.getVinPackages().subscribe(res => {
                this.vinPackages = [...res];
            }, err => {
                this.vinPackages = null;
            });
            this.searchService.getVehicleMakeList().subscribe(res => {
                this.vehicleMakes = [...res];
            }, error => this.handleErrorMessage(error, ''));
        }
        this.tabIndexes = this.getTabIndexes();
        this.isAutonet = this.config.projectId === ProjectId.AUTONET;
    }

    ngAfterViewInit(): void {
        // move the field to bottom
        if (this.isCH && this.topGroup) {
            this.topGroup.nativeElement.parentElement.appendChild(this.topGroup.nativeElement);
        }
    }

    onBuyVin() {
        if (!this.vinPackages) {
            this.vinPackagePurchaseEmitter.emit('');
            return;
        }
        const isAT = AffiliateUtil.isAffiliateAT(this.config.affiliate);
        const defaultVinPackageName = isAT ? LIB_VEHICLE_VIN_10_NAME : LIB_VEHICLE_VIN_20_NAME;
        const defualtVinPackage = this.vinPackages.find(vinPackage => vinPackage.packName.trim() === defaultVinPackageName);
        this.vinPackagePurchaseEmitter.emit(defualtVinPackage && defualtVinPackage.packId || '');
    }

    searchNonVin() {
        const requestBody = this.vehicleSearchForm.value;
        const key = Object.keys(requestBody).find(k => k !== 'vehicleVin' && !!(requestBody[k] || '').trim());

        let type: string;
        switch (key) {
            case 'vehicleCode':
                type = this.VEHICLE_CODE_SEARCH;
                break;
            case 'vehicleYear':
            case 'vehicleName':
                type = this.VEHICLE_DESCRIPTION_SEARCH;
                break;
            default:
                this.errorMsg = 'SEARCH.ERROR_MESSAGE.REQUIRED_FIELD';
                break;
        }
        if (type) {
            this.onSubmit(type);
        }
    }

    searchMakeModel(data) {
        this.vehicleSearchEmitter.emit({
            searchType: this.VEHICLE_MAKE_MODEL_TYPE_SEARCH,
            ...data
        });
    }

    onSubmit(searchByType?) {
        this.errorMsg = this.getFormError();
        if ((this.isCzBased || this.isSb) && this.isShownMakeModelTypeSearch) {
            if (this.makeModelType.searchForm.valid) {
                this.makeModelType.seachData();
                return;
            } else {
                this.makeModelType.checkValidMakeModelTypeInput();
            }
        }
        if (this.vehicleSearchForm.valid) {
            const requestBody = this.vehicleSearchForm.value;
            const searchType = searchByType || this.selectedSearchType;
            const isEmpty = this.isEmptyForm(this.vehicleSearchForm);
            if (isEmpty) {
                this.errorMsg = 'SEARCH.ERROR_MESSAGE.REQUIRED_FIELD';
                return;
            }

            this.searchingOther = false;
            this.searchingVin = false;

            const queryParams = {
                size: 10,
                page: 0
            };
            let responseObserver: Observable<any>;
            let search;
            switch (searchType) {
                case LIB_VEHICLE_SEARCH_FREETEXT:
                    this.searchingOther = true;
                    responseObserver = this.searchFreeText(requestBody, queryParams);
                    break;
                case LIB_VEHICLE_SEARCH_DESC_YEAR:
                    this.searchingOther = true;
                    responseObserver = this.searchByDescYear(requestBody, queryParams);
                    break;
                case LIB_VEHICLE_SEARCH_VIN:
                    search = (requestBody.vehicleVin || '').replace(/\s/g, '').toUpperCase();
                    this.searchingVin = true;
                    responseObserver = this.searchVehicleByVin(requestBody);
                    break;
                default:
                    this.searchingOther = false;
                    this.searchingVin = false;
                    break;
            }
            if (search) {
                this.vehicleSearchEventEmitter.emit({
                    type: 'emit',
                    searchType,
                    search
                });
            }
            responseObserver.subscribe(res => {
                if (!!res) {
                    this.vehicleSearchEmitter.emit({
                        searchType,
                        ...res
                    });
                }
            });
        }
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
        const body = new VehicleSearchRequest().buildVehicleCodeRequest(requestBody);
        // Fahrzeugdaten accepts uppercase only
        return this.searchService.searchVehicles(body, queryParams).pipe(
            tap(() => this.searchingOther = false),
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

    private searchByDescYear(requestBody, queryParams) {
        const info = `${requestBody.vehicleName}, ${requestBody.vehicleYear}`;
        const search = new VehicleSearchRequest().buildVehicleDescriptionRequest(requestBody);
        return this.searchService.searchVehicles(search, queryParams).pipe(
            tap(() => this.searchingOther = false),
            map((res: any) => {
                if (res && res.vehicles && res.vehicles.content && res.vehicles.content.length > 0) {
                    return {
                        analytic: {
                            searchTerm: requestBody.vehicleName,
                            searchYear: requestBody.vehicleYear || '',
                            numberOfHits: res.vehicles.totalElements,
                        },
                        search,
                        info
                    };
                } else {
                    return this.handleErrorMessage({ code: 404 }, info);
                }
            }),
            catchError(error => of(this.handleErrorMessage(error, info))));
    }

    private searchVehicleByVin(requestBody) {
        const info = requestBody.vehicleVin;
        const formattedVinCode = info.replace(/\s/g, '').toUpperCase();

        if (formattedVinCode.length !== LIB_VEHICLE_SEARCH_VIN_LENGTH) {
            this.searchingVin = false;
            this.errorMsg = 'SEARCH.ERROR_MESSAGE.INVALID_VIN';
            return of(null);
        }

        if (requestBody.vinMode === VIN_MODE.STANDARD) {
            return of({
                search: {
                    vinCode: formattedVinCode,
                    vinMode: VIN_MODE.STANDARD,
                    searchByVin: true
                },
                info
            });
        }

        const vinRequestBody = new VinSecurityRequest(formattedVinCode, 'VIN', this.language.toUpperCase());
        return this.searchService.checkVinSecurity(vinRequestBody).pipe(
            tap(() => this.searchingVin = false),
            map(res => {
                if (res.errorCode) {
                    this.handleErrorForVinRequest(res.errorMessage, res.errorCode);
                    this.vehicleSearchEventEmitter.emit({
                        type: 'error',
                        searchType: this.selectedSearchType,
                        search: formattedVinCode,
                        error: {
                            ...res,
                            code: res.errorCode
                        }
                    });
                    return null;
                }
                return {
                    search: {
                        vinCode: formattedVinCode,
                        estimateId: res.estimateId,
                        searchByVin: true
                    },
                    info
                };
            }),
            catchError(error => of(this.handleErrorMessage(error, info))));
    }

    resetUnfocusInput(searchType: string, isSearchFormSplitted: boolean) {
        let unfocusInput = [];
        switch (searchType) {
            case this.VEHICLE_DESCRIPTION_SEARCH:
                unfocusInput = isSearchFormSplitted ? ['vehicleCode'] : ['vehicleCode', 'vehicleVin'];
                break;
            case this.VEHICLE_CODE_SEARCH:
                unfocusInput = isSearchFormSplitted ? ['vehicleName', 'vehicleYear'] : ['vehicleName', 'vehicleYear', 'vehicleVin'];
                break;
            case this.VEHICLE_VIN_SEARCH:
                unfocusInput = isSearchFormSplitted ? [] : ['vehicleName', 'vehicleYear', 'vehicleCode'];
                break;
            case this.VEHICLE_MAKE_MODEL_TYPE_SEARCH:
                unfocusInput = (this.isCzBased || this.isSb) ? ['vehicleName', 'vehicleYear', 'vehicleCode', 'vehicleVin'] : [];
                break;
            default:
                unfocusInput = [];
                break;
        }

        this.selectedSearchType = searchType;
        unfocusInput.forEach(input => {
            this.vehicleSearchForm.get(input).reset();
        });
        this.inValidField = '';
        if (this.makeModelType && searchType !== this.VEHICLE_MAKE_MODEL_TYPE_SEARCH) {
            this.makeModelType.resetForm();
        }
    }

    private initForm() {
        this.vehicleSearchForm = this.fb.group({
            vehicleName: [''],
            vehicleYear: ['', yearValidator],
            vehicleCode: [''],
            vehicleVin: [{ value: '', disabled: !this.isCzBased }],
            vinMode: [{ value: this.getDefaultVinMode(), disabled: !this.isCzBased }]
        }, { validators: requireVehicleNameValidator });
    }

    private getDefaultVinMode() {
        if(this.isCzBased) {
            return VIN_MODE.STANDARD;
        }

        if(this.isSb) {
            return VIN_MODE.PREMIUM;
        }

        return '';
    }

    private getFormError() {
        if ((this.isCzBased || this.isSb) && this.makeModelType && this.makeModelType.searchForm.invalid) {
            if (this.vehicleSearchForm.valid && this.isEmptyForm(this.vehicleSearchForm)) {
                return 'SEARCH.ERROR_MESSAGE.REQUIRED_FIELD';
            }
        }
        if (this.vehicleSearchForm.invalid) {
            if (this.vehicleSearchForm.errors && this.vehicleSearchForm.errors.requiredVehicleName) {
                this.inValidField = 'vehicleName';
                return 'SEARCH.ERROR_MESSAGE.REQUIRED_FIELD';
            }
            const vehicleYear = this.vehicleSearchForm.get('vehicleYear');
            if (vehicleYear.errors && vehicleYear.errors.invalidYear) {
                this.inValidField = 'vehicleYear';
                return 'SEARCH.HOVER_TITLES.YEAR';
            }
        }
        this.inValidField = '';
        return '';
    }

    private handleErrorMessage(error, search) {
        // turn off the spinner
        this.searchingOther = false;
        let code = error.error_code || error.code || error.status;
        if (!code) {
            code = error.error;
        }
        switch (code) {
            case 404:
            case 400:
                // this.gaService.sendNoResultFound(searchTerm);
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
        this.vehicleSearchEventEmitter.emit({
            type: 'error',
            searchType: this.selectedSearchType,
            search,
            error: {
                ...error,
                code
            }
        });
        return null;
    }

    private processDms(data) {
        this.isDmsVersion3 = data.isDmsVersion3;

        if (this.isDmsVersion3) {
            if (data.vehicleVin && this.hasVinLicenseAmountForSearch) {
                this.selectedSearchType = LIB_VEHICLE_SEARCH_VIN;
                this.vehicleSearchForm.get('vehicleVin').setValue(data.vehicleVin);
            }
            this.setDmsVehicleSearchText(data);
            return;
        }

        this.setDmsVehicleSearchText(data);
        this.onSubmit();
    }

    private setDmsVehicleSearchText(data) {
        if (data.vehicleCode) {
            this.vehicleSearchForm.get('vehicleCode').setValue(data.vehicleCode);
            this.selectedSearchType = LIB_VEHICLE_SEARCH_FREETEXT;
        } else if (data.vehicleName) {
            this.vehicleSearchForm.get('vehicleName').setValue(data.vehicleName);
            this.selectedSearchType = LIB_VEHICLE_SEARCH_DESC_YEAR;
        }
    }

    private checkHasVinPermission() {
        if (this.isSalesOnBeHalf) {
            return this.hasVinSearchPermission;
        }
        return this.vinLicenseAmount > 0;
    }

    private handleErrorForVinRequest(errorMessage, errorCode) {
        if (errorCode === 100) {
            this.errorMsg = `${errorMessage} ${this.translateService.instant(this.getGtMotiveFriendlyErrorMessage(errorCode))}`;
        } else {
            this.errorMsg = this.translateService.instant(this.getGtMotiveFriendlyErrorMessage(errorCode));
        }
    }

    private isEmptyForm(form: FormGroup) {
        const value = { ...form.value };
        delete value.vinMode;
        return !Object.keys(value).find(k => !!(form.value[k] || '').trim());
    }

    private getTabIndexes() {
        if (this.isCzBased || this.isSb) {
            return {
                vehicleName: 6,
                vehicleYear: 7,
                vehicleVin: 8,
                buyVin: 9,
                searchVin: 10
            };
        }
        if (this.isCH) {
            return {
                vehicleName: 2,
                vehicleYear: 3,
                vehicleCode: 1,
                vehicleVin: 4,
                buyVin: 5,
                searchVin: 6
            };
        }
        return {
            vehicleName: 1,
            vehicleYear: 2,
            vehicleCode: 3,
            vehicleVin: 4,
            buyVin: 5,
            searchVin: 6
        };
    }

    private getGtMotiveFriendlyErrorMessage(errorCode) {
        // Refer to file GTmotive Error Code 2018-10 in #3020
        const gtMotiveErrorMessage = GTMOTIVE_ERROR_MSGS[errorCode] || 'SEARCH.VIN_ERROR_MSG.UNKNOWN';

        return gtMotiveErrorMessage;
    }

    private updateVinSearchValue(value: string) {
        this.vehicleSearchForm.get('vehicleVin').setValue(value);
    }

    private fillVinSearchInputAutomatically() {
        if (this.autoFilledVinValue) {
            this.updateVinSearchValue(this.autoFilledVinValue);
            this.selectedSearchType = this.VEHICLE_VIN_SEARCH;
        }
    }
}
