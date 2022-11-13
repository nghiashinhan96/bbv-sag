import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { tap, takeUntil, switchMap } from 'rxjs/operators';
import { ActivatedRoute, Router } from '@angular/router';
import { Subject, of } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { AffiliateUtil, UrlUtil, SAG_COMMON_LANG_DE } from 'sag-common';
import { SagGtmotiveConfigService } from '../../services/gtmotive-config.service';
import { GT_TYPE } from '../../enums/gtmotive.enum';
import { SagGtmotiveComponent } from '../../components/gtmotive/gtmotive.component';
import { GtmotiveService } from '../../services/gtmotive.service';
import { LocalStorageService } from 'ngx-webstorage';
import { SAG_COMMON_OIL_IDS } from 'sag-common';

@Component({
    selector: 'sag-gtmotive-page',
    templateUrl: 'gtmotive.component.html'
})
export class SagGtmotivePageComponent implements OnInit, OnDestroy {
    GT_TYPE = GT_TYPE;
    gteIsShown: boolean;
    vinCode: string;
    estimateId: string;
    searchByVin: boolean;
    isVinMode: boolean;
    gtType: GT_TYPE;
    errorMessage: string;
    vehid: string;

    params: any;

    lang;

    resetOnDestroy = true;

    private destroy$ = new Subject();

    @ViewChild(SagGtmotiveComponent, { static: true }) gtmotiveComponent: SagGtmotiveComponent;

    constructor(
        private router: Router,
        private translateService: TranslateService,
        private storage: LocalStorageService,
        private activatedRoute: ActivatedRoute,
        private config: SagGtmotiveConfigService,
        public gtMotiveService: GtmotiveService
    ) { }

    ngOnInit() {
        this.config.spinner.start();
        this.lang = this.getLang();

        this.gtMotiveService.initializeGte(this.config.gtmotive.vehicleEndpoint + this.config.gtmotive.apiKey);
        this.gtMotiveService.reinitializePartsApi(this.config.gtmotive.partsEndpoint + this.config.gtmotive.apiKey);

        this.activatedRoute.params.pipe(
            takeUntil(this.destroy$),
            switchMap((params) => {
                this.params = params;
                this.searchByVin = UrlUtil.toBoolean(this.params.searchByVin);
                this.gtType = params.type;

                this.estimateId = params.estimateId || this.gtMotiveService.estimateId;
                this.vehid = params.vehicleId;

                if (params.vinCode) {
                    this.vinCode = params.vinCode;

                    return this.searchVehicleByVin();
                }

                return of(null);

            }),
            tap(() => {
                this.config.spinner.stop();
                if (this.gtMotiveService.gteCodes.equipments) {
                    this.activeGte();
                } else {
                    if (this.vehid) {
                        this.router.navigate(['/vehicle', this.vehid]);
                    } else {
                        this.router.navigate(['/home']);
                    }
                }
            })).subscribe();
    }

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.complete();
        if (this.resetOnDestroy) {
            this.gtMotiveService.resetData();
            this.resetTree();
        }
    }

    getPartsFromGtmotive(data) {
        this.resetOnDestroy = false;
        this.gtMotiveService.emitSearch({
            makeCode: this.gtMotiveService.gtmotiveData.makeCode,
            operations: data.operations,
            vin: this.gtMotiveService.gtmotiveData.vin,
            estimateId: this.estimateId,
            cupis: data.cupis,
            vehicleId: this.gtMotiveService.vehicle.vehid,
            partCodes: data.partCodes
        });
        this.router.navigate([`vehicle/${this.gtMotiveService.vehicle.id}/articles`], {
            queryParams: {
                ...this.params,
                isGtmotive: true
            },
            replaceUrl: true
        });
    }

    private activeGte() {
        if (this.gtmotiveComponent) {
            this.gteIsShown = true;
            this.gtmotiveComponent.activeGte();
        }
    }

    private searchVehicleByVin() {
        if (!this.vinCode) {
            return of(null);
        }

        // this.fbRecordingService.recordSearchByVinAction(this.vinCode);
        return this.gtMotiveService.searchVehicleByVin({ estimateId: this.estimateId, vin: this.vinCode }).pipe(
            switchMap((res: any) => {
                if (!res || !res.data) {
                    return of(null);
                }

                this.isVinMode = true;
                this.estimateId = res.data.estimateId;
                this.gtMotiveService.gtmotiveData = res.data.gtmotiveResponse || {};
                this.gtMotiveService.vehicle = res.data.vehicle || {};

                this.gtMotiveService.handleVehicleResponse(res.data.vehicle);

                return of(res.data.vehicle);
            })
        );
    }

    private getLang() {
        if (AffiliateUtil.isBaseAT(this.config.affiliate)) {
            return SAG_COMMON_LANG_DE;
        }
        return this.translateService.currentLang;
    }

    private resetTree() {
        this.storage.clear(SAG_COMMON_OIL_IDS);
        this.gtMotiveService.gtmArticles = [];
        this.gtMotiveService.allArticles = [];
        this.gtMotiveService.allSelectedCategoryIds = [];
    }
}
