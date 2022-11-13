import { Component, Input, OnInit, ViewChild, TemplateRef, OnDestroy, EventEmitter, ViewChildren, QueryList, AfterViewInit } from '@angular/core';
import { ArticleModel, CZ_AVAIL_STATE, LibUserSetting } from 'sag-article-detail';
import { UserService } from 'src/app/core/services/user.service';
import { CZ_PRICE_TYPE } from 'src/app/shared/cz-custom/enums/article.enums';
import { SubSink } from 'subsink';
import { SagNumericService } from 'sag-currency';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-cz-article-detail-price',
    templateUrl: 'article-detail-price.component.html'
})
export class ArticleDetailPriceComponent implements OnInit, AfterViewInit, OnDestroy {
    @ViewChild('pop', { static: false }) pop: any;
    @ViewChild('popoverContentRef', { static: false }) popoverContentTemplateRef: TemplateRef<any>;
    @ViewChildren(PopoverDirective) popovers: QueryList<PopoverDirective>;

    @Input() article: ArticleModel;
    @Input() popoversChanged = new EventEmitter<any>();
    @Input() type: CZ_PRICE_TYPE;
    @Input() currentStateVatConfirm: boolean;

    availState = CZ_AVAIL_STATE;
    priceType = CZ_PRICE_TYPE;

    userSetting: LibUserSetting;
    popoverDelay = 0;
    currency: string;
    isEhCz = AffiliateUtil.isEhCz(environment.affiliate);
    isShowNetPrice = false;
    isShowVat = false;

    private subs = new SubSink();

    constructor (
        public userService: UserService,
        private numericService: SagNumericService
    ) { }

    ngOnInit() {
        this.currency = this.numericService.getCurrency();

        this.subs.sink = this.userService.userPrice$.subscribe(userSetting => {
            if (!userSetting) {
                return;
            }
            this.userSetting = userSetting;
            this.popoverDelay = userSetting.mouseOverFlyoutDelay || 0;
            this.setVisibleOfNetPrice();
            this.initialShowVat();
        });
    }

    ngAfterViewInit() {
        if (this.popovers && this.popoversChanged) {
            this.popoversChanged.emit(this.popovers);
            this.subs.sink = this.popovers.changes.subscribe(pops => {
                this.popoversChanged.emit(pops);
            })
        }
    }

    ngOnDestroy() {
        this.subs.unsubscribe();
    }

    setVisibleOfNetPrice() {
        this.isShowNetPrice = this.userSetting.currentStateNetPriceView;
        if (this.isEhCz) {
            this.isShowNetPrice = this.userSetting.netPriceView && this.userSetting.fcUserCanViewNetPrice;
        }
    }

    initialShowVat() {
        const inclVATSettings = this.userSetting && this.userSetting.vatTypeDisplayConvert;
        this.isShowVat = inclVATSettings && inclVATSettings.list;
    }
}
