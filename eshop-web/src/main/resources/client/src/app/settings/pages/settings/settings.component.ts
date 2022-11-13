import { Component, OnInit, OnDestroy } from '@angular/core';
import { ProfileTabModel } from '../../models/profile-tab.model';
import { ProfileBusinessService } from '../../services/profile-business.service';
import { UserService } from 'src/app/core/services/user.service';
import { switchMap } from 'rxjs/operators';
import { Subscription, of } from 'rxjs';

@Component({
    selector: 'connect-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit, OnDestroy {

    tabs: ProfileTabModel[];
    private sub: Subscription;
    constructor(private profileBusinessService: ProfileBusinessService, private userService: UserService) { }

    ngOnInit() {
        this.sub = this.userService.userDetail$.pipe(
            switchMap((userDetail) => {
                if (userDetail) {
                    this.tabs = this.profileBusinessService.getSettingTabList(userDetail);
                    return this.userService.getPaymentSetting();
                }
                return of(null);
            }),
        ).subscribe();
    }

    ngOnDestroy(): void {
        if (this.sub) {
            this.sub.unsubscribe();
        }
    }
}
