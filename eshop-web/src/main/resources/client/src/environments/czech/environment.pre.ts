export const environment = {
    env: 'pre',
    production: true,
    affiliate: 'st-cz',
    shortAffiliate: 'st-cz',
    baseUrl: '/rest-cz-st/',
    tokenUrl: '/auth-server-st/',
    debugMode: false,
    adsServer: 'https://sag.advertserve.com/js/libcode3.js',
    stompEndpoint: '',
    sso_clientid: 'N/A', // No sso for CZ
    sso_appcontext: '',
    sso_whitelist: [],
    gtmotive: {
        apiKey: '0BB4BD43-879E-4B7F-A3C6-434125E606E5',
        vehicleEndpoint: 'https://gtapi.mygtmotive.com/api/navigationboard/js?ApiKey=',
        partsEndpoint: 'https://gtapi.mygtmotive.com/api/vehiclerepresentation/js?apiKey='
    },
    incentive: {
        enable: true,
        external_links: [
          { label: 'MENU.EXTERNAL_LINKS.HAPPY_POINTS_SHOP',
            link: 'https://shop.connexservice.com/gui/derendinger/login.php' },
          { label: 'MENU.EXTERNAL_LINKS.HAPPY_POINTS_REGISTRATION',
            link: 'http://www.derendinger.at/de/extras/anmeldung-pramiensystem.html' },
          { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG',
            link: 'http://www.derendinger.at/de/produkte/kataloge-zum-download.html'}
        ],
        digital_catalog_links: {
          de: { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG', link: 'http://matik.ch/de/downloads.html' },
          fr: { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG', link: 'http://matik.ch/fr/downloads.html' }
        },
    },
    unicatServer: 'https://kunden.int.stahlgruber.de',
    laximo_src: {
      normal: 'sag-test',
      sales: 'sag-test'
    },
    enableHiddenBranch: false,
    defaultArticleMode: 'extended-mode',
    salesArticleMode: 'extended-mode',
    customerArticleMode: 'extended-mode'
};
