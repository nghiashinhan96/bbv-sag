import { MarginErrorMsgs } from './../../enums/margin-mgt.enum';
import { Component, Input, OnInit, ViewChild, ElementRef } from '@angular/core';
import { MarginArticleGroupModel } from '../../models/margin-article-group.model';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { SubSink } from 'subsink';
import { SagMessageData } from 'sag-common';
import { Subscription } from 'rxjs/internal/Subscription';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { MarginService } from '../../services/margin.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { APP_DEFAULT_PAGE_SIZE } from 'src/app/core/conts/app.constant';
import { map, catchError, tap, finalize } from 'rxjs/operators';
import { of } from 'rxjs/internal/observable/of';
import { MarginSupplierArticleGroupSearchRequestModel } from '../../models/margin-supplier-article-group-search-request.model';
import { MarginSupplierArtGroupModel } from '../../models/margin-supplier-art-group.model';
import { MARGIN_VALUES } from '../../enums/margin-mgt.enum';

@Component({
  selector: 'connect-margin-article-group-form-modal',
  templateUrl: './margin-article-group-form-modal.component.html',
  styleUrls: ['./margin-article-group-form-modal.component.scss']
})
export class MarginArticleGroupFormModalComponent implements OnInit {
  @Input() title = 'MARGIN_MANAGE.EDIT_ARTICL_GROUP';
  @Input() artGroup: MarginArticleGroupModel = new MarginArticleGroupModel();
  @Input() currentLangCode: string;

  callback: any;

  marginArtGroupForm: FormGroup;
  editMode: boolean = false;
  msg: SagMessageData;

  margins = MARGIN_VALUES;
  EPSILON_HEIGHT = 20;
  artGrpErrorMsg: string;
  isSearchArtGrpText = false;
  artGrps: any[] = [];
  isLoading = false;
  total: number;
  isArtGrpExpand: boolean = true;
  isGHArtGrpExpand: boolean = true;
  isMarginExpand: boolean = true;

  private spinnerSelector = '.modal-content';
  private subs = new SubSink();
  private searchSupplierArtGrp = new Subscription();

  @ViewChild('pop', { static: true }) resultPopup: PopoverDirective;
  @ViewChild('searchInput', { static: true }) searchInput: ElementRef;

  constructor (
    private bsModalRef: BsModalRef,
    private fb: FormBuilder,
    private marginService: MarginService
  ) { }

  ngOnInit() {
    this.editMode = !!this.artGroup.id || (!this.artGroup.id && this.artGroup.default);

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
    if (this.marginArtGroupForm.valid) {
      SpinnerService.start(this.spinnerSelector);

      const formValue = this.marginArtGroupForm.getRawValue();

      this.margins.forEach(margin => {
        this.artGroup[`margin${margin}`] = Number(formValue[`margin${margin}`]);
      });

      this.artGroup.customArticleGroup = formValue.customArticleGroup;
      this.artGroup.customArticleGroupDesc = formValue.customArticleGroupDesc;

      if (this.editMode) {
        this.editArtGrp();
      } else {
        this.createArtGrp(formValue);
      }
    }
  }

  searchArtGrps(callback?) {
    this.marginArtGroupForm.get('page').setValue(0);

    if (this.searchSupplierArtGrp) {
      this.searchSupplierArtGrp.unsubscribe();
    }

    this.isSearchArtGrpText = true;
    this.searchSupplierArtGrp = this.searchData()
      .pipe(
        finalize(() => this.isSearchArtGrpText = false)
      )
      .subscribe((artGrps: any[]) => {
        if (artGrps.length > 0) {
          this.artGrps = artGrps.map(art => new MarginSupplierArtGroupModel(art));

          if (this.resultPopup) {
            this.resultPopup.show();
          }
        } else {
          if (this.resultPopup) {
            this.resultPopup.hide();
          }
          this.artGrpErrorMsg = 'COMMON_LABEL.NO_RESULTS_FOUND';
        }

        if (callback) {
          callback();
        }
      });
  }

