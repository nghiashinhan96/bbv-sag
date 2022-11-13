import { Component, OnInit, OnDestroy, ViewChild, ElementRef } from '@angular/core';
import { PkwTyreFilter } from '../../models/pkw-tyre-filter.model';
import { PkwTyresModel } from '../../models/pkw-tyres.model';
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
import { PkwTyresAggregations } from '../../models/pkw-tyre-aggregations.model';
import { Validator } from 'src/app/core/utils/validator';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { TyreAnalyticService } from 'src/app/analytic-logging/services/tyre-analytic.service';
import { SEARCH_MODE } from 'sag-article-list';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-pkw-tyre-filters',
    templateUrl: './pkw-tyre-filters.component.html',
    styleUrls: ['./pkw-tyre-filters.component.scss']
})
export class PkwTyreFiltersComponent implements OnInit, OnDestroy {
    private readonly WINTER_TEXT = 'WINTER';
    private readonly SUMMER_TEXT = 'SUMMER';
    private readonly DEBOUNCE = 500;
    private readonly ARTICLE_LIMITATION = 300;
    public readonly maxLengthWidth = 3;
    public readonly maxLengthHeight = 4;
    public readonly maxLengthRadius = 2;
    public readonly EXAMPLE_MATCH_CODES = TyresService.EXAMPLE_MATCH_CODES;

    private user: UserDetail;
    data: PkwTyresModel = new PkwTyresModel();
    loading = false;
    matchCodeArticleOverLimit = false;

    form: FormGroup;

