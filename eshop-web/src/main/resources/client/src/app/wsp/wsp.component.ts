import { Component, OnInit, OnDestroy } from '@angular/core';
import { SubSink } from 'subsink';
import { cloneDeep, get } from 'lodash';
import { WspTreeModel, IWspLink } from './models/wsp-tree.model';
import { UniversalPartService } from './services/wsp.service';
import { Router, ActivatedRoute, NavigationEnd, NavigationStart } from '@angular/router';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { catchError, finalize, map, tap } from 'rxjs/operators';
import { CategoryTreeService, WSP_SHOP_LINK, BrandFilterItem } from 'sag-article-list';
import { CategoryTile, ArticleModel, WspCategoryModel, BarFilter, BarFilterOption, Criteria, WspMatchModel, ArticleBroadcastKey } from 'sag-article-detail';
import {
  ArticleInContextService
} from 'sag-in-context';
import { WSP_LINK_TYPES } from 'sag-article-list';
import { UserService } from 'src/app/core/services/user.service';
import { UserDetailOwnSetting } from '../core/models/user-detail-own-setting.mode';
import { of, Subscription } from 'rxjs';
import { WspFilterRequest } from './models/wsp-filter-request.model';
import { TILE_MODE, VIEW_MODE } from './services/constants';
import { BrandData, BrandPrio, GenArtTile } from './models/genart-tile.model';
import { AppStorageService } from '../core/services/app-storage.service';
import * as _ from 'lodash';
import { WspAnalyticService } from '../analytic-logging/services/wsp-analytics.service';
import { SearchEventTarget } from 'sag-article-list';
import { WspEventType } from '../analytic-logging/enums/event-type.enums';
import { ARTICLE_SEARCH_MODE } from 'sag-article-search';
import { TranslateService } from '@ngx-translate/core';
import { FavoriteCommonService } from '../shared/connect-common/services/favorite-common.service';
import { BroadcastService } from 'sag-common';

@Component({
  selector: 'connect-wsp',
  templateUrl: './wsp.component.html',
  styleUrls: ['./wsp.component.scss']
})
export class WspComponent implements OnInit, OnDestroy {
  treeId: string;
  nodeId: string;
  gaId: string;
  trees: WspTreeModel[];
  tiles;
  VIEW_MODE = VIEW_MODE;
  TILE_MODE = TILE_MODE;
  viewMode;
  userDetailSetting: UserDetailOwnSetting;

  private subs = new SubSink();
  private paramNodeSelected = false;
  private selectedFromFavoriteList = true;
  private previousUrl: string;
  private articleObserver: Subscription;
  private event: any;

  currentTreeActive: string;
  selectedCate;
  selectedGenArt: WspFilterRequest;
  tileMode = TILE_MODE.NODE_MODE;
  isReady = false;
  categoryLabel = 'COMMON_LABEL.FILTER';

  isGetArticleListFromLeafNode = true;
  genArts: GenArtTile[] = [];
  breadcrumbs: any[] = [];
  spinnerName = '.wsp-content';

  currentFocusedCate: HTMLElement;

  currentFavoriteArticle: ArticleModel[] = null;
  error = null;
  CATEGORY_NOT_FOUND = "WSP.CATEGORY_NOT_FOUND";

  private headerSelectedFavoriteItem = null;
  private loadingFavoritedArticle = false;
  brandPrios: BrandPrio[] = [];
  criterionFromInclude: Criteria[] = [];
  criterionFromExclude: Criteria[] = [];
  isCameFromFavoriteSearch = false;
  isHandleFocused: boolean = false;
  genArtTextUsedForBreadcrumb = '';
  isCameFromGenartBreadcrumbClick = false;
  selectedBrandData: BrandData;

  constructor(
    private universalPartService: UniversalPartService,
    private router: Router,
    private activeRouter: ActivatedRoute,
    private categoryTreeService: CategoryTreeService,
    private articleInContextService: ArticleInContextService,
    private userService: UserService,
    private appStorage: AppStorageService,
    private wspAnalyticService: WspAnalyticService,
    private translateService: TranslateService,
    private favoriteCommonService: FavoriteCommonService,
    private broadcastService: BroadcastService
  ) {
    this.subs.sink = this.categoryTreeService.selectedCategoriesObservable
      .subscribe(data => {
        this.getTiles(data);
      });

    this.subs.sink = this.categoryTreeService.readyStateObservable.subscribe(isReady => {
      this.isReady = isReady;
    });
  }