  private searchData() {
    const { articleGroup, articleGroupDesc, page, size } = this.marginArtGroupForm.getRawValue();

    this.isLoading = true;
    this.artGrpErrorMsg = '';
    this.msg = null;

    return this.marginService.searchSupplierArtGroups({ articleGroup, articleGroupDesc, page, size } as MarginSupplierArticleGroupSearchRequestModel).pipe(
      map((res: any) => {
        this.total = res.totalElements;
        return res.content || [];
      }),
      catchError((err) => {
        this.artGrpErrorMsg = 'COMMON_LABEL.NO_RESULTS_FOUND';
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

  onSelectArtGrpFromSearch(artGrp: MarginSupplierArtGroupModel) {
    this.marginArtGroupForm.get('articleGroup').setValue(artGrp.wssArtGrpTree.artgrp);
    this.marginArtGroupForm.get('articleGroupDesc').setValue(artGrp.getCurrentName(this.currentLangCode));

    this.marginArtGroupForm.get('sagArticleGroup').setValue(artGrp.wssArtGrpTree.artgrp);
    this.marginArtGroupForm.get('sagArticleGroupDesc').setValue(artGrp.wssDesignations);

    this.marginArtGroupForm.get('leafId').setValue(artGrp.wssArtGrpTree.leafid);
    this.marginArtGroupForm.get('parentLeafId').setValue(artGrp.wssArtGrpTree.parentid);

    if (this.resultPopup) {
      this.resultPopup.hide();
    }
  }

  private onScrollToEnd() {
    if (this.isLoading || this.artGrps.length >= this.total) {
      return;
    }
    let page = this.marginArtGroupForm.get('page').value;
    page = page + 1;
    this.marginArtGroupForm.get('page').setValue(page);

    this.searchData().subscribe(res => {
      const artGrps = [...this.artGrps, ...res];
      this.artGrps = (artGrps || []).map(item => new MarginSupplierArtGroupModel(item));
    });
  }

  private editArtGrp() {
    this.subs.sink = this.marginService.editArticleGroup(this.artGroup)
      .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
      .subscribe(
        (res) => {
          this.handleSuccessResponse(res);
        },
        (err) => {
          this.handleErrorResponse(err, this.artGroup);
        });
  }

  private createArtGrp(formValue) {
    this.artGroup = {
      ...this.artGroup,
      sagArticleGroup: formValue.sagArticleGroup,
      sagArticleGroupDesc: formValue.sagArticleGroupDesc,
      customArticleGroup: formValue.customArticleGroup,
      customArticleGroupDesc: formValue.customArticleGroupDesc,
      leafId: formValue.leafId,
      parentLeafId: formValue.parentLeafId
    } as MarginArticleGroupModel;

    this.margins.forEach(margin => {
      this.artGroup[`margin${margin}`] = formValue[`margin${margin}`];
    });

    this.subs.sink = this.marginService.createArtGroup(this.artGroup)
      .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
      .subscribe(
        (res) => {
          this.handleSuccessResponse(res);
        },
        (err) => {
          this.handleErrorResponse(err, this.artGroup);
        });
  }

  private handleErrorResponse(err, artGroup: MarginArticleGroupModel) {
    const message = MarginErrorMsgs[err && err.error && err.error.error_code] || 'COMMON_MESSAGE.SAVE_UNSUCCESSFULLY';
    let params = null;

    if (message === MarginErrorMsgs['DUPLICATED_MARGIN_ARTICLE_GROUP']) {
      params = { sagArticleGroup: artGroup.sagArticleGroup, customArticleGroup: artGroup.customArticleGroup };
    } else {
      params = { leafId: artGroup.leafId }
    }

    this.msg = { type: 'ERROR', message, params: params } as SagMessageData;
  }

  private handleSuccessResponse(res) {
    this.msg = { type: 'SUCCESS', message: 'COMMON_MESSAGE.SAVE_SUCCESSFULLY' } as SagMessageData;

    setTimeout(() => {
      this.backToPreviousModel(res);
    }, 1000);
  }

  private initForm() {
    const controls = {};

    this.margins.forEach(margin => {
      controls[`margin${margin}`] = new FormControl('', [Validators.required])
    });

    controls['articleGroup'] = new FormControl('', [Validators.required]);
    controls['articleGroupDesc'] = new FormControl('', [Validators.required]);

    controls['sagArticleGroup'] = new FormControl('', [Validators.required]);
    controls['sagArticleGroupDesc'] = new FormControl('', [Validators.required]);

    controls['customArticleGroup'] = new FormControl('', [Validators.required]);
    controls['customArticleGroupDesc'] = new FormControl('', [Validators.required]);

    controls['page'] = new FormControl(0);
    controls['size'] = APP_DEFAULT_PAGE_SIZE;
    controls['leafId'] = new FormControl('');
    controls['parentLeafId'] = new FormControl('');

    if (this.editMode && this.artGroup.default) {
      controls['articleGroup'] = new FormControl('', []);
      controls['articleGroupDesc'] = new FormControl('', []);
      controls['sagArticleGroup'] = new FormControl('', []);
      controls['sagArticleGroupDesc'] = new FormControl('', []);
    }

    this.marginArtGroupForm = this.fb.group(controls);
  }

  private backToPreviousModel(res?) {
    this.close();

    if (this.callback) {
      this.callback(res);
    }
  }

  private initFormData() {
    if (this.editMode) {
      this.marginArtGroupForm.patchValue(this.artGroup);
      this.marginArtGroupForm.controls['articleGroup'].setValue(this.artGroup.sagArticleGroup);
      this.marginArtGroupForm.controls['articleGroupDesc'].setValue(this.artGroup.getArtGrpDes(this.currentLangCode));
    }
  }
}
