import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/internal/operators/map';
import { AdminManagementUser } from '../models/normal-admin/user-admin-magement.model';
import { UpdateProfileModel } from '../models/user-profile/user-profile.model';

@Injectable({
    providedIn: 'root'
})
export class NormalAdminService {
    private readonly GET_ALL_USERS_URL = 'users/all-user';
    private readonly CREATE_USER_URL = 'customer/users/create';
    private readonly DELETE_USER_URL = 'users/profile/delete-user';

    private readonly BASE_URL = environment.baseUrl;

    constructor(private http: HttpClient) { }

    getAllUsers() {
        const url = `${this.BASE_URL}${this.GET_ALL_USERS_URL}`;
        return this.http.get(url).pipe(map((response: any[]) => response.map(item => new AdminManagementUser(item))));
    }

    createUser(user: UpdateProfileModel) {
        const url = `${this.BASE_URL}${this.CREATE_USER_URL}`;
        return this.http.post(url, user);
    }

    deleteUser(userId: number) {
        const url = `${this.BASE_URL}${this.DELETE_USER_URL}`;
        return this.http.post(url, userId);
    }
}
