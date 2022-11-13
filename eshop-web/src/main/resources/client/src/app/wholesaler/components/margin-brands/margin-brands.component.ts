import { Component, OnInit, ViewChild, TemplateRef, OnDestroy } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';
import { BsModalRef } from 'ngx-bootstrap/modal';

import { SagConfirmationBoxComponent } from 'sag-common';
import { SagTableColumn } from 'sag-table';

import { MarginService } from '../../services/margin.service';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { BrandModel } from '../../models/margin-brand.model';
import { MarginBrandRequestModel } from '../../models/margin-brand-search-request.model';
import { MarginBrandFormModalComponent } from '../margin-brand-form-modal/margin-brand-form-modal.component';
import { MarginImportModalComponent } from '../margin-import-modal/margin-import-modal.component';

import { SubSink } from 'subsink';
import { AppModalService } from 'src/app/core/services/app-modal.service';

@Component({
  selector: 'connect-margin-brands',
  templateUrl: './margin-brands.component.html',
  styleUrls: ['./margin-brands.component.scss']
})
export class MarginBrandsComponent implements OnInit, OnDestroy {
  @ViewChild('colActions', { static: true }) colActions: TemplateRef<any>;

  columns: SagTableColumn[] = [];
  tableCallback = null;
  tableRequest = null;
  brands: BrandModel[] = [];
  params: MarginBrandRequestModel = new MarginBrandRequestModel();
  pageable: any;

  bsModalRef: BsModalRef;
  readonly spinnerSelector = '.brand-management';
  subs = new SubSink();
  numberOfElements: number;
  currentPage: number = 0;

  private importModalRef: BsModalRef;

  constructor (
    private modalService: BsModalService,
    private marginService: MarginService,
    private appModal: AppModalService
  ) { }

  ngOnInit() {
    this.buildColumns();
  }

  ngOnDestroy() {
    if (this.bsModalRef) {
      this.bsModalRef.hide();
    }

    if (this.importModalRef) {
      this.importModalRef.hide();
    }

    this.subs.unsubscribe();
  }

  searchTableData({ request, callback }) {
    this.tableCallback = callback;
    this.tableRequest = request;
    this.handleSearchRequest();
    this.getBrands();
  }

  deleteBrand(brand: BrandModel) {
    this.bsModalRef = this.modalService.show(SagConfirmationBoxComponent, {
      class: 'modal-md clear-brand-modal',
      ignoreBackdropClick: true,
      initialState: {
        message: 'MARGIN_MANAGE.DELETE_BRAND.MSG',
        messageParams: { name: brand.name },
        okButton: 'COMMON_LABEL.YES',
        cancelButton: 'COMMON_LABEL.NO',
        close: () => {
          this.handleDelete(brand);
        }
      }
    });
  }

  edit(data: any) {
    this.bsModalRef = this.modalService.show(MarginBrandFormModalComponent, {
      ignoreBackdropClick: true,
      initialState: {
        title: 'MARGIN_MANAGE.EDIT_BRAND',
        brand: data.brand || data, // data.brand for editing default brand
        callback: () => {
          const sub = this.modalService.onHidden.subscribe((res) => {
            SpinnerService.start(this.spinnerSelector);
            sub.unsubscribe();

            if (data.callback) {
              data.callback();
            } else {
              const page = this.getPage();
              this.resetTable(page);
            }
          });
        }
      }
    });

    this.appModal.modals = this.bsModalRef;
  }

  create() {
    this.bsModalRef = this.modalService.show(MarginBrandFormModalComponent, {
      ignoreBackdropClick: true,
      initialState: {
        callback: () => {
          const sub = this.modalService.onHidden.subscribe(() => {
            SpinnerService.start(this.spinnerSelector);
            sub.unsubscribe();
            const page = this.getPage();

            this.resetTable(page);
          });
        }
      }
    });

    this.appModal.modals = this.bsModalRef;
  }

  openImportModal() {
    this.importModalRef = this.modalService.show(MarginImportModalComponent, {
      class: 'modal-user-form  modal-lg',
      ignoreBackdropClick: false,
      initialState: {
        importObserver: (file) => this.marginService.importBrandData(file),
        onDownloadTemplate: () => this.exportTemplate(),
        onExport: () => this.exportData(),
        onUploadComplete: () => this.reloadData()
      },
    });

    this.appModal.modals = this.importModalRef;
  }

