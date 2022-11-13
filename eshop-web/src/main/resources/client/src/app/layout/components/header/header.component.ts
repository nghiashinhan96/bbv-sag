import { Component, OnInit } from '@angular/core';
import { AffiliateEnum } from 'sag-common';
import { UserService } from 'src/app/core/services/user.service';

@Component({
    selector: 'connect-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
    sb = AffiliateEnum.SB;
    
    constructor(
        public userService: UserService
    ) { }

    ngOnInit() {
    }
}
