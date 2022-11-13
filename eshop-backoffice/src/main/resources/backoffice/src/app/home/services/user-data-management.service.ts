import { Injectable, Injector } from '@angular/core';
import { Subject } from 'rxjs';

import { UserSearchDetail } from '../pages/user-management/models/user-search-detail.model';
import { UserService } from 'src/app/core/services/user.service';
import { UserRequestModel } from '../models/user-request.model';

@Injectable()
export class UserDataManagementService {
    component;

    userQuery: UserRequestModel;
    userCurrentFetch;
    hasLoadedUsers = false;

    constructor(private userService: UserService, private injector: Injector) {
        this.userQuery = new UserRequestModel();
    }

    createInstance(component) {
        const userListService = this.injector.get(UserDataManagementService);
        userListService.component = component;
        return userListService;
    }

    setUserQuery(query) {
        this.userQuery = query;
    }

    mergeUserQuery(query) {
        this.userQuery = this.userQuery || new UserRequestModel();
        Object.assign(this.userQuery, query);
    }

    fetchUsers() {
        const userSource = new Subject<any>();
        let users = [];
        if (this.userCurrentFetch) {
            this.userCurrentFetch.unsubscribe();
        }
        this.userCurrentFetch = this.userService
            .searchUser(this.userQuery)
            .subscribe(
                (res) => {
                    const resData: any = res;
                    users = resData.content.map((user) => new UserSearchDetail(user));
                    userSource.next({
                        totalUsers: resData.totalElements,
                        totalPages: resData.totalPages,
                        itemsPerPage: resData.size,
                        users
                    });
                },
                (e) => userSource.error(e),
                () => userSource.complete()
            );
        return userSource.asObservable();
    }
}
