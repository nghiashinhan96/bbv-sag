export const environment = {
    env: 'prod',
    production: true,
    affiliate: 'ax-cz',
    shortAffiliate: 'ax-cz',
    baseUrl: '/rest-cz-ax/',
    tokenUrl: '/auth-server-cz-ax/',
    debugMode: false,
    adsServer: 'https://sag.advertserve.com/js/libcode3.js',
    stompEndpoint: '',
    sso_clientid: '7ca3ab45-ecea-45e0-b318-f9fe751ddd03',
    sso_appcontext: '/sag-cz',
    sso_whitelist: ['https://connect-int.sag.services'],
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
            link: 'http://www.derendinger.at/de/extras/anmeldung-pramiensystem.html' },
          { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG',
            link: 'http://www.derendinger.at/de/produkte/kataloge-zum-download.html'}
        ],
        digital_catalog_links: {
          de: { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG', link: 'http://matik.ch/de/downloads.html' },
          fr: { label: 'MENU.EXTERNAL_LINKS.DIGITAL_CATALOG', link: 'http://matik.ch/fr/downloads.html' }
        },
    },
    unicatServer: 'https://kunden.stahlgruber.de',
    laximo_src: {
      normal: 'sag-prod',
      sales: 'sag-prod-sales'
    },
    enableHiddenBranch: true,
    defaultArticleMode: 'extended-mode',
    salesArticleMode: 'extended-mode',
    customerArticleMode: 'extended-mode'
};
