SAG-STAKIS-ERP
===========

This is the module contains the implementation and integrate with Stakis ERP

EPIC: [#1572]

Document: [Sharepoint]

How to integration
-------

This is the SOAP webservice

#### Endpoint

##### Test Service:

| No | Service | URL | Note |
| -- | ------- | --- | ---- |
| 01 | CIS | [CIS](https://wsvc.stahlgruber.cz/testService/CustomerInformationService.svc) | ---- |
| 02 | TM Connect | [TM](https://wsvc.stahlgruber.cz/testService/TMConnect.svc) | ---- |
| 03 | --------| --- | ---- |

##### Production Service:

| No | Service | URL | Note |
| -- | ------- | --- | ---- |
| 01 | CIS | [CIS]() | ---- |
| 02 | TM Connect | [TM]() | ---- |
| 03 | --------| --- | ---- |


#### Configuration


##### WSDL Package configuration

| No | Config | Value | Note |
| -- | ------- | --- | ---- |
| 01 | external.webservice.stakis-erp.cis-svc | [Endpoint] | ---- |
| 02 | external.webservice.stakis-erp.cis-context-path | com.sagag.services.stakis.erp.wsdl.cis | ---- |
| 03 | external.webservice.stakis-erp.tm-connect-svc | [Endpoint] | ---- |
| 03 | external.webservice.stakis-erp.tm-connect-context-path | com.sagag.services.stakis.erp.wsdl.tmconnect | ---- |

##### Payment configuration

| No | Config | Value | Note |
| -- | ------- | --- | ---- |
| 01 | external.webservice.stakis-erp.config.payment.CASH | Platba v hotovosti | ---- |
| 02 | external.webservice.stakis-erp.config.payment.EUR_PAYMENT | EUR payment | ---- |
| 03 | external.webservice.stakis-erp.config.payment.BANK_TRANSFER | Převodem na účet | ---- |



[//]: #

[#1572]: https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Serbia%20and%20Czech/_workitems/edit/1572
[Sharepoint]: https://bbvsoftwareservices.sharepoint.com/sites/bbvVietnam/Shared%20Documents/Forms/AllItems.aspx?id=%2Fsites%2FbbvVietnam%2FShared%20Documents%2F03%20Operations%20%2D%20Projects%2FSAG%20%2D%20Connect%2FEstimation%2FST%2D%20Czech%20Republic&p=true&originalPath=aHR0cHM6Ly9iYnZzb2Z0d2FyZXNlcnZpY2VzLnNoYXJlcG9pbnQuY29tLzpmOi9zL2JidlZpZXRuYW0vRW5nWi1ydUFOc2hHZ1ZOSGFjdmk2MkFCT3JvWHdSVFBVbVVRQmMxWktjTy02QT9ydGltZT1ETWR5dnpJaTJFZw
[Wiki]: https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Plus/_wiki/wikis/Connect-Plus.wiki?
[Wiki-Env]: https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Plus/_wiki/wikis/Connect-Plus.wiki?wikiVersion=GBwikiMaster&pagePath=%2FConnect%20Plus%20Home%20Page%2FDeployment%20Guide%2FSAG%20eShop%20Environment%20Version%202&pageId=44
[Endpoint]: #endpoint