  ngOnInit() {
    this.treeId = this.activeRouter.snapshot.queryParams['treeId'] || null;
    this.nodeId = this.activeRouter.snapshot.queryParams['nodeId'];
    this.gaId = this.activeRouter.snapshot.queryParams['gaId'] || null;
    this.selectedFromFavoriteList = !!this.gaId;
    this.isCameFromFavoriteSearch = !!this.gaId;
    this.userDetailSetting = this.userService.userDetail && this.userService.userDetail.settings;
    this.headerSelectedFavoriteItem = this.appStorage.selectedFavoriteItem;
    this.appStorage.selectedFavoriteItem = null;

    if (this.previousUrl !== this.router.url) {
      this.initData();
    }

    this.subs.sink = this.router.events
      .subscribe((event) => {
        if (event instanceof NavigationEnd) {
          this.previousUrl = event.urlAfterRedirects;
          this.treeId = this.activeRouter.snapshot.queryParams['treeId'] || null;
          this.nodeId = this.activeRouter.snapshot.queryParams['nodeId'];
          this.gaId = this.activeRouter.snapshot.queryParams['gaId'] || null;
          this.selectedFromFavoriteList = !!this.gaId;
          this.isCameFromFavoriteSearch = !!this.gaId;
          this.headerSelectedFavoriteItem = this.appStorage.selectedFavoriteItem;
          this.appStorage.selectedFavoriteItem = null;
          this.isHandleFocused = false;
          this.initData();
        }
        if (event instanceof NavigationStart) {
          if (event.url.indexOf('/wsp') === -1) {
              this.appStorage.clearBasketItemSource();
          }
        }
      });
    document.body.addEventListener('click', this.listenToBodyClick.bind(this));
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
    this.categoryTreeService.destroyWspCategoryTree();
    document.body.removeEventListener('click', this.listenToBodyClick.bind(this))
    this.appStorage.selectedFavoriteItem = null;
  }

  updateTree(tree: WspTreeModel) {
    if (this.currentTreeActive !== this.treeId) {
      this.categoryTreeService.unCheckAllCate();
      this.categoryTreeService.reSelectedCategorybyId();
    } else {
      this.treeId = tree.treeId || this.treeId;
      this.currentTreeActive = this.treeId;
    }

    this.categoryLabel = tree.treeName;
    this.resetArticleView();
    this.breadcrumbs = [];
    this.genArtTextUsedForBreadcrumb = '';
  }

  onRootBreadcrumb() {
    this.categoryTreeService.unCheckAllCate();
    this.categoryTreeService.reSelectedCategorybyId();
    this.resetArticleView();
    this.breadcrumbs = [];
    this.genArtTextUsedForBreadcrumb = '';
  }

  selectLink(item: IWspLink, cateId?) {
    let continueStep = false;
    this.event = item.event || this.event;
    switch (item.type) {
      case WSP_LINK_TYPES.service:
        if (item.attr === 'thule') {
          const link = this.userDetailSetting && this.userDetailSetting.externalUrls && this.userDetailSetting.externalUrls.thule || '';
          window.open(link, '_self');
        }

        return;

      case WSP_LINK_TYPES.shop:
        if (WSP_SHOP_LINK[item.attr]) {
          this.router.navigate([WSP_SHOP_LINK[item.attr]]);
        }

        return;

      case WSP_LINK_TYPES.link:
      case WSP_LINK_TYPES.partner:
        if (item.link) {
          window.open(item.link, '_blank');

          if (cateId) {
            this.categoryTreeService.checkOnSingleCategory(cateId, false, false);
          }
        }
        return;

      default:
        this.nodeId = item.link;

        if (this.nodeId) {
          const spinner = SpinnerService.start(this.spinnerName);
          this.subs.sink = this.categoryTreeService.getRequestedTreeFromNodeId(this.nodeId).pipe(
            catchError(error => of({})),
            finalize(() => SpinnerService.stop(spinner))
          ).subscribe(requestedTree => {
            let requestedTreeId = get(requestedTree, 'treeId');

            if (requestedTreeId && requestedTreeId !== this.treeId) {
              this.jumpToNodeIdWithRequestedTreeId(this.nodeId, requestedTreeId);
            }
          });

          this.expandNodeByNodeId(this.nodeId);
          return;
        }

        continueStep = true;

        break;
    }

    return continueStep;
  }

