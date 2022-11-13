import { OnInit, Component, Input, OnDestroy, Output, EventEmitter, OnChanges, SimpleChanges, AfterViewInit } from '@angular/core';
import { finalize, first, takeUntil } from 'rxjs/operators';
import { forkJoin, Observable, Subject } from 'rxjs';
import { cloneDeep, union, partition, isEqual } from 'lodash';
import { SagGtmotiveConfigService } from '../../services/gtmotive-config.service';
import { GT_LANG, GT_TYPE } from '../../enums/gtmotive.enum';
import { FunctionalGroup, Part } from '../../models/functional-group.model';
import { GtmotiveService } from '../../services/gtmotive.service';
import { GtmotiveSubmittedDataModel } from '../../models/gtmotive.model';
import { SagGtmotiveMultiPartsModalComponent } from '../gt-multi-parts-modal/gt-multi-parts-modal.component';
import { SagGtmotiveAdditionalEquipmentsModalComponent } from '../gt-additional-equipments-modal/gt-additional-equipments-modal.component';
import { BsModalService } from 'ngx-bootstrap/modal';
import { SubSink } from 'subsink';

declare var GTEapi: any;
declare var gtMotiveNavigationBoard: any;

@Component({
    selector: 'sag-gtmotive',
    templateUrl: './gtmotive.component.html'
})
export class SagGtmotiveComponent implements OnInit, OnDestroy, OnChanges, AfterViewInit {
    @Input() lang: string;
    @Input() vehicleInfo: any;
    @Input() gteCodes: any;
    @Input() estimateId: any;
    @Input() isVinMode = false;
    @Input() gtType: GT_TYPE;

    @Output() submitParts = new EventEmitter();

    TAB = {
        NAVIGATION: 'board-navigation',
        COMPOSITION: 'composition',
        PARTS_TREE: 'tree'
    };

    gteIsShown: boolean;
    activeTab = this.TAB.NAVIGATION;
    hasLoadedPluginSuccess = false;
    showFooter = false;
    isGraphicLoaded = false;
    readyToClose = true;
    graphicSpinner = '.gte-section';
    partSpinner = '.gte-section .tab-content';

    readonly VEHICLE = 'VEHICLE';
    readonly SERVICE_SCHEDULE = 'SERVICE_SCHEDULE';
    readonly MAINTENANCE_CODE = '98000';
    readonly MOTOROL_PARTCODE = 'G0600';

    gtEquipments: any;
    equipmentRanks: any;
    gtResponseInfo: any;
    vehicleData: any;
    partsTree: FunctionalGroup[] = [];
    partsList = [];
    separatedColumn = [];
    selectedZoneId: any;
    selectedDate: any;

    private selectedPartFromTree: any;
    private selectedPartCode: any;
    private multiPartsData: any;
    private partCodeInQueue = [];
    private colectedOperations = [];
    private cupis = [];
    private submittedData = new GtmotiveSubmittedDataModel();
    private langTag: any;
    resizeTimer: any;
    public readonly POLL_INTERVAL: number = 100;

    private destroy$ = new Subject<boolean>();
    private cancel$ = new Subject<boolean>();
    private pollSubject: Observable<any>;
    private collectListPartFinished$ = new Subject<boolean>();

    subs = new SubSink();

    constructor(
        private bsModalService: BsModalService,
        private config: SagGtmotiveConfigService,
        private gtMotiveService: GtmotiveService
    ) {}
    
    ngAfterViewInit(): void {
        let timeout = this.POLL_INTERVAL;

        this.pollSubject = new Observable(observer => {
            let interval = setInterval(() => {
                if (gtMotiveNavigationBoard && Object(gtMotiveNavigationBoard).length !== 0) {
                    observer.next(true);
                    clearInterval(interval);
                }
            }, timeout);

            return () => {
                clearInterval(interval);
            };
        });
    }
    ngOnChanges(changes: SimpleChanges): void {
        if(changes && changes['gteCodes']) {
            this.activeGte();
        }
    }