    private subs = new Subscription();

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
            this.key = `${Constant.PKW_TYRE}_${this.user.custNr}`;
            this.initSearchFilter();
        }
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    initForm() {
        this.form = this.fb.group({
            season: [Constant.SPACE],
            inputWidth: [],
            inputHeight: [],
            inputRadius: [],
            width: [Constant.SPACE],
            height: [Constant.SPACE],
            radius: [Constant.SPACE],
            speedIndex: [Constant.SPACE],
            supplier: [Constant.SPACE],
            fzCategory: [Constant.SPACE],
            runflat: [false],
            spike: [false],
            tyreSegment: [Constant.SPACE],
            matchCode: [Constant.SPACE],
            loadIndex: [Constant.SPACE],
            totalElements: [0]
        });
        this.initDimensionInputEvent();
        this.initDimensionSelectEvent();
    }

    getInitialFormValue() {
        return {
            season: Constant.SPACE,
            inputWidth: '',
            inputHeight: '',
            inputRadius: '',
            width: Constant.SPACE,
            height: Constant.SPACE,
            radius: Constant.SPACE,
            speedIndex: Constant.SPACE,
            supplier: Constant.SPACE,
            fzCategory: Constant.SPACE,
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
        this.subs.add(
            inputWidth.valueChanges
                .pipe(debounceTime(this.DEBOUNCE))
                .subscribe(() => {
                    this.onDimensionInputChange(width, inputWidth, this.data.content.widths, this.inputHeightEl);
                })
        );

        this.subs.add(
            inputHeight.valueChanges
                .pipe(debounceTime(this.DEBOUNCE))
                .subscribe(() => {
                    this.onDimensionInputChange(height, inputHeight, this.data.content.heights, this.inputRadiusEl);
                })
        );

        this.subs.add(
            inputRadius.valueChanges
                .pipe(debounceTime(this.DEBOUNCE))
                .subscribe(() => {
                    this.onDimensionInputChange(radius, inputRadius, this.data.content.radiuses);
                })
        );
    }

    initDimensionSelectEvent() {
        const { width, height, radius, inputWidth, inputHeight, inputRadius } = this.form.controls;
        this.subs.add(
            width.valueChanges.subscribe(() => {
                this.onDimensionSelectChange(width, inputWidth);
            })
        );

        this.subs.add(
            height.valueChanges.subscribe(() => {
                this.onDimensionSelectChange(height, inputHeight);
            })
        );

        this.subs.add(
            radius.valueChanges.subscribe(() => {
                this.onDimensionSelectChange(radius, inputRadius);
            })
        );
    }

    initSearchFilter() {
        const tyreSearchHistory = this.appStorageService.filterHistory;
        const cached = tyreSearchHistory[this.key];
        if (!!cached) {
            this.updateSearchHistoryToForm(cached);
        } else {
            this.form.controls.season.setValue(this.getCurrentSeason());
        }

        this.getTyreDataFilters();
    }

    public updateSearchHistoryToForm(searchHistory: any) {
        const seasonValue = searchHistory.season !== Constant.SPACE ? searchHistory.season : this.getCurrentSeason();

        this.form.setValue({
            season: seasonValue,
            inputWidth: searchHistory.inputWidth,
            inputHeight: searchHistory.inputHeight,
            inputRadius: searchHistory.inputRadius,
            width: searchHistory.width,
            height: searchHistory.height,
            radius: searchHistory.radius,
            speedIndex: searchHistory.speedIndex,
            supplier: searchHistory.supplier,
            fzCategory: searchHistory.fzCategory,
            runflat: searchHistory.runflat,
            spike: searchHistory.spike,
            tyreSegment: searchHistory.tyreSegment,
            matchCode: searchHistory.matchCode,
            loadIndex: searchHistory.loadIndex,
            totalElements: searchHistory.totalElements
        }, { emitEvent: false });
    }

    getTyreDataFilters() {
        SpinnerService.start('connect-tyre .tyres-filters');
        this.loading = true;
        this.form.controls.matchCode.setValue(Constant.SPACE);
        const params = new PkwTyreFilter(this.form.value);
        this.tyreService.getPkwTyreDataFilter(params.dto)
            .pipe(finalize(() => {
                this.loading = false;
                SpinnerService.stop('connect-tyre .tyres-filters');
            }))
            .subscribe((res: PkwTyresModel) => {
                if (!res) {
                    return;
                }

                this.data = res;

                this.updateFilterOptions(this.data.content, params);
            }, error => { });
    }

    updateFilterOptions(aggregations: PkwTyresAggregations, filter: PkwTyreFilter) {
        const data = {
            seasons: {
                value: filter.season,
                label: `TYRE.SEASONS.${filter.season.toString().toUpperCase()}`
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
            suppliers: {
                value: filter.supplier,
                label: filter.supplier
            },
            fz_categories: {
                value: filter.fzCategory,
                label: `TYRE.FZ_CATEGORIES.${filter.fzCategory.toString().toUpperCase()}`
            },
            tyre_segments: {
                value: filter.tyreSegment,
                label: `TYRE.TYRE_SEGMENTS.${filter.tyreSegment.toString().toUpperCase()}`
            },
            load_indexs: {
                value: filter.loadIndex,
                label: filter.loadIndex
            }
        };

        const options = {
            seasons: this.buildSelectOptions(aggregations.seasons, data.seasons, 'TYRE.SEASONS.', false),
            widths: this.buildSelectOptions(aggregations.widths, data.widths),
            heights: this.buildSelectOptions(aggregations.heights, data.heights),
            radiuses: this.buildSelectOptions(aggregations.radiuses, data.radiuses),
            speed_indexes: this.buildSelectOptions(aggregations.speed_indexes, data.speed_indexes),
            load_indexs: this.buildSelectOptions(aggregations.load_indexs, data.load_indexs),
            tyre_segments: this.buildSelectOptions(aggregations.tyre_segments, data.tyre_segments, 'TYRE.TYRE_SEGMENTS.'),
            suppliers: this.buildSelectOptions(aggregations.suppliers, data.suppliers),
            fz_categories: this.buildSelectOptions(aggregations.fz_categories, data.fz_categories, 'TYRE.FZ_CATEGORIES.')
        };

        this.data.content = new PkwTyresAggregations(options);
    }

    buildSelectOptions(data, inputOption?: NgOption, prefix?: string, hasAllOption = true): NgOption[] {
        let inputOptions = [inputOption];
        const options: NgOption[] = [];

        const allOption: NgOption = {
            value: Constant.SPACE,
            label: 'TYRE.ALL'
        };

        const customText = prefix ? prefix : '';

        data.filter(item => item !== Constant.FILTER_OPTION_ALL_KEY)
            .forEach(element => {
                options.push({ value: `${element}`, label: `${customText}${element}`.toUpperCase() });
            });

        merge(inputOptions, options);

        inputOptions = inputOptions.filter(item => item.value !== Constant.EMPTY_STRING && item.value !== Constant.SPACE);

        return hasAllOption ? [allOption, ...inputOptions] : inputOptions;
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
            const params = new PkwTyreFilter(this.form.value);
            this.tyreService.getPkwTyreDataFilter(params.dto).subscribe((response: PkwTyresModel) => {
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
        this.data = new PkwTyresModel();
        const initValue = this.getInitialFormValue();
        const params = new PkwTyreFilter(initValue);
        this.updateFilterOptions(this.data.content, params);
        this.form.patchValue(initValue);
        this.initSearchFilter();
    }

    onSearch() {
        this.viewArticles(SEARCH_MODE.TYRES_SEARCH);
    }

    searchTyreByMatchCode(callback = () => void (0)) {
        const matchCode = (this.form.controls.matchCode.value || '').trim();
        if (!matchCode) {
            callback();
            return;
        }
        const searchMatchCodeVal = this.getInitialFormValue();
        searchMatchCodeVal.matchCode = matchCode;
        const params = new PkwTyreFilter(searchMatchCodeVal);
        this.form.patchValue(searchMatchCodeVal, { emitEvent: false });
        this.tyreService.getPkwTyreDataFilter(params.dto)
            .pipe(finalize(() => {
                this.initSearchFilter();
                callback();
            }))
            .subscribe((res: PkwTyresModel) => {
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
                this.viewArticles(SEARCH_MODE.TYRES_SEARCH, searchMatchCodeVal);
            });
    }

    viewArticles(searchMode: string, request?) {
        request = request || { ...this.form.value };
        request.type = searchMode;
        request.totalElements = this.data.totalElements;
        request.returnUrl = Constant.TYRE_PAGE;
        delete request.inputWidth;
        delete request.inputHeight;
        delete request.inputRadius;
        this.addTyreSearchHistory(this.form.value);
        this.addTyreSearchHistoryMode();
        this.tyreAnalyticService.sendPkwEvent(request);
        this.tyreAnalyticService.sendPkwGaData(this.form.value);
        this.router.navigate(['/article/result'], { queryParams: request });
    }

    onDimensionInputKeydown(e: KeyboardEvent, types: string) {
        return Validator.Validates(e, types);
    }

    allowDecimalNumber(e: KeyboardEvent, types: string) {
        if (Validator.allowDecimalNumber(e, (e.target as any).value)) {
            return Validator.Validates(e, types);
        }
    }

    public isAllDimensionDataFilled(): boolean {
        const formValue = this.form.value;
        if (formValue.inputWidth && formValue.inputHeight && formValue.inputRadius) {
            return true;
        }
        return false;
    }

    public isSearchTyreDisabled(): boolean {
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

    onFiltering({ term, items }, controlName, current, next) {
        if (items.length === 1) {
            const oldVal = this.form.get(controlName).value;
            if (oldVal !== term) {
                this.form.get(controlName).setValue(term);
                this.onUpdateFilter();
                setTimeout(() => {
                    current.close();
                    if (next) {
                        next.focus();
                    }
                });
            }
        }
    }

    private getCurrentSeason() {
        const currentDate = new Date();
        const currentMonth = currentDate.getMonth() + 1;
        const currentDateOfMonth = currentDate.getDate();
        return this.tyreService.isSummerTime(currentDateOfMonth, currentMonth) ? this.SUMMER_TEXT : this.WINTER_TEXT;
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
            this.appStorageService.tyreSearchHistoryMode[this.user.custNr] = Constant.PKW_TYRE;
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
