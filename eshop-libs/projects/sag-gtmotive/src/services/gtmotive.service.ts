import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { cloneDeep, get } from 'lodash';
import { catchError, finalize, map, tap } from 'rxjs/operators';
import { SagGtmotiveConfigService } from './gtmotive-config.service';
import { ArticleModel } from 'sag-article-detail';
import { BehaviorSubject, of } from 'rxjs';

@Injectable()
export class GtmotiveService {
    vinCode: string;
    estimateId: string;
    isVinMode: boolean;
    vehicle: any;
    gtmotiveData: any;
    gteCodes: any = {};
    isMultiGTcodes: boolean;
    gtLinks: any[];
    gtmArticles = [];
    allArticles = [];
    allSelectedCategoryIds = [];
    queuedCategoryIds = [];

    private gtRequest$ = new BehaviorSubject<any>(null);
    gtRequestObservable = this.gtRequest$.asObservable();
    
    constructor(
        private http: HttpClient,
        private config: SagGtmotiveConfigService
    ) { }

    searchVehicleByVin(body) {
        return this.http.post(`${this.config.baseUrl}gtmotive/vehicle/search-by-vin`, body);
    }

    colectListParts(body) {
        return this.http.post(`${this.config.baseUrl}gtmotive/part-list/search`, body);
    }

    searchReferencesByShortNumber(body) {
        return this.http.post(`${this.config.baseUrl}gtmotive/multi-part/search`, body);
    }

    searchReferencesByPartCode(body) {
        return this.http.post(`${this.config.baseUrl}gtmotive/references/search`, body);
    }

    searchVehicleByGtInfo(body) {
        return this.http.post(`${this.config.baseUrl}gtmotive/vehicle/search-by-gtinfo`, body);
    }

    colectArticlesFromGtmotive(body) {
        return this.http.post(`${this.config.baseUrl}gtmotive/v2/articles`, body)
            .pipe(
                map(res => this.mapGtData(res))
            );
    }

    logVinError(body) {
        return this.http.post(`${this.config.baseUrl}gtmotive/error/log`, body).pipe(
            catchError(() => of(null))
        );
    }

    emitSearch(request) {
        this.gtRequest$.next(request);
    }

    searchInParts(group: any, searchTerm: any, results: any[]) {
        for (const part of group.parts) {
            // search in parts
            for (const key in part) {
                if (`${part[key]}`.toLowerCase().indexOf(searchTerm) !== -1) {
                    if (this.isExistedGroupInList(group, results)) {
                        results = this.updateExistedPartInList(part, group, results);
                    } else {
                        const newGroup = Object.assign({}, group);
                        newGroup.parts = [part];
                        newGroup.open = true;
                        results.push(newGroup);
                    }
                }
            }
        }
        return results;
    }

    collectRanks(originalEquipmentRanks, selectedDate): Array<any> {
        if (!selectedDate) {
            return originalEquipmentRanks;
        }
        const equipmentRanks = cloneDeep(originalEquipmentRanks);
        const dateItem = equipmentRanks.find(rank => {
            return this.isDateEquipment(rank.family, rank.subFamily);
        });

        if (dateItem) {
            dateItem.value = selectedDate;
        } else {
            equipmentRanks.push(this.createDateRankItem(selectedDate));
        }
        return equipmentRanks;
    }

    isDateEquipment(family: string, subFamily: string): boolean {
        return 'RNG' === family && 'FEC' === subFamily;
    }

    openGtmotive() {
        const bodyRequest = {
            gtDrv: this.gteCodes.gtDrv,
            gtEng: this.gteCodes.gtEng,
            gtMod: this.gteCodes.gtMod,
            umc: this.gteCodes.umc
        };
        const spinner = this.config.spinner.start('sag-in-context-articles-result-list .result');
        return this.searchVehicleByGtInfo(bodyRequest)
            .pipe(
                finalize(() => this.config.spinner.stop(spinner)),
                tap((res: any) => {
                    if (res.data) {
                        this.isVinMode = false;
                        this.vehicle = res.data.vehicle;
                        this.gtmotiveData = res.data.gtmotiveResponse;
                        this.estimateId = this.gtmotiveData.estimateId;

                        this.handleVehicleResponse(res.data.vehicle, this.gteCodes.umc);
                    }
                })
            );
    }

    openGtmotiveForGTcode(gteCodes: any) {
        const bodyRequest = {
            gtDrv: gteCodes.gtDrv,
            gtEng: gteCodes.gtEng,
            gtMod: gteCodes.gtMod,
            umc: gteCodes.umc
        };
        const spinner = this.config.spinner.start('sag-in-context-articles-result-list .result');
        return this.searchVehicleByGtInfo(bodyRequest)
            .pipe(
                finalize(() => this.config.spinner.stop(spinner)),
                tap((res: any) => {
                    if (res.data) {
                        this.isVinMode = false;
                        this.vehicle = res.data.vehicle;
                        this.gtmotiveData = res.data.gtmotiveResponse;
                        this.estimateId = this.gtmotiveData.estimateId;
                        this.handleVehicleResponse(res.data.vehicle, gteCodes.umc);
                    }
                })
            );
    }

