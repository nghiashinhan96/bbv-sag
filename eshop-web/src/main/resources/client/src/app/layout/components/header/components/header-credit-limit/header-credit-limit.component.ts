import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';

import { PopoverDirective } from 'ngx-bootstrap/popover';
import { takeUntil } from 'rxjs/operators';
import { Subject } from 'rxjs';

import { AxCreditLimit, CreditLimitService } from 'src/app/core/services/credit-limit.service';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-header-credit-limit',
    templateUrl: './header-credit-limit.component.html'
})
export class HeaderCreditLimitComponent implements OnInit, OnDestroy {
    isShown = false;
    creditInfo: AxCreditLimit;
    isCz = AffiliateUtil.isCz(environment.affiliate);

    private destroy$ = new Subject();
    @ViewChild('pop', { static: true }) miniBasketRef: PopoverDirective;

    constructor(
        public creditLimitService: CreditLimitService,
        public appContextService: AppContextService,
    ) {
    }

    ngOnInit() {
        this.creditLimitService.creditLimitInfo$
            .pipe(takeUntil(this.destroy$))
            .subscribe(creditInfo => {
                this.creditInfo = creditInfo || {};
            });
    }

    ngOnDestroy(): void {
        this.destroy$.next(true);
        this.destroy$.complete();
    }
}
