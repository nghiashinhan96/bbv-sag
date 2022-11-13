import { Directive, TemplateRef, ViewContainerRef, OnInit, Input, OnDestroy } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { Subscription } from 'rxjs/internal/Subscription';
import { intersection } from 'lodash';

@Directive({
    selector: '[connectHasPermission]'
})
export class HasPermissionDirective implements OnInit, OnDestroy {


    @Input() connectHasPermission: string[] = [];
    private subscription: Subscription;
    constructor(
        private templateRef: TemplateRef<any>,
        private viewContainerRef: ViewContainerRef,
        private userService: UserService
    ) { }
    ngOnInit(): void {
        this.subscription = this.userService.hasPermissions(this.connectHasPermission).subscribe(hasPerms => {
            this.viewContainerRef.clear();
            if (hasPerms) {
                this.viewContainerRef.createEmbeddedView(this.templateRef);
                return;
            }
        });
    }

    ngOnDestroy(): void {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }
}
