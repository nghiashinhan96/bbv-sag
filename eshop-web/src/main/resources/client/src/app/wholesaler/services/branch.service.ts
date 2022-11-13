import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';

import {
    GET_BRANCHES_URL,
    GET_BRANCH_URL,
    CREATE_BRANCH_URL,
    DELETE_BRANCH_URL,
    UPDATE_BRANCH_URL,
    GET_COUNTRY_LIST_FOR_BRANCH_URL
} from './constant';
import { BranchRequest } from '../models/branch-request.model';
import { environment } from 'src/environments/environment';
import { BranchModel } from '../models/branch-opening-time.model';

@Injectable()
export class BranchService {

    private readonly BASE_URL = environment.baseUrl;
    constructor(private http: HttpClient) { }

    searchBranches(body: BranchRequest, page?): Observable<any> {
        let url = page ? `${GET_BRANCHES_URL}?page=${page.page}&size=${page.size}` : GET_BRANCHES_URL;
        url = `${this.BASE_URL}${url}`;
        return this.http.post(url, body);
    }

    getBranch(branchNumber: string): Observable<any> {
        let url = `${GET_BRANCH_URL}${branchNumber}`;
        url = `${this.BASE_URL}${url}`;
        return this.http.get(url);
    }

    createBranch(data: BranchModel): Observable<any> {
        const url = `${this.BASE_URL}${CREATE_BRANCH_URL}`;
        return this.http.post(url, data);
    }

    deleteBranch(branchNr: string) {
        const url = `${this.BASE_URL}${DELETE_BRANCH_URL}${branchNr}`;
        return this.http.delete(url);
    }

    editBranch(data: BranchModel): Observable<any> {
        const url = `${this.BASE_URL}${UPDATE_BRANCH_URL}`;
        return this.http.put(url, data);
    }

    getCountryListForBranch(): Observable<any> {
        const url = `${this.BASE_URL}${GET_COUNTRY_LIST_FOR_BRANCH_URL}`;
        return this.http.get(url);
    }
}
