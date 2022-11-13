import { CommonConfigService, BroadcastService, GrossPriceKeyPipe } from 'sag-common';
import {
    SagCurrencyPipe,
    SagNumericService,
    SagNumericPipe,
    FixedNumericPipe,
    SagCurrencyStorageService
} from 'sag-currency';
import { HaynesLinkHandleService, LabourTimeService, HaynesProIntegrationService, HaynesProService } from 'sag-haynespro';
import {
    SagCustomPricingConfigService,
    SagCustomPricingService,
    SagCustomPricingStorageService
} from 'sag-custom-pricing';
import {
    ArticleDetailConfigService,
    SagArticleDetailIntegrationService,
    ArticlesService,
    SagArticleDetailStorageService
} from 'sag-article-detail';
import {
    SagGtmotiveConfigService,
    GtmotiveService
} from 'sag-gtmotive';
import {
    MultiLevelCategoryTreeService,
    ArticleListStorageService,
    QuickClickService,
    CategoryTreeService,
    ClassicCategoryService,
    VehicleSearchService,
    ArticleHaynesProIntegrationImpl,
    ArticleDetailConfigServiceImpl,
    ArticleListConfigService,
    OilService,
    BrandPriorityAvailService,
    SagArticleListIntegrationService
} from 'sag-article-list';
import {
    SagInContextConfigService,
    SagInContextStorageService,
    SagInContextIntegrationService,
    SagArticleListConfigServiceImpl,
    SagInContextResolver,
    ArticleInContextService
} from 'sag-in-context';
import { SagLibThuleConfigService } from 'sag-thule';
import { SagTableConfigService } from 'sag-table';
import { SagVehicleListConfigService, VehicleListService } from 'sag-vehicle-list';
import { ArticleSearchConfigService, ArticleSearchService, ArticleSearchStorageService } from 'sag-article-search';
import { ArticleInContextListConfigService } from './article-in-context-result-list/services/article-in-context-list-config.service';
import { ConnectCommonConfigService } from './core/services/common-config.service';
import { GtmotiveConfigService } from './gtmotive/services/gtmotive-config.service';
import { ArticlesInContextIntegrationService } from './article-in-context-result-list/services/articles-in-context-integration.service';
import { ThuleConnectConfigService } from './thule/thule-connect-config.service';
import { ArticleDetailIntegrationService } from './core/services/article-detail-integration.service';
import { ArticleSearchConfigIntegrateService } from './core/services/article-search-config.service';
import { ArticleListIntegrationService } from './core/services/article-list-integration.service';

export const LIB_PROVIDERS = [
    // common
    BroadcastService,
    GrossPriceKeyPipe,
    { provide: CommonConfigService, useClass: ConnectCommonConfigService },
    // currency
    SagCurrencyStorageService,
    SagNumericPipe,
    SagCurrencyPipe,
    FixedNumericPipe,
    SagNumericService,
    // haynespro
    HaynesLinkHandleService,
    LabourTimeService,
    HaynesProService,
    // custom pricing
    { provide: SagCustomPricingConfigService, useClass: ArticleInContextListConfigService },
    SagCustomPricingStorageService,
    SagCustomPricingService,
    // article-detail
    ArticlesService,
    // gtmotive
    GtmotiveService,
    // article-list
    OilService,
    MultiLevelCategoryTreeService,
    ArticleListStorageService,
    ArticleSearchStorageService,
    QuickClickService,
    CategoryTreeService,
    ClassicCategoryService,
    VehicleSearchService,
    ArticleDetailConfigServiceImpl,
    ArticleHaynesProIntegrationImpl,
    BrandPriorityAvailService,
    // incontext
    SagInContextStorageService,
    SagArticleDetailStorageService,
    SagArticleListConfigServiceImpl,
    SagInContextResolver,
    ArticleInContextService,
    // arrticle search
    { provide: ArticleSearchConfigService, useClass: ArticleSearchConfigIntegrateService },
    ArticleSearchService,
    // vehicle list
    { provide: SagVehicleListConfigService, useClass: ArticleInContextListConfigService },
    VehicleListService,
    // sag table
    { provide: SagTableConfigService, useClass: ArticleInContextListConfigService },
    // overrite from main app
    { provide: HaynesProIntegrationService, useClass: ArticleHaynesProIntegrationImpl },

    { provide: SagGtmotiveConfigService, useClass: GtmotiveConfigService },

    { provide: ArticleDetailConfigService, useClass: ArticleInContextListConfigService },
    { provide: SagArticleDetailIntegrationService, useClass: ArticleDetailIntegrationService },
    { provide: ArticleListConfigService, useClass: ArticleInContextListConfigService },
    { provide: SagInContextConfigService, useClass: ArticleInContextListConfigService },
    { provide: SagInContextIntegrationService, useClass: ArticlesInContextIntegrationService },
    { provide: SagLibThuleConfigService, useExisting: ThuleConnectConfigService },
    { provide: SagArticleListIntegrationService, useClass: ArticleListIntegrationService }
];
