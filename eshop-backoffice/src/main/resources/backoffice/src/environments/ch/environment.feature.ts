// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.
export const environment = {
    env: 'feature',
    production: false,
    countryCode: 'ch',
    affiliate: 'ch',
    // baseUrl: 'http://localhost:9004/',
    // tokenUrl: 'http://localhost:9002/',
    baseUrl: 'https://dev02.bbv-demo.ch/admin-ax/',
    tokenUrl: 'https://dev02.bbv-demo.ch/auth-server/',
};
