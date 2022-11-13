import { Component, Input, OnInit, OnDestroy, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { FormBuilder, FormGroup, FormControl, Validators } from '@angular/forms';

import { SubSink } from 'subsink';
import { BsModalRef } from 'ngx-bootstrap/modal';

import { MarginService } from '../../services/margin.service';
import { BrandSearchModel } from '../../models/margin-brand-search.model';
import { BrandModel } from '../../models/margin-brand.model';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { finalize, map, catchError, tap } from 'rxjs/operators';
import { SagMessageData } from 'sag-common';
import { MarginErrorMsgs, MARGIN_VALUES } from '../../enums/margin-mgt.enum';
import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { of, Subscription } from 'rxjs';

@Component({
  selector: 'connect-margin-brand-form-modal',
  templateUrl: './margin-brand-form-modal.component.html',
  styleUrls: ['./margin-brand-form-modal.component.scss']
})
export class MarginBrandFormModalComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() title = 'MARGIN_MANAGE.EDIT_BRAND';
  @Input() brand: BrandModel = new BrandModel();

  isBrandExpand: boolean = true;
  isMarginExpand: boolean = true;
  marginForm: FormGroup;

  brands: BrandSearchModel[] = [];
  subs = new SubSink();
  margins = MARGIN_VALUES;
  callback: any;
  spinnerSelector = '.modal-content';
  msg: SagMessageData;
  editMode: boolean = false;

  isSearchFreeText = false;
  EPSILON_HEIGHT = 20;
  isLoading = false;
  total: number;
  errorMessage: string;
  private searchBrandSub: Subscription;

  @ViewChild('pop', { static: true }) resultPopup: PopoverDirective;
  @ViewChild('searchInput', { static: true }) searchInput: ElementRef;

  constructor (
    private bsModalRef: BsModalRef,
    private fb: FormBuilder,
    private marginService: MarginService
  ) { }

  ngOnInit() {
    this.editMode = !!this.brand.brandId || (!this.brand.brandId && this.brand.default);

    this.initForm();
    this.initFormData();
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      if (this.searchInput) {
        this.searchInput.nativeElement.focus();
      }
    });
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  close() {
    this.bsModalRef.hide();
  }

  onSave() {
    if (this.marginForm.valid) {
      SpinnerService.start(this.spinnerSelector);

      const formValue = this.marginForm.getRawValue();

      this.margins.forEach(margin => {
        this.brand[`margin${margin}`] = Number(formValue[`margin${margin}`]);
      });

      if (this.editMode) {
        this.editBrand();
      } else {
        this.createBrand(formValue);
      }
    }
  }

  searchBrands(callback?) {
    this.marginForm.get('page').setValue(0);

    if (this.searchBrandSub) {
      this.searchBrandSub.unsubscribe();
    }
    this.isSearchFreeText = true;

    this.searchBrandSub = this.searchData()
      .pipe(
        finalize(() => this.isSearchFreeText = false)
      )
      .subscribe((brands: any) => {
        if (brands.length > 0) {
          this.brands = brands;

          if (this.resultPopup) {
            this.resultPopup.show();
          }
        } else {
          if (this.resultPopup) {
            this.resultPopup.hide();
          }
          this.errorMessage = 'COMMON_LABEL.NO_RESULTS_FOUND';
        }

        if(callback) {
            callback();
        }
      });
  }

  private searchData() {
    const name = this.marginForm.get('name').value || '';
    const page = this.marginForm.get('page').value || 0;
    const size = this.marginForm.get('size').value || APP_DEFAULT_PAGE_SIZE;

    this.isLoading = true;
    this.errorMessage = '';

    return this.marginService.searchBrand({ brandName: name.trim(), page, size }).pipe(
      map((res: any) => {
        this.total = res.totalElements;
        return res.content || [];
      }),
      catchError((err) => {
        this.errorMessage = 'COMMON_LABEL.NO_RESULTS_FOUND';
        return of([]);
      }),
      tap(() => this.isLoading = false)
    );
  }

  onScroll(event) {
    const container = event.currentTarget;
    if ((container.scrollHeight - container.scrollTop) >= (container.clientHeight - this.EPSILON_HEIGHT)) {
      this.onScrollToEnd();
    }
  }

  onSelectBrandFromSearch(brand: BrandSearchModel) {
    this.marginForm.get('brandId').setValue(brand.dlnrid);
    this.marginForm.get('name').setValue(brand.suppname);
    if (this.resultPopup) {
      this.resultPopup.hide();
    }
  }

  private onScrollToEnd() {
    if (this.isLoading || this.brands.length >= this.total) {
      return;
    }
    let page = this.marginForm.get('page').value;
    page = page + 1;
    this.marginForm.get('page').setValue(page);

    this.searchData().subscribe(res => {
      this.brands = [...this.brands, ...res];
    });
  }

  private editBrand() {
    this.subs.sink = this.marginService.editBrand(this.brand)
      .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
      .subscribe(
        (res) => {
          this.handleSuccessResponse();
        },
        (err) => {
          this.handleErrorResponse(err, this.brand.name);
        });
  }

  private createBrand(formValue) {
    const brandId = formValue.brandId;
    const brand = this.brands.find(br => br.dlnrid === brandId);
    let name = '';

    if (brand) {
      name = brand.suppname;
    }

    this.brand = {
      ...this.brand,
      brandId: formValue.brandId,
      name: name
    };

    this.marginForm.get('name').setValue(name);

    this.subs.sink = this.marginService.createBrand(this.brand)
      .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
      .subscribe(
        (res) => {
          this.handleSuccessResponse();
        },
        (err) => {
          this.handleErrorResponse(err, name);
        });
  }

  private handleErrorResponse(err, name: string) {
    const message = MarginErrorMsgs[err && err.error && err.error.error_code] || 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY';
    this.msg = { type: 'ERROR', message, params: { name } } as SagMessageData;
  }

  private handleSuccessResponse() {
    this.msg = { type: 'SUCCESS', message: 'COMMON_MESSAGE.SAVE_SUCCESSFULLY' } as SagMessageData;

    setTimeout(() => {
      this.backToPreviousModel();
    }, 1000);
  }

  private initForm() {
    const controls = {};

    this.margins.forEach(margin => {
      controls[`margin${margin}`] = new FormControl('', [Validators.required])
    });

    controls['brandId'] = new FormControl('', [Validators.required]);
    controls['name'] = new FormControl('');
    controls['page'] = new FormControl(0);
    controls['size'] = APP_DEFAULT_PAGE_SIZE;

    if (this.editMode && this.brand.default) {
      controls['brandId'] = new FormControl('', []);
    }

    this.marginForm = this.fb.group(controls);
  }

  private backToPreviousModel() {
    this.close();

    if (this.callback) {
      this.callback();
    }
  }

  private initFormData() {
    if (this.editMode) {
      this.marginForm.patchValue(this.brand);
    }
  }
}