  private async handleArticleData(data) {
    if (data && data.length == 0) {
      if (this.universalPartService.isFilteringArticle) {
        const result = cloneDeep(this.universalPartService.currentArticleData);
        if (result.values[0]) {
          result.values[0].values = [];
        }
        this.articleInContextService.emitData([result]);
        return;
      }
      this.articleInContextService.emitData([]);
      return;
    }
    const articles = (data || []).map(item => new ArticleModel(item));
    this.selectedCate.genArts = this.brandPrios;
    const result = this.universalPartService.groupArticle(articles, this.selectedCate);
    this.articleInContextService.emitData(result as any);
  }

  onBrandFilterStateChange(checkedBrands) {
    const spinner = SpinnerService.start(this.spinnerName);
    this.universalPartService.currentGetArticleListSub = this.universalPartService.handleCheckedBrandsChanged(checkedBrands).pipe(
      finalize(() => {
        SpinnerService.stop(spinner);
      })
    )
      .subscribe((data: any) => {
        this.handleFilterRequest(data);
      });
  }

  onBarFilterStateChange(checkedFilter) {
    const spinner = SpinnerService.start(this.spinnerName);
    this.universalPartService.currentGetArticleListSub = this.universalPartService.handleCheckedBarFilterChanged(checkedFilter, this.criterionFromInclude).pipe(
      finalize(() => {
        SpinnerService.stop(spinner);
      })
    )
      .subscribe((data: any) => {
        this.handleFilterRequest(data);
      });
  }

  onBrandSelected(data) {
    if (!this.selectedGenArt) {
      return;
    }
    this.universalPartService.selectedGenArtName = data.gaText;
    this.universalPartService.selectedGenArtIds = [data.gaId];
    this.selectedGenArt.wsp_search_request.includeCriteria.genArts = [data.gaId];
    this.selectedGenArt.suppliers = data.suppliers;
    this.event = data.event;
    this.getWspArticle();
    this.selectedBrandData = (({gaText, gaId, suppliers}) => {return {gaText, gaId, suppliers}})(data);
    setTimeout(() => {
      this.genArtTextUsedForBreadcrumb = data.gaText;
    });
  }

  onClickBreadcrumd(breadcrum) {
    this.categoryTreeService.unCheckAllCate();
    if (breadcrum.isLeaf) {
      this.categoryTreeService.checkOnSingleCategory(breadcrum.nodeId, true, true, SearchEventTarget.BREADCRUMBS);
    } else {
      this.expandNodeByNodeId(breadcrum.nodeId);
    }
  }

  rootHeaderClick() {
    this.articleInContextService.emitData(undefined);
    this.handleGenArtsData({ genArts: this.genArts });
  }

  handleCategorySearch(data) {
    this.sendSearchCategoryEvent(data);
  }

  handleFocusedNodeWhenJumpFromAnotherTree(data) {
    if (this.isHandleFocused) {
      return;
    }

    const cateEl = document.getElementById(`cat-${this.nodeId}`);
    if (cateEl) {
      const isCateElChildClassSingleRef = Array.from(cateEl.children).find(child => child.classList.contains("selected"));

      if (!isCateElChildClassSingleRef) {
        this.toggleFocusedCategory(cateEl);
        this.isHandleFocused = true;
      }
    }
  }

  onLoadMore(data) {
    this.event = {
      type: WspEventType.SHOW_MORE
    };
    this.sendSearchArticleEvent(data);
  }

  onSelectFavoriteItem(item: any) {
    this.error = null;
    if (item.vehicleId) {
      this.favoriteCommonService.navigateToFavoriteVehicle(item);
    } else if (item.treeId && item.leafId) {
      this.selectedFromFavoriteList = true;
      this.isCameFromFavoriteSearch = true;
      this.appStorage.selectedFavoriteLeaf = item;
      this.router.navigate(['wsp'], {
        queryParams: {
          treeId: item.treeId,
          nodeId: item.leafId,
          gaId: item.gaId
        }
      });
    } else {
      this.favoriteCommonService.navigateToFavoriteArticle(item);
    }
  }

  private handleBrandFilterData(gaId, brands) {
    if (!brands && brands.length == 0) {
      return;
    }

    const uniqBrands = brands.map(brand => {
      return {
        name: brand.id,
        checked: brand.checked
      };
    });
    const newBrandItem = new BrandFilterItem({
      key: this.universalPartService.selectedGenArtName,
      gaID: gaId,
      brands: uniqBrands
    });

    this.universalPartService.emitBrandFilterData([newBrandItem]);
  }

