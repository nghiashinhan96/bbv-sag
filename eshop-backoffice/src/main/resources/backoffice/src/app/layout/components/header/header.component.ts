import { Component, OnInit, OnDestroy } from '@angular/core';
import { User } from 'src/app/core/models/user.model';
import { AuthService } from 'src/app/authentication/services/auth.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
    selector: 'backoffice-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit, OnDestroy {
    user: User;
    userSub: Subscription;

    constructor(
        private router: Router,
        private authService: AuthService
    ) { }

    ngOnInit() {
        this.userSub = this.authService.user$.subscribe(user => {
            this.user = user;
        });
    }

    ngOnDestroy() {
        this.userSub.unsubscribe();
    }

    logout() {
        this.authService.logout();
        this.router.navigate(['login']);
    }
}
