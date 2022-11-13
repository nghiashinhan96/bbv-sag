import { Component, OnInit, Input } from '@angular/core';
import { NonMerkmaleAttribute } from '../../models/non-merkmale-filter-attribute.model';

@Component({
    selector: 'sag-article-list-non-merkmale-filter-attribute',
    templateUrl: 'non-merkmale-attributes.component.html',
    styleUrls: ['non-merkmale-attributes.component.scss']
})
export class SagArticleListNonMerkmaleAttributesComponent implements OnInit {

    @Input() attributes: NonMerkmaleAttribute;

    isCollapsed: boolean;

    ngOnInit() { }
}
