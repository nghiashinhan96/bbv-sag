import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/core/services/user.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs/internal/Observable';
import { Router } from '@angular/router';

@Component({
  selector: 'autonet-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  leftHeaders = [
    {
      icon: 'assets/icons/home.svg',
      titleCode: 'MENU.HOME',
      link: '/cars',
      disabled: false
    },
    {
      icon: 'assets/icons/catalogues.svg',
      titleCode: 'MENU.CATELOGUES',
      link: '/catelogues',
      disabled: false
    },
    {
      icon: 'assets/icons/administration.svg',
      titleCode: 'MENU.ADMINISTRATION',
      link: '/administration',
      disabled: false
    }, {
      icon: 'assets/icons/shopping-cart.svg',
      titleCode: 'MENU.SHOPPING-BASKET',
      link: '/shopping-basket',
      disabled: false
    },
    {
      icon: 'assets/icons/price-offers.svg',
      titleCode: 'MENU.PRICE-OFFERS',
      link: '/price-offers',
      disabled: false
    }
  ];
  user$: Observable<any>;
  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.user$ = this.userService.userSettingObserver.pipe(map(user => {
      if (!!user) {
        return {
          customerName: user.customerName,
          userCode: user.userCode,
          userName: user.username
        };
      }
      return null;
    }));
  }

  logout() {
    this.userService.logout();
  }

  isActive(url): boolean {
    return this.router.isActive(url, false);
  }
}
