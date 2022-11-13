import { Component, Input, OnInit, TemplateRef, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { get } from 'lodash';
import { BsModalRef } from "ngx-bootstrap/modal";
import { SagTableColumn, SagTableControl, SagTableRequestModel, SagTableResponseModel, TablePage } from "sag-table";
import { UserService } from "src/app/core/services/user.service";

@Component({
    selector: 'connect-header-return-articles-modal',
    templateUrl: './header-return-articles-modal.component.html'
})
export class HeaderReturnArticlesModalComponent implements OnInit {
    @Input() term: string;
    @Input() articles: any[];
    @Input() onSelectItem: any;

    @ViewChild('colCheckbox', { static: true }) colCheckbox: TemplateRef<any>;
    
    enhancedUsedPartsReturnProcEnabled: boolean;
    columns = [];
    
    constructor(
        public bsModalRef: BsModalRef,
        private router: Router,
        private userService: UserService
    ) { }

    ngOnInit(): void {
        this.enhancedUsedPartsReturnProcEnabled = get(this.userService, 'userDetail.settings.enhancedUsedPartsReturnProcEnabled', false);
        this.articles = this.articles.reduce((acc, cur) => {
            cur.disabled = this.enhancedUsedPartsReturnProcEnabled && cur.isDepotReturnArticle;

            if (cur.attachedTransactionReferences && cur.attachedTransactionReferences.length > 0) {
                const attaches = (cur.attachedTransactionReferences || []).map(attach => {
                    attach.root = { ...cur };
                    attach.disabled = this.enhancedUsedPartsReturnProcEnabled && attach.isDepotReturnArticle;
                    return attach;
                });
                return [...acc, cur, ...attaches];
            }

            return [...acc, cur];
        }, []);

        this.columns = [
            {
                id: '',
                i18n: '',
                filterable: false,
                sortable: false,
                cellTemplate: this.colCheckbox,
                class: 'align-middle py-0',
                cellClass: 'align-middle py-0'
            },
            {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.ID',
                id: 'articleId',
                sortable: false,
                filterable: true,
                width: '150px'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.NAME',
                id: 'articleName',
                sortable: false,
                filterable: true,
                width: '200px'
            },{
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.ASSIGNMENT',
                id: 'orderNr',
                sortable: false,
                filterable: true,
                width: '150px'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.AMOUNT',
                id: 'quantity',
                sortable: false,
                filterable: false,
                cellClass: 'text-right'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.REST',
                id: 'returnQuantity',
                sortable: false,
                filterable: false,
                cellClass: 'text-right'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.STORAGE_LOCATION',
                id: 'branchId',
                sortable: false,
                filterable: true
            }
        ] as SagTableColumn[];
    }

    get rowsSelected() {
        return this.articles.some(a => a.selected);
    }

    onClickArticle(article) {
        if (this.rowsSelected) {
            return;
        }
        this.bsModalRef.hide();
        this.selectItem(article, true);
    }

    onCheckArticle(event, article) {
        article.selected = event.target.checked;
    }

    submit() {
        this.articles.forEach(art => {
            if (art.selected) {
                this.selectItem(art);
            }
        });
        this.bsModalRef.hide();
        this.router.navigateByUrl('/return');
    }

    private selectItem(article, redirect = false) {
        if (article.root) {
            const root = article.root;
            delete article.root;
            this.onSelectItem(root, article, redirect);
        } else {
            this.onSelectItem(article, null, redirect);
        }
    }
}