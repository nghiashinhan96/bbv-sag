export const environment = {
    env: 'prod',
    production: true,
    affiliate: 'matik-at',
    shortAffiliate: 'mtat',
    baseUrl: '/rest-at-ax/',
    tokenUrl: '/auth-server-ax/',
    debugMode: false,
    adsServer: 'https://sag.advertserve.com/js/libcode3.js',
    stompEndpoint: '',
    sso_clientid: '1b78a501-c340-462c-a5e8-672847b6a970',
    sso_appcontext: '/mat-ax',
    sso_whitelist: ['https://connect.sag.services'],
    gtmotive: {
        apiKey: 'CBF6CD88-42A7-4AD4-9E76-5181E7CF58C9',
        vehicleEndpoint: 'https://gtapi.mygtmotive.com/api/navigationboard/js?ApiKey=',
        partsEndpoint: 'https://gtapi.mygtmotive.com/api/vehiclerepresentation/js?apiKey='
    },
    incentive: {
        enable: true,
        external_links: [
          { label: 'MENU.EXTERNAL_LINKS.HAPPY_POINTS_SHOP',
            link: 'https://shop.connexservice.com/gui/derendinger/login.php' },
          { label: 'MENU.EXTERNAL_LINKS.HAPPY_POINTS_REGISTRATION',
            link: 'https://www.matik.at/de/praemienwelt.html' },
          { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG',
            link: 'http://www.derendinger.at/de/produkte/kataloge-zum-download.html'}
        ],
        digital_catalog_links: {
          de: { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG', link: 'http://matik.ch/de/downloads.html' },
          fr: { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG', link: 'http://matik.ch/fr/downloads.html' }
        },
    },
    unicatServer: '',
    enableHiddenBranch: true
};
