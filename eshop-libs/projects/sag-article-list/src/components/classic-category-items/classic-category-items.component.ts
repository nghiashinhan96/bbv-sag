import { OnInit, Component, Input, Output, EventEmitter } from '@angular/core';
import { CategoryModel } from 'sag-article-detail';
import { CLASSIC_CATEGORY_STEP } from '../../enums/classic-category.enum';

@Component({
    selector: 'sag-article-list-classic-category-items',
    templateUrl: 'classic-category-items.component.html',
    styleUrls: ['classic-category-items.component.scss']
})
export class SagArticleListClassicCategoryItemsComponent implements OnInit {
    @Input() categories: CategoryModel[] = [];
    @Input() step = CLASSIC_CATEGORY_STEP.ONE;

    @Output() selectItem = new EventEmitter<any>();
    @Output() oilCheck = new EventEmitter<any>();
    constructor() { }

    ngOnInit() {

    }

    handleToggle(category: CategoryModel) {
        category.show = !category.show;
    }

    handleSelect(data) {
        this.selectItem.emit(data);
    }

    selectSingle(event: any, category: CategoryModel) {
        const isChecked = event.target.checked;
        this.selectCategory(category, this.step, event.target.checked);
        if (category.oilCate) {
            if (isChecked) {
                this.oilCheck.emit({ checked: [category.id], unChecked: [] });
            } else {
                this.oilCheck.emit({ checked: [], unChecked: [category.id] });
            }
        }
    }

    selectAll(category: CategoryModel, step = this.step) {
        if (!category.children) {
            return;
        }

        const checked = category.children.filter(item => item.isChecked).length !== category.children.length;

        category.children.forEach(item => {
            this.selectCategory(item, step, checked, true);
            this.selectAllLeaf(item, checked);
        });

        const allOilCates = this.getAllOilCates(category.children);
        if (allOilCates.length > 0) {
            this.oilCheck.emit({
                checked: allOilCates.filter(cate => cate.isChecked).map(cate => cate.id),
                unChecked: allOilCates.filter(cate => !cate.isChecked).map(cate => cate.id),
            });
        }
    }

    private selectAllLeaf(category: CategoryModel, checked: boolean) {
        if (category.children) {
            category.children.forEach(item => {
                this.selectAllLeaf(item, checked);
            });
        } else {
            this.selectCategory(category, CLASSIC_CATEGORY_STEP.TWO, checked);
        }
    }

    private selectCategory(category: CategoryModel, step: CLASSIC_CATEGORY_STEP, checked: boolean, optionAll: boolean = false) {
        if (category.isChecked === checked) {
            return;
        }

        this.handleSelect({
            category,
            step,
            checked,
            optionAll
        });
    }

    private getAllOilCates(categories: CategoryModel[]) {
        let results = [];
        categories.forEach(category => {
            if (category.children) {
                results = [...results, ...this.getAllOilCates(category.children)];
            } else if (category.oilCate) {
                results = [...results, category];
            }
        });
        return results;
    }
}
