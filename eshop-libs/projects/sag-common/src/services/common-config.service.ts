import { Injectable } from '@angular/core';

@Injectable()
export class CommonConfigService {
    baseUrl: string;
    adsServer: string;
    affiliate: string;
    appToken: string;
    projectId: string;
    constructor() { }
}