  private handleBarFilterData(criterions) {
    if (!criterions || criterions.length == 0) {
      return;
    }
    if (!this.categoryTreeService.currentSelectedWspCategory) {
      return;
    }
    const genArtFilters: BarFilter[] = this.getGenArtFilters(this.categoryTreeService.currentSelectedWspCategory, this.selectedGenArt);
    const barFilters = [];
    (genArtFilters || []).forEach(filter => {
      const filterCaid = filter.filterCaid;
      const showFilterBar = filter.filterBar;
      const filterSort = filter.filterSort;
      const filterDefault = filter.filterDefault;
      const filterOpen = filter.filterOpen;
      const filterData = criterions.find(cr => cr.id == filterCaid);

      if (!filterData || !filterData.children) {
        return;
      }

      let checkedDefault = false;
      if (filterDefault) {
        const filter = filterData.children.map(f => {
          if (f.id == filterDefault) {
            if (f.amountItem > 0) {
              checkedDefault = true;
            }
          }
        })
      }

      const filterCn = filterData.description;
      let options = filterData.children.map(filter => {
        return new BarFilterOption({
          cvp: filter.id,
          cid: filterCaid,
          cn: filterCn,
          checked: checkedDefault && filter.id == filterDefault
        })
      });

      const isAllNumber = !((options || []).some(op => !parseFloat(op.cvp)));
      if (isAllNumber) {
        options = (options || []).sort((opA, opB) => opA.cvp - opB.cvp);
      } else {
        options = (options || []).sort((opA, opB) => opA.cvp > opB.cvp ? 1 : -1);
      }

      const barFilterData = new BarFilter({ name: filterCn, options, filterBar: showFilterBar, filterSort: filterSort, filterOpen })
      barFilters.push(barFilterData);
    })
    this.universalPartService.emitBarFilterData(barFilters);
  }

  private handleFilterRequest(data) {
    if (!data) {
      this.handleArticleData([]);
      return;
    }

    const genArts = data.filters.gen_arts;
    if (genArts && genArts[0]) {
      const language = (this.appStorage.appLangCode || '').toUpperCase();
      this.universalPartService.selectedGenArtName = genArts[0].descriptions && genArts[0].descriptions[language] || this.universalPartService.selectedGenArtName;
    } else {
      this.universalPartService.selectedGenArtName = this.selectedCate.nodeName;
    }

    if (data.articles && data.articles.content) {
      this.handleArticleData(data.articles.content);
    }
  }

  private showGenArtTiles(data) {
    let genArts: GenArtTile[] = (data || []).map(g => new GenArtTile(g));
    this.genArts = genArts;
    const gaId = this.activeRouter.snapshot.queryParams['gaId'];
    const nodeId = this.activeRouter.snapshot.queryParams['nodeId'];
    if (!gaId
      || (gaId && nodeId !== this.selectedCate.id)
      || (!this.selectedFromFavoriteList)) {
      this.viewMode = VIEW_MODE.tile;
      this.tileMode = TILE_MODE.GENART_MODE;
      if (this.genArts.length > 0) {
        this.goTop();
      }
      return;
    }

    this.selectedFromFavoriteList = false;
    const tile = genArts.find(ga => ga.gaId === parseInt(gaId));

    if (!tile && nodeId == this.selectedCate.id) {
      this.showNotFoundCategoryError();
      return;
    }
    this.error = null;
    const selectedTileData = {
      gaId: tile.gaId,
      gaText: tile.gaText,
      brandIds: [],
      suppliers: [],
      event: {
        type: WspEventType.GENART,
        target: SearchEventTarget.TILE_CONTENT
      }
    };
    this.onBrandSelected(selectedTileData);
  }

