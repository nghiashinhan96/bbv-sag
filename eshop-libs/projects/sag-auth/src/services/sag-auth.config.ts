import { Injectable } from '@angular/core';

@Injectable()
export class SagAuthConfigService {
    baseUrl: string;
    spinner: any;
    affiliate: string;
    affiliateService: any;
    userService: any;
    tokenKey: any;
    isFinalUser: boolean;
    timeout: number;
    redirectUrl: string;
    tokenUrl: string;
    constructor() { }
}