    handleVehicleResponse(vehicle: any, targetUmc?: string) {
        if (vehicle.gt_links && vehicle.gt_links.length) {
            this.gteCodes = {
                umc: vehicle.gt_links[0].gt_umc,
                makeId: vehicle.id_make,
                gtDrv: vehicle.gt_links[0].gt_drv,
                gtEng: vehicle.gt_links[0].gt_eng,
                gtMod: vehicle.gt_links[0].gt_mod
            };

            this.isMultiGTcodes = vehicle.gt_links && vehicle.gt_links.length > 1;

            if (this.isMultiGTcodes) {
                this.gtLinks = vehicle.gt_links;
            }

            if (targetUmc) {
                const targetGteCodes = vehicle.gt_links.filter(el => el.gt_umc === targetUmc);

                if (targetGteCodes.length) {
                    this.gteCodes = {
                        umc: targetGteCodes[0].gt_umc,
                        makeId: vehicle.id_make,
                        gtDrv: targetGteCodes[0].gt_drv,
                        gtEng: targetGteCodes[0].gt_eng,
                        gtMod: targetGteCodes[0].gt_mod
                    };
                }
            }

            if (this.gtmotiveData) {
                if (this.gtmotiveData.equipmentItems) {
                    this.gteCodes.equipments = this.gtmotiveData.equipmentItems;
                }

                if (this.gtmotiveData.equipmentRanks) {
                    this.gteCodes.equipmentRanks = this.gtmotiveData.equipmentRanks;
                }

                this.gteCodes.vin = this.gtmotiveData.vin;
            }
        }
    }

    initializeGte(scriptUrl) {
        const gtmotiveVehicleScriptUrl = scriptUrl;
        ((d, s) => {
            const f = d.getElementsByTagName(s)[0];
            const j: any = d.createElement(s);
            j.async = true;
            j.src = gtmotiveVehicleScriptUrl;
            if (f) {
                f.parentNode.insertBefore(j, f);
            }
        })(document, 'script');
    }

    reinitializePartsApi(scriptUrl) {
        const gtmotivePartsScriptUrl = scriptUrl;
        const scriptEl = document.getElementById('gtmotive-js-api');
        if (scriptEl) {
            scriptEl.parentNode.removeChild(scriptEl);
        }
        const gtmotivePartsScript = document.createElement('script');
        gtmotivePartsScript.setAttribute('id', 'gtmotive-js-api');
        gtmotivePartsScript.setAttribute('src', gtmotivePartsScriptUrl);
        document.body.appendChild(gtmotivePartsScript);
    }

    resetData() {
        this.vinCode = null;
        this.estimateId = null;
        this.isVinMode = null;
        this.vehicle = null;
        this.gtmotiveData = null;
        this.gteCodes = {};
        this.isMultiGTcodes = null;
        this.gtLinks = null;
        this.gtmArticles = [];
        this.allArticles = [];
        this.allSelectedCategoryIds = [];
        this.queuedCategoryIds = [];
    }

    private createDateRankItem(value: string) {
        return { family: 'RNG', subFamily: 'FEC', value };
    }

    private isExistedGroupInList(item: any, list: any[]) {
        return list.some(el => item.functionalGroup === el.functionalGroup);
    }

    private updateExistedPartInList(part: any, group: any, list: any[]) {
        list.forEach(item => {
            if (item.functionalGroup === group.functionalGroup) {
                item.parts.push(part);
            }
        });

        return list;
    }

    private mapGtData(res) {
        if (get(res, 'data.articles.content.length')) {
            res.data.articles.content = this.toArticleDocs(res.data.articles.content);
        }
        if (get(res, 'data.directMatches.content.length')) {
            res.data.directMatches.content = res.data.directMatches.content.map(item => {
                return {
                    ...item,
                    directMatchesArticles: this.toArticleDocs(item.directMatchesArticles)
                };
            })
        }
        if (get(res, 'data.cupisResponse.articles.content.length')) {
            res.data.cupisResponse.articles.content = this.toArticleDocs(res.data.cupisResponse.articles.content);
        }
        return res;
    }

    private toArticleDocs(articleDocs: Array<any>): Array<ArticleModel> {
        return articleDocs.map(item => this.toArticleDoc(item));
    }

    private toArticleDoc(articleDoc: any): ArticleModel {
        return new ArticleModel(articleDoc);
    }
}