  private getWspFilterOption() {
    const request = cloneDeep(this.selectedGenArt) || null;
    if(request) {
      request.wsp_search_request.includeCriteria.criterion = this.criterionFromInclude;
      request.wsp_search_request.excludeCriteria.criterion = this.criterionFromExclude;
    }    

    this.universalPartService.getWspFilterOptions(request)
      .subscribe((data: any) => {
        if (!data) {
          return;
        }
        if (data.filters && data.filters.criterion) {
          this.handleBarFilterData(data.filters.criterion);
        }

        if (data.filters && data.filters.suppliers) {
          let gaSuppliers = [];
          if (!this.isGetArticleListFromLeafNode) {
            const gaId = this.selectedGenArt.wsp_search_request.includeCriteria.genArts[0];
            const seletectedGenArt = (this.genArts || []).find(ga => ga.gaId == gaId);
            const seletectBrandName = this.selectedGenArt.suppliers[0];
            if (seletectedGenArt) {
              gaSuppliers = (seletectedGenArt.brands || []).map(brand => {
                return { id: brand.brandName, checked: brand.brandName == seletectBrandName }
              })
            }
          }
          const suppliers = this.isGetArticleListFromLeafNode ? data.filters.suppliers : gaSuppliers;

          if (data.filters.gen_arts && data.filters.gen_arts[0]) {
            this.handleBrandFilterData(data.filters.gen_arts[0].id, suppliers);
          }
        }
      });
  }

  private getWspArticle() {
    if (this.categoryTreeService.currentSelectedWspCategory) {
      const { include, exclude } = cloneDeep(this.categoryTreeService.currentSelectedWspCategory);
      const genArtFilters: BarFilter[] = this.getGenArtFilters(this.categoryTreeService.currentSelectedWspCategory, this.selectedGenArt);
      const criteria: Criteria[] = this.getIncludeCriteria(include, exclude, genArtFilters);
      this.selectedGenArt.wsp_search_request.includeCriteria.criterion = criteria;
      this.selectedGenArt.wsp_search_request.excludeCriteria.criterion = exclude.criteria;
      delete this.selectedGenArt.wsp_search_request.includeCriteria.criteria;
      delete this.selectedGenArt.wsp_search_request.excludeCriteria.criteria;
    }
    this.viewMode = null;
    const spinner = SpinnerService.start(this.spinnerName);
    this.universalPartService.getWspArticles(this.selectedGenArt)
      .pipe(
        finalize(() => {
          SpinnerService.stop(spinner);
          this.viewMode = VIEW_MODE.article;
        })
      )
      .subscribe((data: any) => {
        this.handleFilterRequest(data);
        this.getWspFilterOption();
        this.sendSearchArticleEvent(data);
        this.goTop();
      });
  }

  private handleGenArtsData(data) {
    setTimeout(() => {
      this.getBreadcrumbs();
    });

    if (!data) {
      return;
    }

    const { genArts } = data;
    if (genArts && genArts.length < 2) {
      this.genArts = genArts;
      this.isGetArticleListFromLeafNode = true;
      this.setTheEventForFavoriteLeafNode();
      this.getWspArticle();
      return;
    }

    this.isGetArticleListFromLeafNode = false;
    this.showGenArtTiles(genArts);
    this.sendSearchArticleEvent(null);
  }

  private setTheEventForFavoriteLeafNode() {
    if (this.selectedFromFavoriteList) {
      this.event = {
        type: WspEventType.LEAF_NODE,
        target: SearchEventTarget.NAV_TREE
      };
      this.selectedFromFavoriteList = false;
    }
  }

  private initData() {
    const spinner = SpinnerService.start(this.spinnerName);
    this.subs.sink = this.universalPartService.getCompactTrees()
      .pipe(
        finalize(() => {
          if (!this.loadingFavoritedArticle) {
            SpinnerService.stop(spinner);
          }
        })
      )
      .subscribe(data => {
        if (data) {
          this.handleInitData(data);
        }
      });
  }

  private handleInitData(data) {
    this.trees = data;

    if (this.treeId === null) {
      const tree = (this.trees || []).find(tree => tree.treeExternalServiceType === '' || tree.treeExternalServiceType === null)

      this.treeId = tree && tree.treeId || null;
      this.categoryLabel = tree && tree.treeName || null;
    }

    if (this.treeId) {
      this.trees.forEach(tree => {
        if (tree.treeId === this.treeId) {
          tree.active = true;
          this.categoryLabel = tree.treeName;
        } else {
          tree.active = false;
        }
      });
    }

    if (this.currentTreeActive !== this.treeId) {
      this.currentTreeActive = this.treeId;
      this.categoryTreeService.destroyWspCategoryTree();
      this.categoryTreeService.markedTreeState(false);
    }

    if (this.categoryTreeService.rootCategory && this.currentTreeActive === this.treeId) {
      this.categoryTreeService.unCheckAllCate();
      if (this.nodeId) {
        this.expandNodeByNodeId(this.nodeId, true);
        const target = this.activeRouter.snapshot.queryParams['target'];
        this.categoryTreeService.checkOnSingleCategory(this.nodeId, true, true, target);
      }
    }

    if (this.headerSelectedFavoriteItem) {
      this.loadingFavoritedArticle = true;
      this.onSelectFavoriteItem(this.headerSelectedFavoriteItem);
      this.headerSelectedFavoriteItem = null;
    }
  }

