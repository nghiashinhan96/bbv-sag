# ESHOP-PARENT
This module contains all bash scripts about build, deployment, refresh caching data and execute SONAR reporting.

## CI/CD
We are using SAG Bamboo as default CI/CD of SAG Connect project.

Link:

Build and Deployment: [Bamboo-Build]

Refresh caching data and do any action: [Bamboo-Trigger]

Git Project: [Bitbucket]

## Build and Deployment

### Setup

We will setup build and deployment jobs following the template as below:

```
bamboo_v2
  |
  |--> profiles
       |
       |--> <country> or <profile-name>
            |
            |--> <environment-name>
                |
                |--> build.sh   // script will be executed to build source code to runtime artifacts and un-deploy old artifacts at runtime server.
                |--> deploy.sh  // script will be executed to copy lastest articfacts to runtime servers and trigger deploy them.
                |--> trigger.sh // script will be executed at [Bamboo-Trigger] to trigger any actions for this environment.
```


| No | Profile | Country | Note |
| -- | ------- | ------- | ---- |
| 01 | at | Austria | ---- |
| 02 | autonet | Hungary | ---- |
| 03 | ch | Switzerland | ---- |
| 04 | cz | Czech | ---- |
| 05 | ------- | ------- | ---- |
| 06 | ------- | ------- | ---- |

### Definition

We have some glossary for environments

| No | Profile | Description | Responsibilities | Note |
| -- | ------- | ------- | ---- | ------ |
| 01 | dev | This is the profile of ***Development*** host at ***Hetzner Cloud*** | Verifying business logic, do regression testing,... | ------ |
| 02 | pre | This is the profile of ***Pre-Production*** | ---- | ------ |
| 03 | umbpre | This is the profile of ***Pre-Production*** host at ***UMB host provider*** | ---- | ------ |
| 04 | prod | This is the profile of ***Production*** | ---- | ------ | ------ | ------ |
| 05 | umbprod | This is the profile of ***Production*** host at ***UMB host provider*** | ---- | ------ |
| 06 | feature | This is the profile of ***Feature*** host at ***Hetzner Cloud*** | Using for implementation and integrate with other developers together | ------ |
| 07 | loadtest | This is the profile of ***Load Test*** | Verifying performance of release versions | ------ |
| 08 | ------- | ------- | ---- | ------ |
| 09 | ------- | ------- | ---- | ------ |



### Environment

### Profiles

The list of available profiles:

#### Austria

Env: [at] - Austria

Deployment Job: [01_AT-AX]

| No | Profile | Description | Note |
| -- | ------- | ----------- | ---- |
| 01 | dev | [`Definition`](#definition) | [Wiki-Env] |
| 02 | feature | [`Definition`](#definition) | [Wiki-Env] |
| 03 | umbpre | [`Definition`](#definition) | [Wiki-Env] |
| 04 | umbprod | [`Definition`](#definition) | [Wiki-Env] |

#### Switzerland

Env: [ch] - Switzerland

Deployment Job: [02_CH-AX]

| No | Profile | Description | Note |
| -- | ------- | ----------- | ---- |
| 01 | dev | [`Definition`](#definition) | [Wiki-Env] |
| 02 | feature | [`Definition`](#definition) | [Wiki-Env] |
| 03 | loadtest | [`Definition`](#definition) | [Wiki-Env] |
| 04 | umbpre | [`Definition`](#definition) | [Wiki-Env] |
| 05 | umbprod | [`Definition`](#definition) | [Wiki-Env] |

#### Autonet Hungary

Env: [autonet] - Autonet Hungary

Deployment Job: [03_AUTONET]

| No | Profile | Description | Note |
| -- | ------- | ----------- | ---- |
| 01 | dev | [`Definition`](#definition) | [Wiki-Env] |
| 02 | umbpre| [`Definition`](#definition) | [Wiki-Env] |
| 03 | umbprod | [`Definition`](#definition) | [Wiki-Env] |

#### Czech

Env: [cz] - Czech

Deployment Job: [04_CZ-ST]

| No | Profile | Description | Note |
| -- | ------- | ----------- | ---- |
| 01 | dev | [`Definition`](#definition) | [Wiki-Env] |
| 02 | pre | [`Definition`](#definition) | [Wiki-Env] |
| 03 | prod | [`Definition`](#definition) | [Wiki-Env] |

#### Serbia

Env: [sb] - Serbia

Deployment Job: N/A

| No | Profile | Description | Note |
| -- | ------- | ----------- | ---- |
| 01 | ------- | ------- | ---- |
| 02 | ------- | ------- | ---- |
| 03 | ------- | ------- | ---- |
| 04 | ------- | ------- | ---- |

[//]: #

[Bamboo-Build]: https://bamboo.sag-ag.ch/browse/CONNECT-RLAX
[Bamboo-Trigger]: https://bamboo.sag-ag.ch/browse/CONNECT-TRGAX
[01_AT-AX]: https://bamboo.sag-ag.ch/deploy/viewDeploymentProjectEnvironments.action?id=20480010
[02_CH-AX]: https://bamboo.sag-ag.ch/deploy/viewDeploymentProjectEnvironments.action?id=45940738
[03_AUTONET]: https://bamboo.sag-ag.ch/deploy/viewDeploymentProjectEnvironments.action?id=45940739
[04_CZ-ST]: https://bamboo.sag-ag.ch/deploy/viewDeploymentProjectEnvironments.action?id=53477377
[08_ANGULAR_UPGRADE]: https://bamboo.sag-ag.ch/deploy/viewDeploymentProjectEnvironments.action?id=49152001
[90_BATCH_JOB]: https://bamboo.sag-ag.ch/deploy/viewDeploymentProjectEnvironments.action?id=51478534
[99_SONAR]: https://bamboo.sag-ag.ch/deploy/viewDeploymentProjectEnvironments.action?id=45940741
[Bitbucket]: https://bitbucket.sag-ag.ch/projects/SAG-CONN/repos/sag-connect-ax
[Wiki]: https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Plus/_wiki/wikis/Connect-Plus.wiki?
[Wiki-Env]: https://tfs.bbv.ch/tfs/SAGCollection/Connect%20Plus/_wiki/wikis/Connect-Plus.wiki?wikiVersion=GBwikiMaster&pagePath=%2FConnect%20Plus%20Home%20Page%2FDeployment%20Guide%2FSAG%20eShop%20Environment%20Version%202&pageId=44