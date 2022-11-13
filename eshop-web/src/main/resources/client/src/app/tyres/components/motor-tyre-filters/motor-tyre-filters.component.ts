import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { finalize, debounceTime, first } from 'rxjs/operators';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup, AbstractControl } from '@angular/forms';
import { Constant } from 'src/app/core/conts/app.constant';
import { TyresService } from '../../services/tyres.service';
import { merge } from 'lodash';
import { Router } from '@angular/router';
import { NgOption } from '@ng-select/ng-select';
import { Validator } from 'src/app/core/utils/validator';
import { MotorTyreFilter } from '../../models/motor-tyre-filter.model';
import { MotorTyresAggregations } from '../../models/motor-tyre-aggregations.model';
import { MotorTyresModel } from '../../models/motor-tyres.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { TyreAnalyticService } from 'src/app/analytic-logging/services/tyre-analytic.service';
import { SEARCH_MODE } from 'sag-article-list';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-motor-tyre-filters',
    templateUrl: './motor-tyre-filters.component.html',
    styleUrls: ['./motor-tyre-filters.component.scss']
})
export class MotorTyreFiltersComponent implements OnInit, OnDestroy {
    private readonly DEBOUNCE = 500;
    private readonly ARTICLE_LIMITATION = 300;
    public readonly maxLengthWidth = 3;
    public readonly maxLengthHeight = 2;
    public readonly maxLengthRadius = 2;
    public readonly EXAMPLE_MATCH_CODES = TyresService.EXAMPLE_MATCH_CODES;

    private user: UserDetail;
    data: MotorTyresModel = new MotorTyresModel();
    loading = false;
    matchCodeArticleOverLimit = false;

    form: FormGroup;

    private inputWidthChangeSub: Subscription;
    private inputHeightChangeSub: Subscription;
    private inputRadiusChangeSub: Subscription;
    private selectWidthChangeSub: Subscription;
    private selectHeightChangeSub: Subscription;
    private selectRadiusChangeSub: Subscription;

    @ViewChild('inputHeight', { static: false }) inputHeightEl: ElementRef;
    @ViewChild('inputRadius', { static: false }) inputRadiusEl: ElementRef;

    private key: string;
    constructor(
        private router: Router,
        private fb: FormBuilder,
        private appStorageService: AppStorageService,
        private tyreAnalyticService: TyreAnalyticService,
        private userService: UserService,
        private tyreService: TyresService
    ) { }

    ngOnInit() {
        this.initForm();
        this.user = this.userService.userDetail;
        if (this.user && this.user.custNr) {
            this.key = `${Constant.MOTOR_TYRE}_${this.user.custNr}`;
            this.initSearchFilter();
        }
    }

    ngOnDestroy() {
        this.inputWidthChangeSub.unsubscribe();
        this.inputHeightChangeSub.unsubscribe();
        this.inputRadiusChangeSub.unsubscribe();
        this.selectWidthChangeSub.unsubscribe();
        this.selectHeightChangeSub.unsubscribe();
        this.selectRadiusChangeSub.unsubscribe();
    }

    initForm() {
        this.form = this.fb.group({
            category: [Constant.SPACE],
            inputWidth: [],
            inputHeight: [],
            inputRadius: [],
            width: [Constant.SPACE],
            height: [Constant.SPACE],
            radius: [Constant.SPACE],
            speedIndex: [Constant.SPACE],
            loadIndex: [Constant.SPACE],
            tyreSegment: [Constant.SPACE],
            supplier: [Constant.SPACE],
            runflat: [false],
            spike: [false],
            matchCode: [Constant.SPACE],
            totalElements: [0]
        });
        this.initDimensionInputEvent();
        this.initDimensionSelectEvent();
    }

    getInitialFormValue() {
        return {
            category: Constant.SPACE,
            inputWidth: '',
            inputHeight: '',
            inputRadius: '',
            width: Constant.SPACE,
            height: Constant.SPACE,
            radius: Constant.SPACE,
            speedIndex: Constant.SPACE,
            supplier: Constant.SPACE,
            runflat: false,
            spike: false,
            tyreSegment: Constant.SPACE,
            matchCode: Constant.SPACE,
            loadIndex: Constant.SPACE,
            totalElements: 0
        };
    }

    initDimensionInputEvent() {
        const { width, height, radius, inputWidth, inputHeight, inputRadius } = this.form.controls;
        this.inputWidthChangeSub = inputWidth.valueChanges
            .pipe(debounceTime(this.DEBOUNCE))
            .subscribe(() => {
                this.onDimensionInputChange(width, inputWidth, this.data.content.widths, this.inputHeightEl);
            });
        this.inputHeightChangeSub = inputHeight.valueChanges
            .pipe(debounceTime(this.DEBOUNCE))
            .subscribe(() => {
                this.onDimensionInputChange(height, inputHeight, this.data.content.heights, this.inputRadiusEl);
            });
        this.inputRadiusChangeSub = inputRadius.valueChanges
            .pipe(debounceTime(this.DEBOUNCE))
            .subscribe(() => {
                this.onDimensionInputChange(radius, inputRadius, this.data.content.radiuses);
            });
    }