  private resetArticleView(shouldKeepSelectedGenArt = false) {
    this.articleInContextService.emitData(undefined);
    this.universalPartService.resetPaginationData();
    this.resetFilterData(shouldKeepSelectedGenArt);
  }

  private resetFilterData(shouldKeepSelectedGenArt = false) {
    if (!shouldKeepSelectedGenArt) {
      this.selectedGenArt = null;
    }
    this.universalPartService.isFilteringArticle = false;
    this.universalPartService.emitBarFilterData(null);
    this.universalPartService.emitBrandFilterData(null);
  }

  private getTiles(data) {
    const spinner = SpinnerService.start(this.spinnerName);
    if (data && data.cateIds && data.cateIds.length > 0) {
      if (this.nodeId && !this.paramNodeSelected) {
        this.paramNodeSelected = true;
        this.categoryTreeService.checkOnSingleCategory(this.nodeId);
        return;
      }
      let cate;
      if (this.categoryTreeService.rootCategory.id === data.cateIds[0]) {
        cate = this.categoryTreeService.rootCategory;
      } else {
        cate = this.categoryTreeService.getCateDetailById(data.cateIds[0]);
      }

      if (!cate) {
        this.showNotFoundCategoryError();
        SpinnerService.stop(spinner);
        return;
      }
      this.error = null;
      setTimeout(() => {
        this.selectedCate = cate;

        if (this.selectedCate) {
          this.universalPartService.selectedGenArtName = this.selectedCate.nodeName;
        }
        if (cate && cate.children && cate.children.length) {
          this.handleOtherNode(cate);
        } else {
          this.event = this.mapEventFromTarget(data.target) || this.event;
          this.handleLeafNode(cate, spinner);
        }
      });
    }
    if (!this.loadingFavoritedArticle) {
      SpinnerService.stop(spinner);
    }
  }

  private handleLeafNode(cate, spinner) {
    SpinnerService.stop(spinner);
    if (cate) {
      const nodeData = <IWspLink>{
        type: cate.nodeExternalType,
        attr: cate.nodeExternalServiceAttribute,
        link: cate.nodeExternalServiceAttribute
      };
      const { include, exclude } = cloneDeep(cate);
      include.criterion = include.criteria;
      exclude.criterion = exclude.criteria;
      
      if (this.gaId && include.genArts.length == 1 && include.genArts[0] !== parseInt(this.gaId) && this.nodeId == cate.id) {
        this.showNotFoundCategoryError();
        return;
      }

      const storedNodeId = this.nodeId;

      const continueStep = this.selectLink(nodeData, cate.id);
      if (!continueStep) {
        return;
      }

      if (this.gaId && include.genArts.length === 0 && storedNodeId == cate.id) {
        this.showNotFoundCategoryError();
        return;
      }

      this.resetArticleView();

      if (include && !include.genArts || (include.genArts.length == 0)) {
        this.articleInContextService.emitData([]);
        return;
      }

      this.error = null;
      if (cate.filterDefault) {
        include.criterion = [{ cid: cate.filterCaid, cvp: cate.filterDefault }]
      }

      if (this.articleObserver) {
        this.articleObserver.unsubscribe();
      }
      const spinner = SpinnerService.start(this.spinnerName);
      const language = this.appStorage.appLangCode;
      this.universalPartService.selectedGenArtIds = include.genArts;

      include.treeId = this.treeId;
      include.leafId = cate.id;

      this.selectedGenArt = new WspFilterRequest({ wsp_search_request: { includeCriteria: include, excludeCriteria: exclude, language } });
      this.articleObserver = this.universalPartService.getUniversalParts(this.selectedGenArt.wsp_search_request)
        .pipe(
          tap(() => {
            SpinnerService.stop(spinner);
          })
        )
        .subscribe(data => {
          if (data) {
            this.brandPrios = (data.brandPrios || []).map(item => new BrandPrio(item));
          }
          this.handleGenArtsData(data);
        })
    }
    this.subs.sink = this.articleObserver;
  }

