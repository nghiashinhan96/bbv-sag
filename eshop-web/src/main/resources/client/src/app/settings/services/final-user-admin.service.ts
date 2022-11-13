import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { FinalCustomerUserResponse } from '../models/final-customer/final-customer-user-response.model';
import { map } from 'rxjs/operators';
import { ProfileModel } from '../models/final-user-admin/user-profile.model';

@Injectable({
    providedIn: 'root'
})
export class FinalUserAdminService {

    private readonly BASE_URL = environment.baseUrl;

    constructor(
        private http: HttpClient
    ) { }

    getWssProfile(orgId) {
        const url = `${this.BASE_URL}final-customer-user/profile?finalCustomerId=${orgId}`;
        return this.http.get(url);
    }

    getFinalUserProfile() {
        const url = `${this.BASE_URL}final-customer-user/profile`;
        return this.http.get(url);
    }

    getFinalUser(userId: number) {
        const url = `${this.BASE_URL}final-customer-user/${userId}/profile`;
        return this.http.get(url);
    }

    public getFinalCustomerUserList(body: any) {
        const url = `${this.BASE_URL}final-customer-user/search`;
        return this.http.post(url, body).
            pipe(
                map((res: any) => {
                    const data = new FinalCustomerUserResponse({ ...res, data: res.content, total: res.totalElements, pageNr: res.number });
                    return data;
                })
            );
    }

    createFinalUser(finalCustomerOrgId: number, userProflie: ProfileModel) {
        const url = `${this.BASE_URL}final-customer-user/${finalCustomerOrgId}/create`;
        return this.http.post(url, userProflie);
    }

    updateFinalUser(userId: number, userProflie: ProfileModel) {
        const url = `${this.BASE_URL}final-customer-user/${userId}/update`;
        return this.http.post(url, userProflie);
    }

    deleteFinalUser(userId: number) {
        const url = `${this.BASE_URL}final-customer-user/${userId}/profile/delete`;
        return this.http.post(url, null);
    }

    changePassword(userId: number, body) {
        const url = `${this.BASE_URL}final-customer-user/${userId}/password/update`;
        return this.http.post(url, body);
    }

    createFinalCustomerUser(req: any) {
        const url = `${this.BASE_URL}final-customer-user/create`;
        return this.http.post(url, req);
    }

    searchFinalCustomersUser(finalCustomerOrgId: number, req: any) {
        const url = `${this.BASE_URL}final-customer-user/${finalCustomerOrgId}/search`;
        return this.http.post(url, req)
            .pipe(map(response => new FinalCustomerUserResponse(response)));
    }
}
