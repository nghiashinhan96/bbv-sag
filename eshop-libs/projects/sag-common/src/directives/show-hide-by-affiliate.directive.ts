import { Directive, TemplateRef, ViewContainerRef, OnInit, Input } from '@angular/core';
import { CommonConfigService } from '../services/common-config.service';

@Directive({
    selector: '[sagCommonShowHideByAff]'
})
export class SagCommonShowHideByAffiliateDirective implements OnInit {
    @Input() sagCommonShowHideByAff: string[] = [];
    @Input() sagCommonShowHideByAffShow = true;

    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainerRef: ViewContainerRef,
        private config: CommonConfigService
    ) { }

    ngOnInit() {
        const containAff = this.sagCommonShowHideByAff.find(aff => aff === this.config.affiliate);
        const show = (this.sagCommonShowHideByAffShow && containAff) || (!this.sagCommonShowHideByAffShow && !containAff);
        if (show) {
            this.viewContainerRef.createEmbeddedView(this.templateRef);
        } else {
            this.viewContainerRef.clear();
        }
    }
}
