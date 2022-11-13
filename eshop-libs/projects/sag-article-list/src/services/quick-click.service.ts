import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/internal/operators/map';
import { includes } from 'lodash';

import { CategoryModel } from 'sag-article-detail';

import { ArticleListConfigService } from './article-list-config.service';

@Injectable()
export class QuickClickService {
    quickClick: CategoryModel[][];
    constructor(
        private http: HttpClient,
        private config: ArticleListConfigService
    ) { }

    getData(vehicleId: string) {
        const url = `${this.config.baseUrl}categories/quick/${vehicleId}`;
        return this.http.get(url, { observe: 'body' }).pipe(map((res: any[]) => {
            this.quickClick = res.map(group => group.map(cate => new CategoryModel(cate)));
            // this.quickClick = [];
            // this.extractTree(tree, this.quickClick);
            return this.quickClick;
        }));
    }

    findCateIds(node: CategoryModel, cateIds: any[]) {
        if (!node.children) {
            if (node.qflag === 1) {
                cateIds.push(node.id);
            }
        } else {
            node.children.forEach(child => this.findCateIds(child, cateIds));
        }
    }

    checkQuickClick(cateIds: string[]) {
        // #913 If a branch has 3 leaves and only one leaf is seleted, then this branch will be selected in quickclick
        if (this.quickClick) {
            this.quickClick.forEach(col => {
                col.forEach(row => {
                    (row.children || []).forEach(category => {
                        const ids = [];
                        this.findCateIds(category, ids);
                        category.isChecked = ids.findIndex(id => includes(cateIds, id)) !== -1;
                    });
                });
            });
        }
    }

    reset() {
        (this.quickClick || []).forEach(col => {
            col.forEach(row => {
                (row.children || []).forEach(category => {
                    category.isChecked = false;
                });
            });
        });
    }
}
