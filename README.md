SAG-CONNECT-AX
================

The goal is to build a web-shop system (Connect) for the B2B business of SAG that can help the clients order the article parts in a fast and convenient way.

The system is required to be available for other third-party system integration such as GTMotive, Olyslager, HaynesPro, DVSE Catalogues, AutoNet, ...

## Technology

### Frontend

- Angular 4+ (planning to upgrade to latest Angular 9+ in 2020)
- HTML/SCSS
- Google Analytics

### Backend
- Java 8, JDK 1.8 (planning to upgrade to later Java version (TBD.) in 2020)
- Spring Boot (v2.0.1.RELEASE)
- Spring security
	- Spring MVC
	- Spring Data JPA
	- Spring tx
	- Spring Web Services
	- Spring Data Elasticsearch
	-	Spring Batch
- Hibernate
	- Connection Pool: Hikari CP
-	ElasticSearch
- Hazelcast
- Lombok
- Logback, SLF4J
- ~~LogFaces~~
- ELK: Elasticsearch, Logstash, Kibana for logging central
- Swagger Doc

## Modules

EShop is built on top of Spring framework and opensource technologies. It is designed in a way that enables the flexibility, scalability and maintenainbility. Thus every independent functionality is analyzed carefully to make it as an independent re-usable unit called 'component'.

| No | Component | Component Category | Description | Deployment Note |
| -- | --------- | :----: | ----------- | --------------- |
| 1 | eshop-web | UI | The EShop core UI for SAG EShop for Business user | This component will be installed on the separate webserver which makes the secure request to the backend services |
| 2 | eshop-backoffice | UI | The EShop core UI for SAG EShop for Administrator user | This component will be installed at SAG side to make settings for the whole system. |
| 3 | eshop-autonet | UI |  |  |
| 4 | eshop-xxxx | UI |  |  |
| 5 | eshop-libs | UI Library |  |  |
| 6 | eshop-parent |  |  |  |
| 7 | eshop-repo | Backend service/ independent runnable module | The EShop Web Data Repository that handles the connection and results(data) from the EShop database |  |
| 8 | eshop-service | Backend service/ independent runnable module | The EShop Localized services which contains the services to EShop database (e.g Coupons,...) | This component will be part of the backend services and be installed on the RESTful core server which will be used as the central SAG secured backend services (*) |
| 9 | bamboo-specs | Backend service/ independent runnable module |  |  |
| 10 | sag-parent |  |  |  |
| 11 | sag-common | Backend service/ independent runnable module | The SAG common functions, enumeration and constants for whole system |  |
| 12 | sag-domain | Backend service/ independent runnable module | The SAG common data response/request object models for whole system |  |
| 13 | sag-elasticsearch | Backend service/ independent runnable module | The SAG ElasticSearch API Services component that handles the calls and the response for searching |  |
| 14 | sag-hazelcast | Backend service/ independent runnable module |  |  |
| 15 | sag-logging | Backend service/ independent runnable module |  |  |
| 16 | sag-gtmotive | Backend service/ independent runnable module | The SAG GTMotive API Services component that handles the GTMotive calls and the response |  |
| 17 | sag-incentive | Backend service/ independent runnable module | The SAG Incentive API Services component that handles the Happy Points |  |
| 18 | ~~sag-olyslager~~ | ~~Backend service/ independent runnable module~~ | ~~The SAG Olyslager API Services component that handles the Olyslager vehicle typeID(s) using KBA identifier(s), Oil Categories, SAGArticles...~~ |  |
| 19 | sag-mdm | Backend service/ independent runnable module | The SAG user integration with MDM system for DVSE application integration |  |
| 20 | sag-oates | Backend service/ independent runnable module | Replace for Olyslager |  |
| 21 | sag-thule | Backend service/ independent runnable module |  |  |
| 22 | sag-haynespro | Backend service/ independent runnable module | The SAG Haynes Pro API Services component that handles the Single Sign On service |  |
| 23 | sag-ivds | Backend service/ independent runnable module | The SAG Intelligent Vehicle Data API Services component that handles the vehicle-related searching including vehicle search by VIN, by Plate, by Name, Part Reference searching, search Part by Name, search and calculate the cost of a repair with labour times and parts,... |  |
| 24 | sag-service | Backend service/ independent runnable module |  |  |
| 25 | sag-article-api | Backend service/ independent runnable module | The SAG external interface to implement update articles information from ERP or AX of other SAG services |  |
| 26 | sag-ax | Backend service/ independent runnable module | SAG AX API Services components that handles the calls and response to AX system(Microsoft Dynamics AX system) for Article/Order/Invoice/Customer... |  |
| 27 | sag-autonet-erp | Backend service/ independent runnable module |  |  |
| 28 | sag-stakis-erp | Backend service/ independent runnable module |  |  |
| 29 | ~~sag-partcontent~~ | ~~~~ | ~~The SAG Part Content API Services component that handles the calls and response for part details (e.g. part pictures and part documents...)~~ |  |
| 30 | ~~sag-incentive2~~ | ~~Backend service/ independent runnable module~~ |  |  |
| 31 | ~~sag-vindata~~ | ~~Backend service/ independent runnable module~~ |  |  |
| 32 | ~~eshop-context~~ | ~~Backend service/ independent runnable module~~ | ~~The EShop Application context at run-time~~ |  |
| 33 | ~~sag-plate~~ | ~~Backend service/ independent runnable module~~ |  |  |
| 34 | sag-rest | Backend service/ independent runnable module | The SAG Core Common Rest API Services component that exposes the data to external/client application (mobile, web) |  |
| 35 | sag-oauth2 | Backend service/ independent runnable module |  |  |
| 36 | sag-admin | Backend service/ independent runnable module |  |  |
| 37 | sag-dvse | Backend service/ independent runnable module | The SAG SOAP Web service component that provide for DVSE external application |  |
| 38 | sag-sso | Backend service/ independent runnable module | The SAG Single Sign On API Services component that handles the access connection from e-Connect to AX or ERP systems. |  |
| 39 | ~~sag-gtestimate~~| ~~Backend service/ independent runnable module~~ | ~~The SAG GTEstimate API Services component that handles the calls and response for single sign on and the completed estimate of multiple parts.~~ |  |
|  | |  |  |  |
|  | |  |  |  |

## Environment

Refer to this page <a href="https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Plus/_wiki/wikis/Connect-Plus.wiki?wikiVersion=GBwikiMaster&pagePath=%2FConnect%20Plus%20Home%20Page%2FDeployment%20Guide%2FSAG%20eShop%20Environment%20Version%202&pageId=44"> Environment Page</a>

## Tools

##### Version Source Control
Git: <a href="https://bitbucket.sag-ag.ch/projects/SAG-CONN">SAG Bitbucket</a>

CI/CD: <a href="https://bamboo.sag-ag.ch/browse/CONNECT-RLAX">SAG Bamboo</a>

##### Project Management Tool
Currently: <a href="https://tfs.bbv.ch/tfs/SAGCollection/">Azure DevOps</a>

Old: <a href="https://app.assembla.com/spaces/sag-eshop/tickets/realtime_cardwall">Assembla</a>

##### IDE
Java: [Eclipse], [IntelliJ],...

DB: [dbeaver],...

Front-end: [Visual Code]


## License

[SAG]

[//]: #

[SAG]: https://www.sag-ag.ch/
[Eclipse]: https://www.eclipse.org/downloads/
[IntelliJ]: https://www.jetbrains.com/idea/download/#section=windows
[dbeaver]: https://dbeaver.io/download/
[Visual Code]: https://code.visualstudio.com/Download