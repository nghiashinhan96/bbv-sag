import { Component, OnInit, Input, ViewChild, AfterViewInit } from '@angular/core';
import { Router } from '@angular/router';
import { CategoryModel } from 'sag-article-detail';
import { ProjectId } from 'sag-common';
import { SearchEventTarget } from '../../enums/search-event-target.enum';
import { ArticleListConfigService } from '../../services/article-list-config.service';
import { CategoryTreeService } from '../../services/category-tree.service';

@Component({
    selector: 'sag-article-list-category-tree-item',
    templateUrl: './category-tree-item.component.html',
    styleUrls: ['./category-tree-item.component.scss']
})
export class SagArticleListCategoryTreeItemComponent implements OnInit, AfterViewInit {

    @Input() category: any;
    @Input() level: number;
    @Input() isSingleLink: boolean;

    @ViewChild('checkboxRef', { static: false }) checkboxRef: any;

    isShow = false;
    criteriaStr: any;
    brands: any;
    rootCategoryId: string;

    constructor(
        public router: Router,
        private categoryTreeService: CategoryTreeService,
        private config: ArticleListConfigService
    ) { }

    ngOnInit() {
        this.rootCategoryId = this.categoryTreeService.rootCategory && this.categoryTreeService.rootCategory.id || null;
        this.category.level = +this.level || 0;
        this.handleExpand();
    }

    ngAfterViewInit(): void {
        if (this.checkboxRef) {
            this.category.ref = this.checkboxRef;
            this.checkboxRef.checked = this.category.isChecked;
        }
    }

    setMarginStyles(index) {
        return `${-(index * 1)}rem`;
    }

    setPaddingStyles(index) {
        return `${(index * 1) + 0.5}rem`;
    }

    openLeafRoot() {
        if(this.category.link) {
            if(this.category.navigate) {
                this.navigateTo(this.category.navigate);
            } else {
                window.open(this.category.link, this.category.target);
            }
        } else {
            this.selectCategory(this.category);
        }
    }

    navigateTo(page: string) {
        this.router.navigate([page]);
    }

    searchArticles(element) {
        const id = this.category.id;
        const isChecked = element.target.checked;
        if (isChecked && this.config.projectId === ProjectId.AUTONET) {
            this.categoryTreeService.unCheckAllCate(false);
        }
        this.category.isChecked = isChecked;
        this.categoryTreeService.emitSearchRequest(id, isChecked);
        // this.quickClickService.checkQuickClick([id]);
        // if unchecked one category -> emit list part remove redundant parts
        if (isChecked) {
            // this.quickClickService.selectOilCheckBox(gaidValue);
            // this.categoryService.checkRecursive(this.el.nativeElement);
            // let selectedGaIds = this.categoryService.findSelectedGaids();
            // let selectedCateIds = this.categoryService.findSelectedCategoryIds();
            // let gaids = { gaIds: selectedGaIds, cateIds: selectedCateIds, currentGaIds: gaidValue };
            // this.seletedCategoriesEmitter.emit(gaids);
        } else {
            // this.categoryService.handleUncheckEvent(gaidValue);
        }
    }

    toggleCollapse() {
        this.category.show = !this.category.show;
        this.category.rendered = true;

        if(this.isSingleLink) {
            this.categoryTreeService.checkOnSingleCategory(this.category.id, this.category.show);
        }
    }

    handleExpand() {
        // Expand categories has open : 1 after loaded
        // #3019: AT-AX: Minimise the leftside search menu on "Alle Artikel" as default for all users
        if (!this.category.ignoredOpen && this.category.open === '1') {
            this.category.show = true;
            this.category.rendered = true;
        }
    }

    selectCategory(category: CategoryModel, isChecked = true) {
        this.categoryTreeService.unCheckAllCate();
        this.categoryTreeService.checkOnSingleCategory(category.id, isChecked, true, SearchEventTarget.NAV_TREE);
    }
}
