export const environment = {
    env: 'dev',
    production: true,
    affiliate: 'eh-cz',
    shortAffiliate: 'ehcz',
	baseUrl: 'https://cz.bbv-demo.ch/rest-st/',
    tokenUrl: 'https://cz.bbv-demo.ch/auth-server/',
	// baseUrl: 'http://localhost:9001/',
	// tokenUrl: 'http://localhost:9002/',
    debugMode: true,
    adsServer: 'https://sag.advertserve.com/js/libcode3.js',
    stompEndpoint: '',
    sso_clientid: 'N/A', // No sso for CZ
    sso_appcontext: '',
    sso_whitelist: [],
    gtmotive: {
        apiKey: '4EB72EDB-0949-420E-8F16-5FA09862A4C9',
        vehicleEndpoint: 'https://uat-gtapi.mygtmotive.com/api/navigationboard/js?ApiKey=',
        partsEndpoint: 'https://uat-gtapi.mygtmotive.com/api/vehiclerepresentation/js?apiKey='
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
      normal: 'sag-dev',
      sales: 'sag-dev'
    },
    enableHiddenBranch: false
};
