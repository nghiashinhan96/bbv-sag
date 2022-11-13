import { Directive, ElementRef, Input, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { UserService } from 'src/app/core/services/user.service';
import { AffiliateUtil } from 'sag-common';
import { environment } from 'src/environments/environment';
import { MOTORBIKE_PERMISSION_CODE } from '../enums/motorbike-permission-code.enum';

@Directive({
    selector: '[connectEnabled]'
})
export class EnabledDirective implements OnInit, OnDestroy {
    @Input() connectEnabled: string;
    private subscription: Subscription;

    constructor(
        private el: ElementRef,
        private userService: UserService
    ) { }

    ngOnInit(): void {
        const isEh = AffiliateUtil.isEhCh(environment.affiliate) || AffiliateUtil.isEhAt(environment.affiliate) || AffiliateUtil.isEhCz(environment.affiliate);
        const isAffiliateAT = AffiliateUtil.isAffiliateAT(environment.affiliate);
        const style = isEh || (this.connectEnabled === MOTORBIKE_PERMISSION_CODE.MOTO && isAffiliateAT) ? 'hidden' : 'disabled';

        this.subscription = this.userService.userDetail$.subscribe(user => {
            if (!!user && !!user.permissions) {
                const permissions: any[] = user.permissions;
                const isEnabled = permissions.map(p => p.permission).find(code => this.connectEnabled === code);
                if (isEnabled) {
                    this.el.nativeElement.removeAttribute(style);
                    return;
                }
            }
            this.el.nativeElement.setAttribute(style, style);
        });
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }
}