  private handleOtherNode(cate) {
    if (cate.tiles && cate.tiles.length > 0) {
      this.tileMode = TILE_MODE.NODE_MODE;
      this.tiles = (cate.tiles || []).map(item => new CategoryTile(item));
      this.nodeId = cate.id || null;
      this.viewMode = VIEW_MODE.tile;
      this.goTop();
    } else {
      const root = this.categoryTreeService.rootCategory;
      if (root) {
        this.tileMode = TILE_MODE.NODE_MODE;
        this.tiles = (root.tiles || []).map(item => new CategoryTile(item));
        this.viewMode = VIEW_MODE.tile;
        this.nodeId = cate.id || root.id;
      }
    }
  }

  private goTop() {
    window.scrollTo(0, 0);
  }

  private getBreadcrumbs() {
    const categories = _.filter((this.categoryTreeService.categories || []),
      _.flow(
        _.partialRight(_.some, { isChecked: true })
      ));

    if (this.error) {
      return;
    }

    if (categories.length > 0) {
      this.breadcrumbs = [];
      this.genArtTextUsedForBreadcrumb = '';
      this.formatCategory([...categories]);
    }
  }

  private formatCategory(categories: WspCategoryModel[] = []) {
    (categories || []).forEach(category => {
      if (category.isChecked) {
        this.breadcrumbs.push({
          nodeId: category.id,
          nodeName: category.nodeName,
          isLeaf: category.children.length > 0 ? false : true
        });
      }

      this.formatCategory(category.children);
    });
  }

  private expandNodeByNodeId(nodeId: string, isInit = false) {
    const category = this.categoryTreeService.categoriesArray.find(cate => cate.id === nodeId);

    if (!category && this.selectedFromFavoriteList) {
      this.showNotFoundCategoryError();
    }

    if (category) {
      this.error = null;
      this.categoryTreeService.unCheckAllCate();
      this.categoryTreeService.checkOnCategoryTree([category.id]);

      setTimeout(() => {
        if (category.children && category.children.length > 0) {
          this.scrollToElement(category.id);
        }
        if (!isInit) {
          this.categoryTreeService.checkOnSingleCategory(category.id);
        }
      });
    }
  }

  private listenToBodyClick(e) {
    this.toggleFocusedCategory(this.currentFocusedCate);
  }

  private scrollToElement(nodeId: string) {
    const cateEl = document.getElementById(`cat-${nodeId}`);
    if (cateEl) {

      cateEl.scrollIntoView({
        behavior: 'smooth',
        block: 'center'
      });
      setTimeout(() => {
        this.toggleFocusedCategory(cateEl);
      }, 200);
    }
  }

  private toggleFocusedCategory(cateEl: HTMLElement) {
    if (!cateEl) {
      return;
    }
    const content = cateEl.firstElementChild;

    if (cateEl.classList.contains('focused')) {
      cateEl.classList.remove('focused')
      if (content) {
        content.classList.remove('focused');
      };
      this.currentFocusedCate = null;
      return;
    }

    this.currentFocusedCate = cateEl;
    if (content) {
      content.classList.add('focused');
      cateEl.classList.add('focused');
    }
  }

  private sendSearchCategoryEvent(data) {
    if (!data) {
      return;
    }
    const request: any = {
      wspCurrentTreeId: this.treeId,
      wspCurrentNodeId: get(this.selectedCate, 'id'),
      wspSearchTermEntered: data.keyword,
      wspNumberOfNodeResults: data.totalElements || 0
    };
    this.wspAnalyticService.sendEventData(request);
  }

  private sendSearchArticleEvent(data) {
    if (!this.event) {
      return;
    }
    const request: any = {
      wspCurrentTreeId: this.treeId,
      wspCurrentNodeId: get(this.selectedCate, 'id'),
      wspNumberOfArticleResults: get(data, 'articles.totalElements', null)
    };
    switch (this.event.type) {
      case WspEventType.LEAF_NODE:
        request.wspLeafNodeClicked = this.event.target;
        break;
      case WspEventType.LINK:
        request.wspStdTileLinkClicked = this.event.target;
        break;
      case WspEventType.GENART:
        request.wspGenartTileLinkClicked = this.event.target;
        break;
      case WspEventType.SHOW_MORE:
        const articles = document.querySelectorAll('connect-wsp-article-list sag-article-detail');
        request.wspShowMoreClicked = articles && articles.length || 0;
        break;
    }
    this.event = null;
    this.wspAnalyticService.sendEventData(request, this.isCameFromFavoriteSearch, this.isCameFromGenartBreadcrumbClick);
    this.isCameFromFavoriteSearch = false;
    this.isCameFromGenartBreadcrumbClick = false; 
  }

