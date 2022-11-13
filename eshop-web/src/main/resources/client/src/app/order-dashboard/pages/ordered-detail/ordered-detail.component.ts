import { Component, OnInit, Input, ViewChild, TemplateRef, SimpleChanges, OnChanges } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { BsModalService } from 'ngx-bootstrap/modal';
import { SagTableColumn } from 'sag-table';
import {
  AffiliateUtil,
  getImages
} from 'sag-common';

import { FinalCustomerOrder } from 'src/app/settings/models/final-customer/final-customer-order.model';
import { FinalCustomerService } from 'src/app/settings/services/final-customer.service';
import { environment } from 'src/environments/environment';
import { FinalCustomerOrderItem } from 'src/app/settings/models/final-customer/final-customer-order-item.model';
import { SagCommonImgSliderModalComponent } from 'sag-common';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'connect-ordered-detail',
  templateUrl: './ordered-detail.component.html',
  styleUrls: ['./ordered-detail.component.scss']
})
export class OrderedDetailComponent implements OnInit, OnChanges {
  data = [];
  mode = 'offline';
  finalOrder: FinalCustomerOrder;

  defaultDateTimeFormat = 'dd.MM.yyyy HH:mm';
  columns: SagTableColumn[] = [];
  isAffiliateAT = AffiliateUtil.isBaseAT(environment.affiliate);

  isShowVat: boolean = false;

  private images = [];

  @ViewChild('articleNr', { static: true }) articleNr: TemplateRef<any>;
  @ViewChild('supplier', { static: true }) supplier: TemplateRef<any>;
  @ViewChild('image', { static: true }) image: TemplateRef<any>;
  @ViewChild('reference', { static: true }) reference: TemplateRef<any>;
  @ViewChild('grossPriceRef', { static: true }) grossPriceRef: TemplateRef<any>;
  @ViewChild('FCNetPriceRef', { static: true }) FCNetPriceRef: TemplateRef<any>;

  constructor(
    private finalCustomerService: FinalCustomerService,
    private route: ActivatedRoute,
    private router: Router,
    private modalService: BsModalService,
    public userService: UserService,
  ) { }

  ngOnInit() {
    const finalOrderId = this.route.snapshot.params['orderId'];
    this.finalCustomerService.getOrderDetail(finalOrderId)
      .subscribe(order => {
        this.finalOrder = order;
        this.data = this.finalOrder.items;
      });

    this.initTable();
    this.initialShowVat();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.userSetting && !changes.userSetting.firstChange) {
      this.initialShowVat();
    }
  }

  backToOrderedList() {
    this.router.navigate(['/order-dashboard', 'ordered']);
  }

  showImagesInfo(item: FinalCustomerOrderItem) {
    this.modalService.show(SagCommonImgSliderModalComponent, {
      ignoreBackdropClick: true,
      class: 'slick-slider-modal',
      initialState: {
        images: getImages(item.images)
      }
    });
  }

  private initTable() {
    this.columns = [
      {
        id: 'articleNr',
        i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.ARTICLE_NUMBER',
        filterable: false,
        sortable: false,
        cellTemplate: this.articleNr,
        width: '225px'
      }, {
        id: 'supplier',
        i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.MANUFACTURER',
        filterable: false,
        sortable: false,
        width: '100px',
        cellClass: 'supplier',
        cellTemplate: this.supplier
      }, {
        id: 'image',
        i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.IMAGE',
        filterable: false,
        sortable: false,
        width: '100px',
        cellTemplate: this.image
      }, {
        id: 'vehicleDesc',
        i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.VEHICLE',
        filterable: true,
        sortable: false,
        width: '220px'
      }, {
        id: 'grossPrice',
        i18n: 'ARTICLE.GROSS_VALUE',
        filterable: true,
        sortable: false,
        cellTemplate: this.grossPriceRef,
        cellClass: 'align-middle text-right',
        class: 'text-right',
        width: '200px'
      },{
        id: 'finalCustomerNetPrice',
         i18n: 'ARTICLE.FC_NET',
        filterable: true,
        sortable: false,
        cellTemplate: this.FCNetPriceRef,
        cellClass: 'align-middle text-right',
        class: 'text-right',
        width: '180px'
      },{
        id: 'quantity',
        i18n: 'ARTICLE.NUMBER',
        filterable: true,
        sortable: false,
        class: 'text-right',
        cellClass: 'text-right',
        width: '200px'
      },
      {
        id: 'reference',
        i18n: 'SETTINGS.MY_ORDER.DETAIL.HEADER.REFERENCE',
        filterable: true,
        sortable: false,
        width: '120px'
      }
    ];
  }

  private initialShowVat() {
    if (!this.userService.userPrice) {
      return;
    }
    const inclVATSettings = this.userService.userPrice && this.userService.userPrice.vatTypeDisplayConvert;
    this.isShowVat = inclVATSettings && inclVATSettings.list;
  }
}