    ngOnInit() {
        this.langTag = GT_LANG[this.lang].toString();
    }

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.complete();
        this.subs.unsubscribe();
    }

    get isMaintenance() {
        return this.selectedZoneId === this.MAINTENANCE_CODE;
    }

    get isServiceSchedule() {
        return this.gtType === this.SERVICE_SCHEDULE;
    }

    activeGte() {
        if (!this.gteCodes || (this.gteCodes && Object.keys(this.gteCodes).length === 0)) {
            return;
        }

        this.gteIsShown = true;
        this.toggleFooter(false);

        this.collectListParts();
        this.config.spinner.start(this.graphicSpinner);

        this.subs.sink = this.collectListPartFinished$.subscribe(() => {
            this.initNavBoard();
            this.config.spinner.stop(this.graphicSpinner);
        });
    }

    selectTab(tab, callback?) {
        this.activeTab = tab;
        if (tab === this.TAB.COMPOSITION && !this.isGraphicLoaded) {
            this.isGraphicLoaded = true;
            this.initGraphic().then(() => {
                if (callback) {
                    callback();
                }
            });
        } else {
            if (callback) {
                callback();
            }
        }
    }

    refreshListPart() {
        if (this.partsList.length) {
            this.cancel$.next(true);
            this.cancel$.complete();
            GTEapi.unselectPart();
            this.partsList.forEach(partSet => {
                GTEapi.deletePart({ partCodeList: [partSet.partCode] });
            });
            this.partsList = [];
            this.submittedData = new GtmotiveSubmittedDataModel();
            this.selectPartsOnList();
        }
    }

    resetZoom() {
        GTEapi.resetZoom();
    }

    pickPartOnTree({zoneId, partCode}) {
        this.selectedPartFromTree = partCode;

        this.selectTab(this.TAB.COMPOSITION, () => {
            if (this.selectedZoneId !== zoneId && gtMotiveNavigationBoard) {
                gtMotiveNavigationBoard.selectFunctionalGroups(zoneId, true);
            }
        });
    }

    onSubmitParts() {
        if (this.hasLoadedPluginSuccess) {
            this.submitParts.emit(this.submittedData);
            this.closeGte();
        }
    }

    deletePart(operation, part) {
        const operations = part.operations;
        if (operations.length > 1) {
            part.operations = part.operations.filter(o => !isEqual(o, operation));
            this.submittedData.operations = this.submittedData.operations.filter(o => !isEqual(o, operation));
        } else {
            this.deletePartSet(part);
        }
    }

    toggleFooter(showFooter?: boolean) {
        const compositionObject = document.querySelector('#dynamic-composition > object') as HTMLElement;
        if (compositionObject && compositionObject.hidden) {
            compositionObject.style.display = 'block';
        }

        if (showFooter === undefined) {
            this.showFooter = !this.showFooter;
        } else {
            this.showFooter = showFooter;
        }

        document.body.classList.toggle('footer-hide', !this.showFooter);
        this.updateGteLayout();
        if (compositionObject) {
            compositionObject.style.display = 'none';
        }
    }

    private closeGte() {
        this.cancel$.next(true);
        this.cancel$.complete();
        GTEapi.clearGraphicZone();
        this.selectedZoneId = null;
        this.separatedColumn = [];
        this.gteIsShown = false;
        this.resetColectedPartData();
        this.selectTab(this.TAB.NAVIGATION);
        this.toggleFooter(true);
    }

    private initNavBoard() {
        this.config.spinner.start(this.graphicSpinner);

        const modelInfo = {
            model: this.gteCodes.umc,
            query: [
                `language=${this.langTag}`,
                `equipment=`
            ]
        };

        this.subs.sink = this.pollSubject.subscribe(value => {
            const config = { containerId: 'board-navigation', fontFamily: '"Roboto", sans-serif' };
            gtMotiveNavigationBoard.setOnFunctionalGroupSelected((zoneId) => {
                this.selectTab(this.TAB.COMPOSITION, () => this.updateGraphic(zoneId));
            })
            .init(config)
            .selectModel(modelInfo)
            .then(() => {
                this.updateGteLayout();
                this.config.spinner.stop(this.graphicSpinner);
                this.hasLoadedPluginSuccess = true;
            })
            .then(() => {
                if (this.isServiceSchedule) {
                    this.selectTab(this.TAB.COMPOSITION, () => this.updateGraphic(this.MAINTENANCE_CODE));
                }
            });
        });
    }

    private initGraphic() {
        if (!GTEapi) {
            return;
        }

        return GTEapi.userConf({
            containerId: 'dynamic-composition',
            selected: '0X80B2FF'
        })
            .then(GTEapi.renderApp())
            .then(() => {
                GTEapi.onAppError(error => {
                    console.log(error);
                });
                GTEapi.onPartClick(res => {
                    if (res) {
                        this.togglePartInTree(res.partCode, true);

                        const hasStoredPart = this.partsList.some(item => item.partCode === res.partCode);
                        const hasQueuedPart = this.partCodeInQueue.indexOf(res.partCode) !== -1;
                        if (hasStoredPart || hasQueuedPart) {
                            return;
                        }

                        this.partCodeInQueue.push(res.partCode);
                        this.selectedPartCode = res.partCode;

                        if (this.isMaintenance) {
                            this.resetMaintenanceColectedData();
                            this.searchReferencesByShortNumber(res.partCode);
                        } else {
                            this.searchReferencesByPartCode(res.partCode);
                        }

                        this.selectedPartFromTree = null;
                    }
                });
            });
    }

    private updateGraphic(zoneId: any) {
        this.readyToClose = false;
        this.config.spinner.start(this.graphicSpinner);

        if (this.selectedZoneId === zoneId) {
            this.config.spinner.stop(this.graphicSpinner);
            if (this.selectedPartFromTree) {
                GTEapi.selectPart(this.selectedPartFromTree, true);
            }
            return;
        }

        GTEapi.selectGraphicZone({
            model: this.gteCodes.umc,
            id: zoneId,
            query: [
                'jobType=All',
                `language=${this.langTag}`,
                `equipments=${this.gteCodes.equipments.join()}`
            ]
        }).then(() => {
            this.readyToClose = true;
            this.config.spinner.stop(this.graphicSpinner);
            this.selectedZoneId = zoneId;
            if (this.selectedPartFromTree) {
                GTEapi.selectPart(this.selectedPartFromTree, true);
            }
            const compositionObject = document.querySelector('#dynamic-composition > object') as HTMLElement;
            if (compositionObject) {
                compositionObject.style.display = 'none';
            }
        });
    }

    private deletePartSet(part) {
        this.togglePartInTree(part.partCode, false);
        GTEapi.deletePart({ partCodeList: [part.partCode] });
        this.partsList = this.partsList.filter(p => !isEqual(p, part));
        this.submittedData.operations = this.submittedData.operations.filter(o => !isEqual(o, part.operations[0]));

        if (part.isCupi) {
            this.submittedData.cupis = this.submittedData.cupis.filter(c => c !== part.partCode);
        }
    }

    private selectDate(date) {
        if (date && date.singleDate && date.singleDate.formatted) {
            this.selectedDate = date.singleDate.formatted.replace(/\./g, '');
        }
    }

    private findPartInTree(partCode: string): Part {
        for (const group of this.partsTree) {
            const part = group.parts.find(p => p.partCode === partCode);
            if (part) {
                return part;
            }
        }
        return null;
    }

    private collectListParts() {
        if (this.partsTree && this.partsTree.length) {
            return;
        }

        this.config.spinner.start(this.graphicSpinner);
        const bodyRequest = {
            umc: this.gteCodes.umc,
            equipments: this.gteCodes.equipments,
            equipmentRanks: this.gteCodes.equipmentRanks
        };
        this.gtMotiveService.colectListParts(bodyRequest)
            .pipe(finalize(() => {
                this.config.spinner.stop(this.graphicSpinner);
                this.collectListPartFinished$.next(true);
            }))
            .subscribe((res: any) => {
                const partsTree = res.data.functionalGroups;
                this.partsTree.forEach(partSet => {
                    partSet.parts.forEach(part => {
                        part.functionalGroup = partSet;
                    });
                });
                this.partsTree = partsTree;
            });
    }

    private resetColectedPartData() {
        if (this.partsList.length) {
            this.partsList.forEach(partSet => {
                GTEapi.deletePart({ partCodeList: [partSet.partCode] });
            });
            this.partsList = [];
        }
        this.partsTree = [];
        this.multiPartsData = null;
        this.colectedOperations = [];
        this.selectedPartCode = null;
        this.submittedData = new GtmotiveSubmittedDataModel();
    }

    private selectPartsOnList() {
        this.partsTree.forEach((group: FunctionalGroup) => {
            group.parts.forEach(part => {
                part.selected = this.partsList.some(p => p.partCode === part.partCode);
                if (part.selected && part.functionalGroup) {
                    part.functionalGroup.open = true;
                }
            });
        });
    }

    private searchReferencesByShortNumber(shortNumber: string) {
        this.config.spinner.start(this.partSpinner);
        const requestBody = {
            partInfoRequest: {
                estimateId: this.estimateId,
                umc: this.gteCodes.umc,
                equipments: this.gteCodes.equipments
            },
            partUpdateRequest: {
                estimateId: this.estimateId,
                shortNumber
            }
        };

        this.gtMotiveService.searchReferencesByShortNumber(requestBody)
            .pipe(
                takeUntil(this.destroy$),
                takeUntil(this.cancel$),
                finalize(() => {
                    this.config.spinner.stop(this.partSpinner);
                    // release queue while search done
                    this.partCodeInQueue = this.partCodeInQueue.filter(item => item !== shortNumber);
                })
            )
            .subscribe((res: any) => {
                if (!res.data) {
                    return;
                }

                res.data.operations.forEach(operation => {
                    if (operation.shortNumber) {
                        operation.cupi = operation.shortNumber;
                    }
                });

                this.colectedOperations = res.data.operations.filter(el => el.shortNumber !== this.selectedPartCode);

                const multiReferencesParts =
                    this.colectedOperations.filter(el => el.multiReference && el.shortNumber !== this.MOTOROL_PARTCODE);

                if (multiReferencesParts.length) {
                    this.searchForMultiParts(multiReferencesParts);
                } else {
                    this.colectGtParts(true);
                }
            });
    }

    private colectGtParts(isMaintenance = false, cupi?) {
        if (!this.colectedOperations.length && !cupi) {
            this.selectPartsOnList();
            GTEapi.unselectPart();
            return;
        }

        const partData: any = {};

        this.colectedOperations.forEach(el => {
            el.isMaintenance = isMaintenance;
        });

        if (cupi) {
            let customDescription: string;
            const matchedPartFromTree = [];
            for (const group of this.partsTree) {
                this.gtMotiveService.searchInParts(group, cupi.toLowerCase(), matchedPartFromTree);
            }

            if (matchedPartFromTree.length) {
                customDescription = matchedPartFromTree[0].parts[0].partDescription;
            }

            this.submittedData.cupis.push(cupi);
            partData.isCupi = true;
            partData.partCode = cupi;
            partData.operations = [{
                description: customDescription ? customDescription : '',
                reference: cupi
            }];
        } else {
            this.submittedData.operations = [...this.submittedData.operations, ...this.colectedOperations];
            partData.partCode = this.selectedPartCode;
            partData.operations = this.colectedOperations;
        }

        partData.isMaintenance = isMaintenance;
        this.partsList.push(partData);
        setTimeout(() => {
            // sometimes GTE API has not response, using timeout as trick
            GTEapi.addPart({ partCodeList: [this.selectedPartCode] });
        });
    }

    private searchReferencesByPartCode(partCode: string, additionalEquips = [], isNewEquips?, callback?) {
        this.config.spinner.start(this.partSpinner);
        const equipments = union(this.gteCodes.equipments, additionalEquips);
        const equipmentRanks = this.gtMotiveService.collectRanks(this.gteCodes.equipmentRanks, this.selectedDate);

        const requestBody = {
            gtmotivePartsThreeSearchRequest: {
                partCode,
                functionalGroup: this.selectedZoneId,
                umc: this.gteCodes.umc,
                equipments,
                equipmentRanks
            },
            isVinMode: this.isVinMode
        };
        this.gtMotiveService.searchReferencesByPartCode(requestBody)
            .pipe(
                takeUntil(this.destroy$),
                takeUntil(this.cancel$),
                finalize(() => {
                    this.config.spinner.stop(this.partSpinner);

                    this.partCodeInQueue = this.partCodeInQueue.filter(item => item !== partCode);

                    if (callback) {
                        callback();
                    }
                })
            )
            .subscribe((res: any) => {
                if (!res.data) {
                    return;
                }

                if (res.data && res.data.cupi) {
                    this.cupis.push(res.data.cupi);
                    this.colectGtParts(false, res.data.cupi);
                    return;
                }

                if (res.data && res.data.equipmentOptionFamilies && res.data.equipmentOptionFamilies.length) {
                    if (!isNewEquips) {
                        this.handleGreenPartVinCase(res.data.equipmentOptionFamilies);
                    }
                    return;
                }

                if (!res.data || !res.data.operations || !res.data.operations.length) {
                    return;
                }

                res.data.operations.forEach(operation => {
                    operation.cupi = partCode;
                });

                this.colectedOperations = [...res.data.operations];
                this.handleReferenceNormalCase(res.data, this.colectedOperations);
            });
    }

    private handleReferenceNormalCase(data: any, operations: any) {
        if (operations && operations.length > 1) {
            this.handleMutiReferencesCase(data);
        } else {
            this.colectGtParts();
        }
    }

    private dimissPart(partCode, isMaintenance = false) {
        if (isMaintenance) {
            (this.colectedOperations || []).forEach(el => {
                if (el.shortNumber === partCode) {
                    const indexOfDeletedPart = this.colectedOperations.indexOf(el);
                    this.colectedOperations.splice(indexOfDeletedPart, 1);
                }
            });
            return;
        }

        this.colectedOperations = [];
    }

    private togglePartInTree(partCode, selected?: boolean) {
        const part = this.findPartInTree(partCode);
        if (part) {
            part.selected = selected !== undefined ? selected : !part.selected;
        }
    }

    // Multi Parts

    private selectPartFromMultiParts(selectedPart, isMaintenance = false) {
        if (isMaintenance) {
            this.colectedOperations.forEach(el => {
                if (el.shortNumber === selectedPart.partCode) {
                    el.reference = selectedPart.reference;
                }
            });
            return;
        }
        this.colectedOperations.splice(1);
        this.colectedOperations[0].reference = selectedPart.reference;
    }

    private handleMutiReferencesCase(data: any) {
        this.multiPartsData = cloneDeep(data);
        const itemsHasDesc = this.colectedOperations.filter(el => el.description);
        this.multiPartsData.description = itemsHasDesc.length ? itemsHasDesc[0].description : '';
        this.bsModalService.show(SagGtmotiveMultiPartsModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                multiPartsData: this.multiPartsData,
                selectPartFromMultiParts: (selectedPart, isMaintenance) => this.selectPartFromMultiParts(selectedPart, isMaintenance),
                dimissPart: this.dimissPart,
                close: () => {
                    this.colectGtParts();
                }
            },
            backdrop: 'static'
        });
    }

    private async searchForMultiParts(multiReferencesParts) {
        const multiReferencesList = [];
        const requests = [];
        const partRequestBody = {
            gtmotivePartsThreeSearchRequest: {
                umc: this.gteCodes.umc,
                equipments: this.gteCodes.equipments,
                equipmentRanks: this.gteCodes.equipmentRanks,
                partCode: null
            },
            isVinMode: this.isVinMode,
            isMaintenance: this.isMaintenance
        };

        this.config.spinner.start(this.graphicSpinner);
        multiReferencesParts.forEach(el => {
            partRequestBody.gtmotivePartsThreeSearchRequest.partCode = el.shortNumber;
            requests.push(this.gtMotiveService.searchReferencesByPartCode(cloneDeep(partRequestBody)));
        });
        const respones = await forkJoin(requests)
            .pipe(
                first(),
                finalize(() => this.config.spinner.stop(this.graphicSpinner))
            )
            .toPromise();

        respones.forEach(res => {
            multiReferencesList.push(res.data);
        });

        if (multiReferencesList.length) {
            this.updateMultiPartsDataForMaintenance(multiReferencesList[0]);
            this.handleMultiReferenceItem(multiReferencesList);
        }
    }

    private handleMultiReferenceItem(multiReferencesList) {
        this.bsModalService.show(SagGtmotiveMultiPartsModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                multiPartsData: this.multiPartsData,
                selectPartFromMultiParts: (selectedPart, isMaintenance) => this.selectPartFromMultiParts(selectedPart, isMaintenance),
                dimissPart: this.dimissPart,
                close: () => {
                    multiReferencesList.splice(0, 1);
                    if (multiReferencesList.length) {
                        this.updateMultiPartsDataForMaintenance(multiReferencesList[0]);
                        this.handleMultiReferenceItem(multiReferencesList);
                    } else {
                        this.colectGtParts(true);
                    }
                }
            },
            backdrop: 'static'
        });
    }

    private updateMultiPartsDataForMaintenance(multiReferencesList) {
        this.multiPartsData = multiReferencesList;
        this.multiPartsData.isMaintenance = true;
        this.colectedOperations.forEach(el => {
            if (this.multiPartsData.partCode === el.shortNumber) {
                this.multiPartsData.description = el.description;
            }
        });
    }

    // Additional Equipments

    private handleGreenPartVinCase(additionalEquipments) {
        const modalRef = this.bsModalService.show(SagGtmotiveAdditionalEquipmentsModalComponent, {
            ignoreBackdropClick: true,
            initialState: {
                gtEquipments: this.gteCodes.equipments,
                additionalEquipments,
                searchPartsWithNewEquips: (equips, callback) => this.searchReferencesByPartCode(this.selectedPartCode, equips, true, () => {
                    modalRef.hide();
                    if (callback) {
                        callback();
                    }
                }),
                selectDate: (val) => this.selectDate(val),
                close: () => {
                    this.selectedDate = null;
                }
            },
            class: 'modal-lg',
            backdrop: 'static'
        });
    }

    // Maintenance

    private resetMaintenanceColectedData() {
        this.submittedData.operations = this.submittedData.operations.filter(item => !item.isMaintenance);
        this.submittedData.cupis = this.submittedData.cupis.filter(item => !item.isMaintenance);

        const results = partition(this.partsList, item => item.isMaintenance);

        const partCodesToRemove = results[0].map(item => item.partCode);
        if (partCodesToRemove.length) {
            GTEapi.deletePart({ partCodeList: partCodesToRemove });
        }

        this.partsList = results[1];

        this.multiPartsData = null;
        this.colectedOperations = [];
    }

    private updateGteLayout() {
        const rootEl = document.querySelector('.gte-wrapper') as HTMLElement;
        const partSectionEl = document.querySelector('.part-section') as HTMLElement;
        const headerHeight = document.querySelector('.header-bar') as HTMLElement;
        const rootHeight = rootEl.clientHeight;

        [
            '.board-navigation-outer',
            '.composition-outer',
            '.list-parts-outer'
        ].forEach(item => {
            const el = document.querySelector(item);
            if (el) {
                (el as HTMLElement).style.height = `${rootHeight - headerHeight.clientHeight}px`;
            }
        });
        if (partSectionEl) {
            partSectionEl.style.height = `${rootHeight}px`;
        }
    }
}