    initDimensionSelectEvent() {
        const { width, height, radius, inputWidth, inputHeight, inputRadius } = this.form.controls;
        this.selectWidthChangeSub = width.valueChanges.subscribe(() => {
            this.onDimensionSelectChange(width, inputWidth);
        });
        this.selectHeightChangeSub = height.valueChanges.subscribe(() => {
            this.onDimensionSelectChange(height, inputHeight);
        });
        this.selectRadiusChangeSub = radius.valueChanges.subscribe(() => {
            this.onDimensionSelectChange(radius, inputRadius);
        });
    }

    initSearchFilter() {
        const searchHistory = this.appStorageService.filterHistory;
        const cached = searchHistory[this.key];
        if (!!cached) {
            this.updateSearchHistoryToForm(cached);
        }
        this.getTyreDataFilters();
    }

    updateSearchHistoryToForm(searchHistory: any) {
        this.form.setValue({
            category: searchHistory.category,
            inputWidth: searchHistory.inputWidth,
            inputHeight: searchHistory.inputHeight,
            inputRadius: searchHistory.inputRadius,
            width: searchHistory.width,
            height: searchHistory.height,
            radius: searchHistory.radius,
            speedIndex: searchHistory.speedIndex,
            loadIndex: searchHistory.loadIndex,
            tyreSegment: searchHistory.tyreSegment,
            supplier: searchHistory.supplier,
            runflat: searchHistory.runflat,
            spike: searchHistory.spike,
            matchCode: searchHistory.matchCode,
            totalElements: searchHistory.totalElements
        });
    }

    getTyreDataFilters() {
        SpinnerService.start('connect-tyre .tyres-filters');
        this.loading = true;
        this.form.controls.matchCode.setValue(Constant.SPACE);
        const params = new MotorTyreFilter(this.form.value);
        this.tyreService.getMotorTyreDataFilter(params.dto)
            .pipe(finalize(() => {
                this.loading = false;
                SpinnerService.stop('connect-tyre .tyres-filters');
            }))
            .subscribe((res: MotorTyresModel) => {
                if (!res) {
                    return;
                }

                this.data = res;

                this.updateFilterOptions(this.data.content, params);
            }, error => {
            });
    }

    updateFilterOptions(aggregations: MotorTyresAggregations, filter: MotorTyreFilter) {
        const data = {
            motor_categories: {
                value: filter.category,
                label: `TYRE.MOTOR_CATEGORIES.${filter.category.toString().toUpperCase()}`
            },
            widths: {
                value: filter.width,
                label: filter.width
            },
            heights: {
                value: filter.height,
                label: filter.height
            },
            radiuses: {
                value: filter.radius,
                label: filter.radius
            },
            speed_indexes: {
                value: filter.speedIndex,
                label: filter.speedIndex
            },
            load_indexs: {
                value: filter.loadIndex,
                label: filter.loadIndex
            },
            tyre_segments: {
                value: filter.tyreSegment,
                label: `TYRE.TYRE_SEGMENTS.${filter.tyreSegment.toString().toUpperCase()}`
            },
            suppliers: {
                value: filter.supplier,
                label: filter.supplier
            }
        };

        const options = {
            motor_categories: this.buildSelectOptions(
                aggregations.motor_categories,
                data.motor_categories,
                'TYRE.MOTOR_CATEGORIES.',
                'TYRE.MOTOR_CATEGORIES.ALL'
            ),
            widths: this.buildSelectOptions(aggregations.widths, data.widths),
            heights: this.buildSelectOptions(aggregations.heights, data.heights),
            radiuses: this.buildSelectOptions(aggregations.radiuses, data.radiuses),
            speed_indexes: this.buildSelectOptions(aggregations.speed_indexes, data.speed_indexes),
            load_indexs: this.buildSelectOptions(aggregations.load_indexs, data.load_indexs),
            tyre_segments: this.buildSelectOptions(aggregations.tyre_segments, data.tyre_segments, 'TYRE.TYRE_SEGMENTS.'),
            suppliers: this.buildSelectOptions(aggregations.suppliers, data.suppliers)
        };

        this.data.content = new MotorTyresAggregations(options);
    }

    buildSelectOptions(data, inputOption?: NgOption, prefix?: string, allLabelKey = 'TYRE.ALL'): NgOption[] {
        let inputOptions = [inputOption];
        const options: NgOption[] = [];

        const allOption: NgOption = {
            value: Constant.SPACE,
            label: allLabelKey
        };

        const customText = prefix ? prefix : '';

        data.filter(item => item !== Constant.FILTER_OPTION_ALL_KEY)
            .forEach(element => {
                options.push({ value: `${element}`, label: `${customText}${element}`.toUpperCase() });
            });

        merge(inputOptions, options);

        inputOptions = inputOptions.filter(item => item.value !== Constant.EMPTY_STRING && item.value !== Constant.SPACE)

        return [allOption, ...inputOptions];
    }

    onDimensionSelectChange(selectControl: AbstractControl, inputControl: AbstractControl) {
        inputControl.setValue(selectControl.value === Constant.SPACE ? Constant.EMPTY_STRING : selectControl.value, { emitEvent: false });
    }

