import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { ChangePassword } from '../models/change-password.model';
import { map } from 'rxjs/operators';
import { UserProfile, UpdateProfileModel } from '../models/user-profile/user-profile.model';

@Injectable({
    providedIn: 'root'
})
export class ProfileService {
    private readonly UPDATE_PASSWORD_FOR_USER = 'user/password/update';
    private readonly UPDATE_PASSWORD_FOR_ADMIN = 'users/password/update';
    private readonly UPDATE_PASSWORD_BY_FINAL_USER = 'final-customer-user/password/update';
    private readonly UPDATE_PROFILE = 'user/profile/update';
    private readonly USER_PROFILE = 'user/profile/';

    private readonly BASE_URL = environment.baseUrl;

    constructor(private http: HttpClient) { }

    updatePasswordForUser(changePasswordData: ChangePassword) {
        const url = `${this.BASE_URL}${this.UPDATE_PASSWORD_FOR_USER}`;
        return this.http.post(url, changePasswordData);
    }

    updatePasswordForAdmin(changePasswordData: ChangePassword) {
        const url = `${this.BASE_URL}${this.UPDATE_PASSWORD_FOR_ADMIN}`;
        return this.http.post(url, changePasswordData);
    }

    updatePasswordForFinalUser(changePasswordData: ChangePassword) {
        const url = `${this.BASE_URL}${this.UPDATE_PASSWORD_BY_FINAL_USER}`;
        return this.http.post(url, changePasswordData);
    }

    getUserProfile() {
        const url = `${this.BASE_URL}${this.USER_PROFILE}`;
        return this.http.get(url).pipe(map((profile) => new UserProfile(profile)));
    }

    getUserProfileById(userId: number) {
        const url = `${this.BASE_URL}users/${userId}/profile/`;
        return this.http.get(url);
    }

    updateProfile(body: UpdateProfileModel) {
        const url = `${this.BASE_URL}${this.UPDATE_PROFILE}`;
        return this.http.post(url, body);
    }

    updateUserProfile(profile) {
        const url = `${this.BASE_URL}users/profile/update`;
        return this.http.post(url, profile);
    }
}