  private mapEventFromTarget(target: string) {
    switch (target) {
      case SearchEventTarget.FREE_TEXT_SEARCH_RESULTS:
      case SearchEventTarget.NAV_SEARCH_RESULTS:
      case SearchEventTarget.NAV_TREE:
      case SearchEventTarget.BREADCRUMBS:
        return {
          type: WspEventType.LEAF_NODE,
          target
        };
      default:
        return null;
    }
  }

  private showNotFoundCategoryError() {
    this.viewMode = null;
    this.breadcrumbs = [];
    this.genArtTextUsedForBreadcrumb = '';
    this.selectedFromFavoriteList = false;
    this.isCameFromFavoriteSearch = false;
    this.goTop();
    const error = this.translateService.instant(this.CATEGORY_NOT_FOUND);
    if (this.appStorage.selectedFavoriteLeaf) {
      this.error = `${error} <br/> ${this.appStorage.selectedFavoriteLeaf.title}`;
      return;
    }
    this.error = `${error}`;
  }

  private getGenArtFilters(currentSelectedWspCategory: WspCategoryModel, selectedGenArt: WspFilterRequest) {
    const { genArtBarFilters } = currentSelectedWspCategory;
    let gaId;
    const genArts = selectedGenArt && selectedGenArt.wsp_search_request && selectedGenArt.wsp_search_request.includeCriteria && selectedGenArt.wsp_search_request.includeCriteria.genArts || null;
    if (genArts && genArts.length && genArts.length > 0) {
      gaId = genArts[0];
    }
    let genArtFilters: BarFilter[] = [];
    if (gaId) {
      const genArtFilterObj = (genArtBarFilters || []).find(item => item.gaid === gaId.toString());
      if (genArtFilterObj) {
        genArtFilters = genArtFilterObj.filter || [];
      } else {
        const genArtFilterObjWithEmptyGaid = (genArtBarFilters || []).find(item => item.gaid === '' || item.gaid === null);
        if (genArtFilterObjWithEmptyGaid) {
          genArtFilters = genArtFilterObjWithEmptyGaid.filter || [];
        }
      }
    }
    return genArtFilters;
  }

  private getIncludeCriteria(include: WspMatchModel, exclude: WspMatchModel, genArtFilters: BarFilter[]) {
    let criteria: Criteria[] = include.criteria || [];
    this.criterionFromInclude = [...criteria];
    const criterionFromExclude = exclude.criteria || [];
    this.criterionFromExclude = [...criterionFromExclude];
    const filterDefaultList: Criteria[] = [];
    genArtFilters.forEach(filter => {
      const filterDefault = filter.filterDefault;
      const filterCaid = filter.filterCaid;
      if (filterDefault) {
        const existedCriteriaFromInclude = criteria.find(item => item.cid === filterCaid && item.cvp === filterDefault);
        if (existedCriteriaFromInclude) {
          filterDefaultList.push(existedCriteriaFromInclude);
        }
      }
    });
    if (filterDefaultList.length > 0) {
      const filterDefaultCidList = filterDefaultList.map(item => item.cid);
      const differentCriterion = criteria.filter(item => filterDefaultCidList.indexOf(item.cid) === -1);
      criteria = [...filterDefaultList, ...differentCriterion];
    }
    return criteria;
  }

  private jumpToNodeIdWithRequestedTreeId(nodeId: string, requestedTreeId: string) {
    this.router.navigate(['/wsp'], {
      queryParams: {
        treeId: requestedTreeId,
        nodeId: nodeId
      },
      relativeTo: this.activeRouter
    });
  }

  onClickGenartBreadcrumb() {
    if (!this.selectedBrandData) {
      return;
    }
    this.isCameFromGenartBreadcrumbClick = true;
    this.resetArticleView(true);
    this.universalPartService.selectedGenArtName = this.selectedBrandData.gaText;
    this.universalPartService.selectedGenArtIds = [this.selectedBrandData.gaId];
    this.selectedGenArt.wsp_search_request.includeCriteria.genArts = [this.selectedBrandData.gaId];
    this.selectedGenArt.suppliers = this.selectedBrandData.suppliers;
    this.event = this.mapEventFromTarget(SearchEventTarget.BREADCRUMBS);
    this.getWspArticle();
  }
}