    onDimensionInputChange(
        selectControl: AbstractControl,
        inputControl: AbstractControl,
        options: NgOption[],
        nextInputElement?: ElementRef
    ) {
        selectControl.setValue(inputControl.value || Constant.SPACE, { emitEvent: false });

        const foundOptions = options.filter(item => String(item.value).indexOf(inputControl.value) !== -1);

        if (foundOptions.length === 1 && foundOptions[0].value === inputControl.value) {
            this.focusNextInput(nextInputElement);
        }

        this.getTyreDataFilters();
    }

    onDimensionInputEnter() {
        if (!this.isSearchTyreDisabled()) {
            const params = new MotorTyreFilter(this.form.value);
            this.tyreService.getMotorTyreDataFilter(params.dto).subscribe((response: MotorTyresModel) => {
                if (response) {
                    this.data = response;
                    this.viewArticles(SEARCH_MODE.TYRES_SEARCH);
                }
            });
        }
    }

    onUpdateFilter() {
        this.getTyreDataFilters();
    }

    onReset() {
        this.removedTypreHistory();
        this.data = new MotorTyresModel();
        const initValue = this.getInitialFormValue();
        const params = new MotorTyreFilter(initValue);
        this.updateFilterOptions(this.data.content, params);
        this.form.patchValue(initValue);
        this.initSearchFilter();
    }

    onSearch() {
        this.viewArticles(SEARCH_MODE.MOTOR_TYRES_SEARCH);
    }

    searchTyreByMatchCode(callback = () => void (0)) {
        const matchCode = (this.form.controls.matchCode.value || '').trim();

        if (!matchCode) {
            callback();
            return;
        }

        const searchMatchCodeVal = this.getInitialFormValue();
        searchMatchCodeVal.matchCode = matchCode;
        const params = new MotorTyreFilter(searchMatchCodeVal);
        this.form.patchValue(searchMatchCodeVal);
        this.tyreService.getMotorTyreDataFilter(params.dto)
            .pipe(finalize(() => {
                callback();
            }))
            .subscribe((res: MotorTyresModel) => {
                if (!res) {
                    return;
                }

                this.data = res;

                if (this.data.totalElements === 0) {
                    return;
                }

                if (this.data.totalElements > this.ARTICLE_LIMITATION
                    && (AffiliateUtil.isAffiliateAT(environment.affiliate)
                    || AffiliateUtil.isAffiliateCH(environment.affiliate))) {
                    this.matchCodeArticleOverLimit = true;
                    return;
                }

                this.viewArticles(SEARCH_MODE.MOTOR_TYRES_SEARCH, searchMatchCodeVal);
            });
    }

    viewArticles(searchMode: string, request?: any) {
        request = request || { ...this.form.value };
        request.type = searchMode;
        request.totalElements = this.data.totalElements;
        request.returnUrl = Constant.TYRE_PAGE;
        delete request.inputWidth;
        delete request.inputHeight;
        delete request.inputRadius;
        this.addTyreSearchHistory(this.form.value);
        this.addTyreSearchHistoryMode();
        this.tyreAnalyticService.sendMotorEvent(request);
        this.router.navigate(['/article/result'], { queryParams: request });
    }

    onDimensionInputKeydown(e: KeyboardEvent, types: string) {
        return Validator.Validates(e, types);
    }

    isAllDimensionDataFilled(): boolean {
        const formValue = this.form.value;
        if (formValue.inputWidth && formValue.inputHeight && formValue.inputRadius) {
            return true;
        }
        return false;
    }

    isSearchTyreDisabled(): boolean {
        if (this.data) {
            return this.data.totalElements === 0 ||
                this.data.totalElements > this.ARTICLE_LIMITATION ||
                !this.isAllDimensionDataFilled();
        }
        return true;
    }

    setMatchCode(matchCode) {
        this.form.controls.matchCode.setValue(matchCode);
    }

    private addTyreSearchHistory(data) {
        if (this.key) {
            this.appStorageService.filterHistory = {
                key: this.key,
                value: data
            };
        }
    }

    private removedTypreHistory() {
        this.appStorageService.clearFilterHistory(this.key);
    }

    private addTyreSearchHistoryMode() {
        if (this.user.custNr) {
            this.appStorageService.tyreSearchHistoryMode = {
                key: this.user.custNr,
                value: Constant.MOTOR_TYRE
            };
        }
    }

    private focusNextInput(inputElement: ElementRef) {
        if (!inputElement) {
            return;
        }

        const { width, height, radius } = this.form.value;

        if (
            width === Constant.SPACE &&
            height !== Constant.SPACE &&
            radius !== Constant.SPACE
        ) {
            return;
        }

        if (
            height === Constant.SPACE &&
            width !== Constant.SPACE &&
            radius !== Constant.SPACE
        ) {
            return;
        }

        if (
            width === Constant.SPACE ||
            height === Constant.SPACE ||
            radius === Constant.SPACE
        ) {
            // focus the next one
            setTimeout(() => inputElement.nativeElement.focus());
            return;
        }
    }
}