  reloadData() {
    this.resetTable();
  }

  exportData() {
    this.marginService.exportBrandData(this.params).subscribe();
  }

  exportTemplate() {
    this.marginService.exportBrandCsvTemplate().subscribe();
  }

  private handleDelete(brand: BrandModel) {
    SpinnerService.start(this.spinnerSelector);
    this.subs.sink = this.marginService.deleteBrand(brand.id)
      .pipe(
        finalize(() => SpinnerService.stop(this.spinnerSelector))
      )
      .subscribe(data => {
        const page = this.getPage(true);
        this.resetTable(page);
      });
  }

  private handleSearchRequest() {
    const { filter, sort, page } = this.tableRequest;

    this.params = {
      ...this.params,
      brandName: filter.name || null,
      orderDescByBrandName: sort && sort.direction === 'desc' || false
    };

    this.pageable = {
      ...this.pageable,
      page: page - 1
    };
  }

  private resetTable(page?: number) {
    this.pageable.page = page || 0;
    this.getBrands();
  }

  private getBrands() {
    SpinnerService.start();
    this.subs.sink = this.marginService.getBrands(this.params, this.pageable)
      .pipe(
        finalize(() => SpinnerService.stop())
      ).subscribe((data: any) => {
        this.brands = data.content.map(tour => new BrandModel(tour));
        this.numberOfElements = data.numberOfElements;
        this.currentPage = data.number;

        if (this.tableCallback) {
          this.tableCallback({
            rows: this.brands,
            page: data.number + 1,
            totalItems: data.totalElements,
            itemsPerPage: data.size,
          })
        }
      }, error => {
        this.tableCallback({
          rows: [],
          page: 0
        })
      });
  }

  private buildColumns() {
    this.columns = [
      {
        id: 'name',
        i18n: 'MARGIN_MANAGE.BRAND',
        width: '30%',
        sortable: false,
        filterable: true
      },
      {
        id: 'margin1',
        i18n: '1',
        width: '10%',
        sortable: false,
        filterable: false,
        cellClass: 'text-center margin-value-10 margin-value',
        class: 'text-center margin-value-10'
      },
      {
        id: 'margin2',
        i18n: '2',
        width: '10%',
        sortable: false,
        filterable: false,
        cellClass: 'text-center margin-value-10 margin-value',
        class: 'text-center margin-value-10'
      },
      {
        id: 'margin3',
        i18n: '3',
        width: '10%',
        sortable: false,
        filterable: false,
        cellClass: 'text-center margin-value-10 margin-value',
        class: 'text-center margin-value-10'
      },
      {
        id: 'margin4',
        i18n: '4',
        width: '10%',
        sortable: false,
        filterable: false,
        cellClass: 'text-center margin-value-10 margin-value',
        class: 'text-center margin-value-10'
      },
      {
        id: 'margin5',
        i18n: '5',
        width: '10%',
        sortable: false,
        filterable: false,
        cellClass: 'text-center margin-value-10 margin-value',
        class: 'text-center margin-value-10'
      },
      {
        id: 'margin6',
        i18n: '6',
        width: '10%',
        sortable: false,
        filterable: false,
        cellClass: 'text-center margin-value-10 margin-value',
        class: 'text-center margin-value-10'
      },
      {
        id: 'margin7',
        i18n: '7',
        width: '10%',
        sortable: false,
        filterable: false,
        cellClass: 'text-center margin-value-10 margin-value',
        class: 'text-center margin-value-10'
      },
      {
        id: 'action',
        i18n: ' ',
        filterable: false,
        sortable: false,
        width: '10%',
        cellTemplate: this.colActions,
        cellClass: 'text-center align-items-center',
        class: 'margin-action'
      },
    ] as SagTableColumn[];
  }

  private getPage(deleteMode = false) {
    if (this.numberOfElements === 1 && deleteMode) {
      const prevPage = this.currentPage - 1;
      return prevPage > 0 ? prevPage : 0;
    }

    return this.currentPage;
  }
}
