import { Location } from '@angular/common';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BroadcastService } from 'sag-common';
import { SagCurrencyModule } from 'sag-currency';
import { SagCustomPricingModule, SagCustomPricingService } from 'sag-custom-pricing';
import { HaynesLinkHandleService, HaynesProService, LabourTimeService } from 'sag-haynespro';
import { OrderAnalyticService } from 'src/app/analytic-logging/services/order-analytic.service';
import { ShoppingBasketAnalyticService } from 'src/app/analytic-logging/services/shopping-basket-analytic.service';
import { ArticleListSearchStorageService } from 'src/app/article-list/services/article-list-storage.service';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { AppModalService } from 'src/app/core/services/app-modal.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { ArticleShoppingBasketService } from 'src/app/core/services/article-shopping-basket.service';
import { CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { ShoppingBasketService } from 'src/app/core/services/shopping-basket.service';
import { UserService } from 'src/app/core/services/user.service';
import { ActiveDmsProcessor } from 'src/app/dms/context/active-dms-processor';
import { CustomerSearchService } from 'src/app/home/service/customer-search.service';
import { OciService } from 'src/app/oci/services/oci.service';
import { AppContextServiceStub } from 'src/tests/services/AppContextService.stub';
import { AppStorageServiceStub } from 'src/tests/services/AppStorageService.stub';
import { BroadcastServiceStub } from 'src/tests/services/BroadcastService.stub';
import { CustomerSearchServiceStub } from 'src/tests/services/CustomerSearchService.stub';
import { OciServiceStub } from 'src/tests/services/OciService.stub';
import { ShoppingBasketServiceStub } from 'src/tests/services/ShoppingBasketService.stub';
import { UserServiceStub } from 'src/tests/services/UserService.stub';
import { ShoppingCartService } from '../../services/shopping-cart.service';
import { ShoppingOrderService } from '../../services/shopping-order.service';

import { ShoppingCartComponent } from './shopping-cart.component';

xdescribe('ShoppingCartComponent', () => {
  let component: ShoppingCartComponent;
  let fixture: ComponentFixture<ShoppingCartComponent>;
  let appStorageServiceStub = null;
  let shoppingBasketServiceStub = null;
  let appContextServiceStub = null;
  let userServiceStub = null;
  let creditLimitServiceSpy = jasmine.createSpyObj('CreditLimitService', ['']);
  let customerSearchServiceStub = null;
  let locationSpy = jasmine.createSpyObj('Location', ['']);
  let broadcastServiceStub = null;
  let activeDmsProcessorSpy = jasmine.createSpyObj('ActiveDmsProcessor', ['getDmsInfo', 'getDmsErrorMessage']);
  let articleListSearchStorageServiceSpy = jasmine.createSpyObj('ArticleListSearchStorageService', ['']);
  let articleShoppingBasketServiceSpy = jasmine.createSpyObj('ArticleShoppingBasketService', ['']);
  let shoppingBasketAnalyticServiceSpy = jasmine.createSpyObj('ShoppingBasketAnalyticService', ['']);
  let ociServiceStub = null;
  let haynesProServiceSpy = jasmine.createSpyObj('HaynesProService', ['']);
  let labourTimeServiceSpy = jasmine.createSpyObj('LabourTimeService', ['']);
  let haynesLinkHandleServiceSpy = jasmine.createSpyObj('HaynesLinkHandleService', ['']);
  let shoppingOrderServiceSpy = jasmine.createSpyObj('ShoppingOrderService', ['']);
  let shoppingCartServiceSpy = jasmine.createSpyObj('ShoppingCartService', ['']);
  let sagCustomPricingServiceSpy = jasmine.createSpyObj('SagCustomPricingService', ['']);
  let orderAnalyticServiceSpy = jasmine.createSpyObj('OrderAnalyticService', ['']);
  let appModalServiceSpy = jasmine.createSpyObj('AppModalService', ['']);

  beforeEach(async(() => {
    appContextServiceStub = new AppContextServiceStub();
    appStorageServiceStub = new AppStorageServiceStub();
    shoppingBasketServiceStub = new ShoppingBasketServiceStub();
    userServiceStub = new UserServiceStub();
    customerSearchServiceStub = new CustomerSearchServiceStub();
    broadcastServiceStub = new BroadcastServiceStub();
    ociServiceStub = new OciServiceStub();

    TestBed.configureTestingModule({
      declarations: [ShoppingCartComponent],
      imports: [
        HttpClientTestingModule,
        ModalModule.forRoot(),
        SagCurrencyModule.forRoot(),
        FormsModule,
        ReactiveFormsModule,
        RouterTestingModule,
        ModalModule.forRoot(),
        SagCustomPricingModule.forRoot(),
        TranslateModule.forRoot()
      ],
      providers: [
        {
          provide: ShoppingBasketService,
          useValue: shoppingBasketServiceStub
        },
        {
          provide: AppContextService,
          useValue: appContextServiceStub
        },
        {
          provide: UserService,
          useValue: userServiceStub
        },
        {
          provide: CreditLimitService,
          useValue: creditLimitServiceSpy
        },
        {
          provide: AppStorageService,
          useValue: appStorageServiceStub
        },
        {
          provide: CustomerSearchService,
          useValue: customerSearchServiceStub
        },
        {
          provide: Location,
          useValue: locationSpy
        },
        {
          provide: BroadcastService,
          useValue: broadcastServiceStub
        },
        {
          provide: ActiveDmsProcessor,
          useValue: activeDmsProcessorSpy
        },
        {
          provide: ArticleListSearchStorageService,
          useValue: articleListSearchStorageServiceSpy
        },
        {
          provide: ArticleShoppingBasketService,
          useValue: articleShoppingBasketServiceSpy
        },
        {
          provide: ShoppingBasketAnalyticService,
          useValue: shoppingBasketAnalyticServiceSpy
        },
        {
          provide: OciService,
          useValue: ociServiceStub
        },
        {
          provide: HaynesProService,
          useValue: haynesProServiceSpy
        },
        {
          provide: LabourTimeService,
          useValue: labourTimeServiceSpy
        },
        {
          provide: HaynesLinkHandleService,
          useValue: haynesLinkHandleServiceSpy
        },
        {
          provide: ShoppingOrderService,
          useValue: shoppingOrderServiceSpy
        },
        {
          provide: ShoppingCartService,
          useValue: shoppingCartServiceSpy
        },
        {
          provide: SagCustomPricingService,
          useValue: sagCustomPricingServiceSpy
        },
        {
          provide: OrderAnalyticService,
          useValue: orderAnalyticServiceSpy
        },
        {
          provide: AppModalService,
          useValue: appModalServiceSpy
        }
      ]
    })
      .overrideTemplate(ShoppingCartComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingCartComponent);
    component = fixture.componentInstance;

    component.userService.userDetail = new UserDetail(null);

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
