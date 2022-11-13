import { Injectable } from '@angular/core';

import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from '@angular/router';
import { of } from 'rxjs';

import { ExternalVendorService } from './external-vendor.service';
import { ExternalVendorDetailRequest } from '../model/external-vendor-item.model';

@Injectable()
export class ExternalVendorDetailResolver implements Resolve<any> {

    constructor(private externalVendorService: ExternalVendorService) { }

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.paramMap.get('id');
        return id ? this.externalVendorService.find(id) : of(new ExternalVendorDetailRequest());
    }
}
