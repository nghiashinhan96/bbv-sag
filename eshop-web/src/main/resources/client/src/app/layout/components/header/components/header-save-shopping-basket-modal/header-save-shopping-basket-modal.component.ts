import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { AppContextService } from 'src/app/core/services/app-context.service';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { UserService } from 'src/app/core/services/user.service';

@Component({
  selector: 'connect-header-save-shopping-basket-modal',
  templateUrl: './header-save-shopping-basket-modal.component.html',
  styleUrls: ['./header-save-shopping-basket-modal.component.scss']
})
export class HeaderSaveShoppingBasketModalComponent implements OnInit {

  isShoppingBasketPage = false;
  constructor(
    public bsModalRef: BsModalRef,
    public appContextService: AppContextService,
    private router: Router,
    private userService: UserService,
    private appStorage: AppStorageService
  ) { }

  ngOnInit() {
    this.isShoppingBasketPage = this.router.url.indexOf('/shopping-basket') > -1;
  }

  onSaveAndClearShoppingBasket() {
    this.bsModalRef.hide();
    if (this.isShoppingBasketPage) {
      this.router.navigate(['/home']);
    }
    this.clearReferenceText();
  }


  private clearReferenceText() {
    const customerNr = this.userService.userDetail && this.userService.userDetail.custNr || '';
    const refKey = customerNr;
    if (refKey) {
      this.appStorage.cleanRefText(refKey);
    }
  }
}
