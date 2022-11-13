import { Injectable } from '@angular/core';

import * as CryptoJS from 'crypto-js';

@Injectable()
export class EncryptDecryptService {
    constructor() { }

    encrypt(value?: string) {
        return CryptoJS.MD5(value);
    }
}
