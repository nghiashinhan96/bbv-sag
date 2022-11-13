import { UserAddress } from 'src/app/core/models/user-address.model';
import { PaymentMethod } from 'src/app/core/models/payment-method.model';
import { DeliveryType } from 'src/app/core/models/delivery-type.model';
import { ShoppingConditionHeaderModel } from '../../../models/shopping-condition-header.model';
import { ShoppingBasketContextModel } from 'src/app/shopping-basket/models/shopping-basket-context.model';
import { Component, Input, OnInit, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';

import { SB_LOCATION_TYPES } from 'sag-article-detail';

import { OrderLocationModel } from 'src/app/core/models/order-location.model';
import { ShoppingBasketModel } from 'src/app/core/models/shopping-basket.model';
import { PaymentSetting } from 'src/app/core/models/payment-settings.model';
import { ADDRESS_TYPE, DELIVERY_TYPE } from 'src/app/core/enums/shopping-basket.enum';
import { OrderLocationConditionFormCtrlModel } from 'src/app/core/models/order-location-condition-form-ctrl.model';
import { OrderLocationBranchModel } from 'src/app/core/models/order-location-branch.model';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { CourierServiceModel } from 'src/app/core/models/courier-service.model';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
@Component({
  selector: 'connect-shopping-condition-sb',
  templateUrl: './shopping-condition-sb.component.html',
  styleUrls: ['./shopping-condition-sb.component.scss']
})
export class ShoppingConditionSBComponent implements OnInit, OnChanges {
  @Input() set context(val: ShoppingBasketContextModel) {
    this.getNewBasketContext(val);
  }
  @Input() currentBasket: ShoppingBasketModel;
  @Input() userDetail: UserDetail;
  @Input() set paymentSetting(val: PaymentSetting) {
    if (!!val) {
      this.settings = val;
      this.filterAddress(this.settings);
      this.initPrimaryBrancheForPickup();
      this.initOrderLocations();
    }
  }

  @Output() updateShoppingBasketContext = new EventEmitter<{
    body: ShoppingBasketContextModel;
    reload: boolean;
    done: () => void;
  }>();

  locations: OrderLocationModel[] = [];
  settings: PaymentSetting;
  deliveryAddress: any[] = [];
  billingAddress: any[] = [];
  branchesForPickup: any[] = [];
  COURIER = DELIVERY_TYPE.COURIER;
  PrimaryLocation = SB_LOCATION_TYPES.PRIMARY;
  basketContext: ShoppingBasketContextModel;
  conditionHeader: ShoppingConditionHeaderModel = new ShoppingConditionHeaderModel();

  shoppingConditionForm: FormGroup;
  locationFormArray: FormArray;

  constructor(
    private fb: FormBuilder,
    private appStorage: AppStorageService
  ) {
    this.locationFormArray = this.fb.array([]);
    this.initForm();
  }

  ngOnInit() {
  }

  ngOnChanges(change: SimpleChanges) {
    if (change.currentBasket && change.currentBasket.currentValue && !change.currentBasket.firstChange) {
      this.currentBasket = change.currentBasket.currentValue;
      this.initOrderLocations();
    }

    if (change.context && change.context.currentValue && !change.context.firstChange) {
      this.getNewBasketContext(change.context.currentValue);
    }
  }

  get locationFormCtrls() {
    return (this.shoppingConditionForm.get('locationFormArray') as FormArray).controls;
  }

  changePaymentMethod(method: PaymentMethod, location: OrderLocationModel) {
    const index = this.getFormIndex(location.branch.branchId);
    if (index > -1) {
      // set payment id
      (this.locationFormCtrls[index] as FormGroup).controls['paymentId'].setValue(method.id);
      (location.formCtrl as OrderLocationConditionFormCtrlModel).paymentId = method.id;

      if (this.basketContext.eshopBasketContextByLocation[index]) {
        this.basketContext.eshopBasketContextByLocation[index].paymentMethod = method;
      }
      this.updateContext(this.basketContext);
    }
  }

  changeDeliveryType(method: DeliveryType, location: OrderLocationModel) {
    const index = this.getFormIndex(location.branch.branchId);
    if (index > -1) {
      // set delivery type value
      (this.locationFormCtrls[index] as FormGroup).controls['deliveryId'].setValue(method.id);
      (location.formCtrl as OrderLocationConditionFormCtrlModel).deliveryId = method.id;

      if (method.descCode === DELIVERY_TYPE.PICKUP) {
        // Show pickup dropdown
        location.isPickupMode = true;

        // set default pickup branch
        (this.locationFormCtrls[index] as FormGroup).controls['branchId'].setValue(location.branch.branchId);
        (location.formCtrl as OrderLocationConditionFormCtrlModel).branchId = location.branch.branchId;

        this.basketContext.eshopBasketContextByLocation[index].pickupLocation = location.branch;
      } else {
        // hide pickup dropdown
        location.isPickupMode = false;

        // reset pickup branch
        (this.locationFormCtrls[index] as FormGroup).controls['branchId'].setValue(null);
        (location.formCtrl as OrderLocationConditionFormCtrlModel).branchId = null;

        this.basketContext.eshopBasketContextByLocation[index].pickupLocation = null;
      }

      this.basketContext.eshopBasketContextByLocation[index].deliveryType = method;

      if (method.descCode === DELIVERY_TYPE.COURIER) {
        const currentCourierServiceCode = (this.locationFormCtrls[index] as FormGroup).controls['courierServiceCode'].value;

        let courierIndex = location.courierServices.findIndex(item => item.courierServiceCode === currentCourierServiceCode);
        if(courierIndex === -1) {
          courierIndex = 0;
        }

        const newValue = location.courierServices[courierIndex] && location.courierServices[courierIndex].courierServiceCode || null;
        (this.locationFormCtrls[index] as FormGroup).controls['courierServiceCode'].setValue(newValue);
        this.basketContext.eshopBasketContextByLocation[index].courierService = location.courierServices[courierIndex];

        location.isCourierMode = true;
      } else {
        this.basketContext.eshopBasketContextByLocation[index].courierService = null;
        location.isCourierMode = false;
      }

      this.updateContext(this.basketContext);
    }
  }

  changeDeliveryAddress(item: UserAddress, location: OrderLocationModel) {
    const index = this.getFormIndex(location.branch.branchId);
    if (index > -1) {
      (this.locationFormCtrls[index] as FormGroup).controls['deliveryAddressId'].setValue(item.id);
      (location.formCtrl as OrderLocationConditionFormCtrlModel).deliveryAddressId = item.id;

      this.basketContext.eshopBasketContextByLocation[index].deliveryAddress = item;
      this.updateContext(this.basketContext);
    }
  }

  changeCourierServices(item: CourierServiceModel, location: OrderLocationModel) {
    const index = this.getFormIndex(location.branch.branchId);
    if (index > -1) {
      (this.locationFormCtrls[index] as FormGroup).controls['courierServiceCode'].setValue(item.courierServiceCode);
      (location.formCtrl as OrderLocationConditionFormCtrlModel).courierServiceCode = item.courierServiceCode;

      this.basketContext.eshopBasketContextByLocation[index].courierService = item;
      this.updateContext(this.basketContext);
    }
  }

  updateLocationRefText(event, location: OrderLocationModel) {
    const index = this.getFormIndex(location.branch.branchId);
    if (index > -1) {
      const value = event.target.value || '';
      (this.locationFormCtrls[index] as FormGroup).controls['referenceTextByLocation'].setValue(value);
      (location.formCtrl as OrderLocationConditionFormCtrlModel).referenceTextByLocation = value;

      this.basketContext.eshopBasketContextByLocation[index].referenceTextByLocation = value;
      this.updateContext(this.basketContext);
    }
  }

  changePickupBranch(item: OrderLocationBranchModel, location: OrderLocationModel) {
    const index = this.getFormIndex(location.branch.branchId);
    if (index > -1) {
      (this.locationFormCtrls[index] as FormGroup).controls['branchId'].setValue(item.branchId);
      (location.formCtrl as OrderLocationConditionFormCtrlModel).branchId = item.branchId;

      this.basketContext.eshopBasketContextByLocation[index].pickupLocation = item;
      this.updateContext(this.basketContext);
    }
  }

  changeBillingAddress(item: UserAddress) {
    this.shoppingConditionForm.patchValue({
      billingAddressId: item.id
    });

    this.basketContext.billingAddress = item;
    this.updateContext(this.basketContext);
  }

  private getNewBasketContext(val) {
    this.basketContext = val;
    this.conditionHeader = new ShoppingConditionHeaderModel(val);
    this.setBillingAddressId(val);
  }

  private updateContext(body: ShoppingBasketContextModel, reload = false, done?: () => void) {
    this.updateShoppingBasketContext.emit({
      body,
      reload,
      done
    });
  }

  private getFormIndex(branchId) {
    return this.locationFormCtrls.findIndex((form: FormGroup) => form.getRawValue() && form.getRawValue().defaultBranchId === branchId);
  }

  private initOrderLocations() {
    this.locations = [];
    this.locationFormArray = this.fb.array([]);

    if (this.currentBasket) {
      (this.settings && this.settings.orderLocations || []).forEach(location => {
        const hasLocation = this.currentBasket.items.some(item => {
          if (item.articleItem && item.articleItem.availabilities) {
            return item.articleItem.availabilities.some(avail => avail.location.items.some(lo => lo.locationId == location.branch.branchId))
          }
        });

        if (hasLocation) {
          const item = new OrderLocationModel(location);
          item.formCtrl = new OrderLocationConditionFormCtrlModel();
          item.pickupBranchs = this.initBranchPickup(item.branch);
          item.formCtrl.defaultBranchId = item.branch.branchId;

          this.setLocationDefaultContextValue(item);
          this.locations.push(item);

          (this.locationFormArray as FormArray).push(this.buildLocationForm());
        }
      });

      this.setBasketContext();
      this.updateContext(this.basketContext);
    }

    this.shoppingConditionForm.controls['locationFormArray'] = this.locationFormArray;
  }

  private setLocationDefaultContextValue(location: OrderLocationModel) {
    const contextAdded = (this.basketContext.eshopBasketContextByLocation || []).find(con => con.location && con.location.branchId === location.branch.branchId);
    if (contextAdded) {
      location = this.setDefaultValue(location, { ...contextAdded });
    } else {
      location = this.setDefaultValue(location, { ...this.basketContext });
    }

    return location;
  }

  private setBasketContext() {
    const eshopBasketContextByLocation = [];

    this.locations.forEach(location => {
      const contextAdded = this.basketContext.eshopBasketContextByLocation.find(con => con.location && con.location.branchId === location.branch.branchId);
      let item: ShoppingBasketContextModel = new ShoppingBasketContextModel();

      if (contextAdded) {
        item = { ...contextAdded };
      } else {
        // set init data for eshopBasketContext of this location
        item = { ...this.basketContext };
      }

      item.referenceTextByLocation = location.formCtrl.referenceTextByLocation;
      const paymentMethod = (location.paymentMethods || []).find(p => p.id === location.formCtrl.paymentId);
      if (paymentMethod) {
        item.paymentMethod = paymentMethod;
      }
      item.location = location.branch;
      item.eshopBasketContextByLocation = [];

      // remove pickup branch only used in Connect
      delete item.pickupBranch;

      if (item && !(eshopBasketContextByLocation || []).find(con => con.location && con.location.branchId === location.branch.branchId)) {
        eshopBasketContextByLocation.push(item);
      }
    });

    this.basketContext.eshopBasketContextByLocation = eshopBasketContextByLocation;
  }

  private filterAddress(settings: PaymentSetting) {
    this.deliveryAddress = settings.addresses
      .filter(x => x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DELIVERY]
        || x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]);

    if (this.deliveryAddress.length > 1) {
      this.deliveryAddress = this.deliveryAddress.filter(item => {
        return item.addressType !== ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT];
      });
    }

    this.billingAddress = settings.billingAddresses
      .filter(x => x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.INVOICE]
        || x.addressTypeCode === ADDRESS_TYPE[ADDRESS_TYPE.DEFAULT]);
  }

  private initPrimaryBrancheForPickup() {
    this.branchesForPickup = [];

    const location = (this.settings.orderLocations || []).find(item => item.branch.locationType === this.PrimaryLocation);
    if (location) {
      this.branchesForPickup.push(location.branch);
    }
  }

  private initForm() {
    this.shoppingConditionForm = this.fb.group({
      billingAddressId: '',
      locationFormArray: this.locationFormArray
    });
  }

  private buildLocationForm() {
    let obj = {};

    this.locations.forEach(location => {
      if (location.formCtrl) {
        Object.keys(location.formCtrl).forEach(ctrl => {
          obj[ctrl] = this.fb.control(location.formCtrl[ctrl])
        });
      }
    });

    return this.fb.group(obj);
  }

  private initBranchPickup(currentBranch: OrderLocationBranchModel) {
    const itemExist = this.branchesForPickup.find(it => it.branchId === currentBranch.branchId);

    if (!itemExist) {
      return this.branchesForPickup.concat([currentBranch]);
    }

    return this.branchesForPickup;
  }

  private setBillingAddressId(context: ShoppingBasketContextModel) {
    this.shoppingConditionForm.controls['billingAddressId'].setValue(context && context.billingAddress && context.billingAddress.id || '');
  }

  private convertDeliveryId(context) {
    const deliveryTypeId = context && context.deliveryType && context.deliveryType.id || ''
    if (!this.userDetail || !this.userDetail.isSalesOnBeHalf || !context || !this.settings) {
      return deliveryTypeId;
    }

    return deliveryTypeId;
  }

  private getPaymentMethod(item: OrderLocationModel, context: ShoppingBasketContextModel) {
    const paymentMethodDescCode = context && context.paymentMethod && context.paymentMethod.descCode || '';

    if (item.paymentMethods.find(pay => pay.descCode === paymentMethodDescCode)) {
      return context.paymentMethod.id;
    } else {
      return item.paymentMethods[0].id;
    }
  }

  private setDefaultValue(item: OrderLocationModel, context: ShoppingBasketContextModel) {
    const deliveryId = this.convertDeliveryId(context);
    const referenceTextByLocation = this.getReferenceTextFromSaveBasket(item, context);

    item.formCtrl = {
      ...item.formCtrl,
      branchId: this.getPickupLocation(item, context),
      deliveryAddressId: context && context.deliveryAddress && context.deliveryAddress.id || '',
      deliveryId: deliveryId,
      paymentId: this.getPaymentMethod(item, context),
      referenceTextByLocation: referenceTextByLocation
    };

    const currentDelivery = item.deliveryTypes.find(delivery => delivery.id === deliveryId);
    if (currentDelivery && currentDelivery.descCode === DELIVERY_TYPE.PICKUP) {
      item.isPickupMode = true;
    } else {
      item.isPickupMode = false;
    }

    if (currentDelivery && currentDelivery.descCode === this.COURIER) {
      item.formCtrl.courierServiceCode = context.courierService && context.courierService.courierServiceCode || '';
      item.isCourierMode = true;
    } else {
      item.isCourierMode = false;
    }

    return item;
  }

  private getPickupLocation(item: OrderLocationModel, context: ShoppingBasketContextModel) {
    if (context && context.pickupLocation) {
      return context.pickupLocation.branchId;
    }

    if (context && context.pickupBranch) {
      return context.pickupBranch.branchId;
    }

    if (item.branch && item.branch.locationType !== SB_LOCATION_TYPES.PRIMARY) {
      return item.branch.branchId;
    }

    return '';
  }

  private getReferenceTextFromSaveBasket(item: OrderLocationModel, context: ShoppingBasketContextModel) {
    if (!!context.referenceTextByLocation) {
      return context.referenceTextByLocation;
    }

    if (!!this.appStorage.refText) {
      return this.appStorage.refText[this.userDetail.custNr] || '';
    }

    return '';
  }
}
