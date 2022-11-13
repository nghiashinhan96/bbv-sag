import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import { BranchModel } from '../model/branch.model';
import { BranchRequest } from '../model/branch-request.model';
import { ApiUtil } from 'src/app/core/utils/api.util';
import {
    GET_BRANCHES_URL,
    GET_BRANCH_URL,
    CREATE_BRANCH_URL,
    DELETE_BRANCH_URL,
    UPDATE_BRANCH_URL,
    GET_COUNTRY_LIST_FOR_BRANCH_URL
} from '../branches.constant';

@Injectable()
export class BranchService {

    constructor(private http: HttpClient) { }

    searchBranches(body: BranchRequest, page?): Observable<any> {
        let url = page ? `${GET_BRANCHES_URL}?page=${page.page}&size=${page.size}` : GET_BRANCHES_URL;
        url = ApiUtil.getUrl(url);
        return this.http.post(url, body);
    }

    getBranch(branchNumber: string): Observable<any> {
        let url = `${GET_BRANCH_URL}${branchNumber}`;
        url = ApiUtil.getUrl(url);
        return this.http.get(url);
    }

    createBranch(data: BranchModel): Observable<any> {
        const url = ApiUtil.getUrl(CREATE_BRANCH_URL);
        return this.http.post(url, data);
    }

    deleteBranch(branchNr: string) {
        const url = ApiUtil.getUrl(`${DELETE_BRANCH_URL}${branchNr}`);
        return this.http.delete(url);
    }

    editBranch(data: BranchModel): Observable<any> {
        const url = ApiUtil.getUrl(UPDATE_BRANCH_URL);
        return this.http.put(url, data);
    }

    getCountryListForBranch(): Observable<any> {
        const url = ApiUtil.getUrl(`${GET_COUNTRY_LIST_FOR_BRANCH_URL}`);
        return this.http.get(url);
    }
}
