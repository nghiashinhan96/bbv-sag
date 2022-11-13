import { Injectable } from '@angular/core';
import { ArticleSortUtil } from '../utils/article-sort.util';
import { OilOption } from '../models/oil-cate-option.model';
import { OilType } from '../enums/oil-type.enum';
import { ArticleListStorageService } from './article-list-storage.service';

@Injectable()
export class OilService {
    constructor(private storage: ArticleListStorageService) { }

    filterOilGroup(group: OilOption[]) {
        group.forEach(g => {
            this.markAsDeleted(g);
            this.deleteMarkedAsDeleted(g);
        });
        group = group.filter(g => !g.markedAsDeleted);
        group.forEach(g => {
            const { cateId, cateName } = this.getCateId(g);
            g.cateId = cateId;
            g.cateName = cateName;
        });
        const oils = this.storage.selectedOils;
        const selectedOil = [];
        const data = ArticleSortUtil.groupBy(group, oil => [oil.cateId])
            .filter((g: OilOption[]) => {
                const oil = (oils || []).find(o => o.key === g[0].cateId);
                if (oil) {
                    selectedOil.push({ key: oil.key, value: oil.value });
                    return false;
                }
                if (g.length === 1) {
                    const oil = g[0];
                    if (oil.type === OilType.Application) {
                        selectedOil.push({ key: oil.cateId, value: oil.guid });
                        return false;
                    }
                    return true;
                }
                return true;
            });
        return { data, selectedOil };
    }
    private markAsDeleted(currOption: OilOption) {
        if (currOption.type === OilType.Application) {
            currOption.markedAsDeleted = false;
            return currOption.markedAsDeleted;
        }
        if (currOption.type === OilType.Choice) {
            if (!currOption.choice || !currOption.choice.option || currOption.choice.option.length === 0) {
                currOption.markedAsDeleted = true;
                return currOption.markedAsDeleted;
            }
        }
        if (currOption.choice && currOption.choice.option && currOption.choice.option.length > 0) {
            currOption.markedAsDeleted = true;
            currOption.choice.option.forEach((op) => {
                const marked = this.markAsDeleted(op);
                currOption.markedAsDeleted = marked && currOption.markedAsDeleted;
            });
            return currOption.markedAsDeleted;
        }
    }

    private deleteMarkedAsDeleted(currOption: OilOption) {
        if (currOption.choice && currOption.choice.option && currOption.choice.option.length > 0) {
            currOption.choice.option = currOption.choice.option.filter(op => !op.markedAsDeleted);
            currOption.choice.option.forEach((op, i) => {
                this.deleteMarkedAsDeleted(op);
            });
        }
    }

    private getCateId(currOption: OilOption) {
        if (currOption.cateId) {
            return { cateId: currOption.cateId, cateName: currOption.cateName };
        }
        if (currOption.choice && currOption.choice.option && currOption.choice.option.length > 0) {
            return this.getCateId(currOption.choice.option[0]);
        } else {
            return { cateId: '', cateName: '' };
        }
    }
}
