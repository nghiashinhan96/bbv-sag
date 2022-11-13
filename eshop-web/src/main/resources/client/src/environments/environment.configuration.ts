import { environment } from './environment';

export interface EnvConfig {
    readonly env: string;
    readonly production: boolean;
    readonly affiliate: string;
    readonly baseUrl: string;
    readonly tokenUrl: string;
}

export let ENV_CONFIG: EnvConfig = {
    env: environment.env,
    production: environment.production,
    affiliate: environment.affiliate,
    baseUrl: environment.baseUrl,
    tokenUrl: environment.tokenUrl
};
