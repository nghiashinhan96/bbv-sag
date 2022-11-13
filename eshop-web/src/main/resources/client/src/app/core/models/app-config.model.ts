import { DEFAULT_LANG_CODE } from '../conts/app-lang-code.constant';

export class AppConfigurationModel {
    appLangCode: string = DEFAULT_LANG_CODE;
    appVersion = '';
    appToken = null;
    appSetting = {};
}
