import { Component, OnInit, ViewChild, TemplateRef, OnDestroy, AfterViewInit } from '@angular/core';

import { SessionStorageService } from 'ngx-webstorage';
import { APP_RETURN_ARTICLE, APP_HEADER_SEARCH_CHANGE_EVENT } from 'src/app/core/conts/app.constant';
import { SAG_CURRENCY_INPUT_HORIZONTAL_MODE } from 'sag-currency';
import { interval, Observable, of, Subscription } from 'rxjs';
import { ArticleReturnService } from '../../services/article-return.service';
import { ReturnReasonCode } from '../../models/return-reason-code';
import { ReturnBasketState } from '../../models/return-basket.state';
import { UserService } from 'src/app/core/services/user.service';
import { Router } from '@angular/router';
import { BroadcastService, SagMessageData } from 'sag-common';
import { HeaderSearchTypeEnum } from 'src/app/layout/components/header/components/header-search/header-search-type.enum';
import { OrderAnalyticService } from 'src/app/analytic-logging/services/order-analytic.service';
import { SagTableColumn } from 'sag-table';
import { get } from 'lodash';
import { ReturnOrderInformationModalComponent } from '../../components/return-order-information-modal/return-order-information-modal.component';
import { switchMap } from 'rxjs/operators';
import { OrderBatchJobResponse, ReturnOrderBatchJobs, ReturnOrderResponse } from '../../models/return-order.model';
import { BATCH_JOB_STATUS, RETURN_ORDER_MESSAGE_TYPE } from '../../enums/return-order.enum';
import { BsModalService } from 'ngx-bootstrap/modal';

@Component({
    selector: 'connect-article-return-list',
    templateUrl: './article-return-list.component.html',
    styleUrls: ['./article-return-list.component.scss']
})
export class ArticleReturnListComponent implements OnInit, OnDestroy, AfterViewInit {

    columns = [];
    rows = [];
    isAllSelected = false;
    inputHorizontalMode = SAG_CURRENCY_INPUT_HORIZONTAL_MODE;
    conditionExpanded = true;
    reasonList = [];
    isPrintConfirmChecked;
    messenger: SagMessageData;
    enhancedUsedPartsReturnProcEnabled = false;
    submitted = false;
    private sublist: Subscription;
    private timer = null;
    @ViewChild('controlHeaderRef', { static: true }) controlHeaderRef: TemplateRef<any>;
    @ViewChild('controlCellRef', { static: true }) controlCellRef: TemplateRef<any>;
    @ViewChild('quantityRef', { static: true }) quantityRef: TemplateRef<any>;
    @ViewChild('quantityWarningRef', { static: true }) quantityWarningRef: TemplateRef<any>;
    @ViewChild('quarantaneRef', { static: true }) quarantaneRef: TemplateRef<any>;
    @ViewChild('returnReasonRef', { static: true }) returnReasonRef: TemplateRef<any>;
    @ViewChild('returnReasonWarningRef', { static: true }) returnReasonWarningRef: TemplateRef<any>;
    constructor(
        private sessionStorage: SessionStorageService,
        private articleReturnService: ArticleReturnService,
        private userService: UserService,
        private router: Router,
        private broadcaster: BroadcastService,
        private orderAnalyticService: OrderAnalyticService,
        private modalService: BsModalService
    ) { }

    ngAfterViewInit(): void {
        setTimeout(() => {
            this.broadcaster.broadcast(APP_HEADER_SEARCH_CHANGE_EVENT, HeaderSearchTypeEnum.RETURN);
        }, 0);
    }

    ngOnInit() {
        this.enhancedUsedPartsReturnProcEnabled = get(this.userService, 'userDetail.settings.enhancedUsedPartsReturnProcEnabled', false);
        this.columns = [
            {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.ID',
                id: 'articleId',
                sortable: false,
                filterable: false
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.DESCRIPTION',
                id: 'articleName',
                sortable: false,
                filterable: false,
                width: '80px'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.ASSIGNMENT',
                id: 'orderNr',
                sortable: false,
                filterable: false
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.AMOUNT',
                id: 'quantity',
                sortable: false,
                filterable: false,
                cellClass: 'text-right'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.REST',
                id: 'maxReturnQuantity',
                sortable: false,
                filterable: false,
                cellClass: 'text-right'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.STORAGE_LOCATION',
                id: 'branchId',
                sortable: false,
                filterable: false
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.PAYMENT',
                id: 'axPaymentType',
                sortable: false,
                filterable: false
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.DEADLINE',
                id: 'termOfPayment',
                sortable: false,
                filterable: false
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.CASH_DISCOUNT',
                id: 'cashDiscount',
                sortable: false,
                filterable: false
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.TYPE',
                id: 'sourcingType',
                sortable: false,
                filterable: false
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.QUARANTINE',
                id: 'quantity',
                sortable: false,
                filterable: false,
                cellTemplate: this.quarantaneRef
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.TABLE_COLUMNS.NUMBER',
                id: 'returnQuantity',
                sortable: false,
                filterable: false,
                cellTemplate: this.quantityRef,
                cellClass: 'col-amount',
                class: 'text-center',
                width: '75px'
            }, {
                sortable: false,
                filterable: false,
                cellTemplate: this.quantityWarningRef,
                cellClass: 'col-amount-warning'
            }, {
                i18n: 'RETURN_ARTICLE_BASKET.RETURN_REASON_TITLE',
                id: 'returnReason',
                sortable: false,
                filterable: false,
                cellTemplate: this.returnReasonRef,
                cellClass: 'col-reason',
                width: '120px'
            }, {
                sortable: false,
                filterable: false,
                cellTemplate: this.returnReasonWarningRef,
                cellClass: 'col-reason-warning'
            }, {
                sortable: false,
                filterable: false,
                headerTemplate: this.controlHeaderRef,
                cellTemplate: this.controlCellRef,
                class: 'position-relative',
                cellClass: 'position-relative',
                width: '60px'
            }
        ] as SagTableColumn[];
        this.rows = (this.sessionStorage.retrieve(APP_RETURN_ARTICLE) || []).map(art => {
            if (!art.maxReturnQuantity) {
                art.maxReturnQuantity = art.returnQuantity;
            }
            art.checked = false;
            return art;
        });
        this.filterDepotArticle();
        if (this.rows.length > 0) {
            this.getReturnReasonCode();
        }
        this.sublist = this.sessionStorage
            .observe(APP_RETURN_ARTICLE)
            .subscribe(articles => {
                this.messenger = null;
                this.rows = (articles || []).map(art => {
                    if (!art.maxReturnQuantity) {
                        art.maxReturnQuantity = art.returnQuantity;
                    }
                    art.checked = false;
                    return art;
                });
                this.filterDepotArticle();
                if (this.rows.length > 0) {
                    this.getReturnReasonCode();
                }
            });
    }

    ngOnDestroy(): void {
        this.sublist.unsubscribe();
        this.sessionStorage.clear(APP_RETURN_ARTICLE);
    }

    filterDepotArticle() {
        if (this.enhancedUsedPartsReturnProcEnabled) {
            this.rows = this.rows.filter(r => !r.isDepotReturnArticle);
        }
    }

    submitReturnBasket(callback) {
        this.submitted = true;
        this.messenger = null;
        const positions = this.articleReturnService.buildReturnBasketPositions(this.rows);
        if (positions.some(p => !p.reasonCode)) {
            callback();
            return;
        }
        const salesPersonalNumber = this.userService.employeeInfo && this.userService.employeeInfo.id;
        const submittedReturnBasket = {
            branchId: this.rows[0].branchId,
            positions,
            printConfirmDoc: this.isPrintConfirmChecked,
            returnOrderJournalPostingInBatch: true,
            salesPersonalNumber,
            validToExecute: true
        } as ReturnBasketState;

        this.articleReturnService.createReturnOrder(submittedReturnBasket)
            .subscribe((res: ReturnOrderResponse) => {
                const { returnOrder } = res
                if (res && returnOrder && returnOrder.batchJobId) {
                    const batchJobId = returnOrder.batchJobId;
                    const journalId = returnOrder.journalId;
                    const modal = this.modalService.show(ReturnOrderInformationModalComponent, {
                        ignoreBackdropClick: true,
                        initialState: {
                            onProcessInBackground: () => {
                                if (this.timer) {
                                    this.timer.unsubscribe();
                                };

                                this.articleReturnService.processInBackground(batchJobId, journalId).subscribe(
                                    () => {
                                        modal.hide();
                                        this.router.navigate(['/home']);
                                    },
                                    errorResponse => {
                                        let message = '';
                                        if (errorResponse.code === 408) {
                                            message = 'RETURN_ORDER.PROCESSING_IN_BACKGROUND_TIMEOUT';
                                        } else {
                                            message = this.articleReturnService.getSubmitReturnOrderErrorCode(errorResponse.error);
                                        }
                                        modal.hide();
                                        callback();
                                        this.handleNotifyMessage(batchJobId, message, RETURN_ORDER_MESSAGE_TYPE.WARNING);
                                    }
                                );

                            }
                        }
                    });

                    const successCallback = (value) => {
                        const returnOrd = value && value.returnOrder || {};
                        const items = [...this.rows];
                        this.sessionStorage.clear(APP_RETURN_ARTICLE);
                        modal.hide();
                        this.router.navigate(['return', 'confirmation'], {
                            queryParams: {
                                nonQuarantineOrderPositions: returnOrd.nonQuarantineOrderPositions,
                                quarantineOrderPositions: returnOrd.quarantineOrderPositions,
                                batchJobId: value,
                                messageType: RETURN_ORDER_MESSAGE_TYPE.SUCCESS
                            }
                        });
                        callback();
                        this.orderAnalyticService.sendReturnOrderEventData(items);
                    }

                    const errorCallBack = (message, type, showWarningTitle = false) => {
                        modal.hide();
                        callback();
                        this.handleNotifyMessage(batchJobId, message, type, showWarningTitle);
                    }

                    this.handleAsyncJob(batchJobId, successCallback, errorCallBack, journalId);
                }
            }, errRes => {
                const { error } = errRes;
                const errorMessage = `${error.code} - ${error.status}: ${error.message}`;
                this.messenger = {
                    type: 'ERROR',
                    message: this.articleReturnService.getSubmitReturnOrderErrorCode(errRes.error),
                    params: {
                        errorMessage
                    }
                } as SagMessageData;
                callback();
            });

    }

    updateRows() {
        this.sessionStorage.store(APP_RETURN_ARTICLE, this.rows);
    }

    onDeleteAllRecord() {
        this.rows = this.rows.filter(row => !row.checked);
        this.isAllSelected = false;
        this.sessionStorage.store(APP_RETURN_ARTICLE, this.rows);
    }

    onDeleteARecord(data) {
        this.rows = this.rows.filter(row => row.articleId !== data.articleId);
        this.sessionStorage.store(APP_RETURN_ARTICLE, this.rows);
    }

    onCheckArticle() {
        this.isAllSelected = this.rows.filter(row => !!row.checked).length === this.rows.length;
    }

    onAllArticleCheck(status) {
        this.rows.forEach(r => {
            r.checked = status;
        });
    }

    closeEditor(editor, data) {
        const input = editor.querySelector('input');
        input.value = data.quarantineText || '';
        editor.classList.remove('editor-shown');
        data.isQuarantined = !!data.quarantineText;
        this.sessionStorage.store(APP_RETURN_ARTICLE, this.rows);
    }

    updateEditor(editor, data) {
        const input = editor.querySelector('input');
        const newVal = input.value || '';
        if (newVal) {
            data.quarantineText = newVal;
            editor.classList.remove('editor-shown');
        }
        this.sessionStorage.store(APP_RETURN_ARTICLE, this.rows);
    }

    showEditor(editor, data) {
        if (editor.className.indexOf('editor-shown') === -1) {
            const input = editor.querySelector('input');
            input.value = data.quarantineText || '';
            editor.classList.add('editor-shown');
        }
    }

    enableEditor(isChecked, editor, data) {
        if (isChecked) {
            data.quarantineText = '';
            this.showEditor(editor, data);
        } else {
            data.quarantineText = '';
            editor.classList.remove('editor-shown');
            this.sessionStorage.store(APP_RETURN_ARTICLE, this.rows);
        }
    }

    private getReturnReasonCode() {
        this.articleReturnService.getReturnReasonCode().subscribe((reasonCodeList: ReturnReasonCode[]) => {
            this.reasonList = reasonCodeList.map((reason) => {
                if (reason.default) {
                    const defaultReason = reason.reasonId.toString();
                    (this.rows || []).forEach(item => {
                        if (!this.enhancedUsedPartsReturnProcEnabled || !item.hasDepotReturnArticle) {
                            item.returnReason = defaultReason;
                        }
                    });
                }
                return {
                    value: reason.reasonId.toString(),
                    code: reason.reasonCode,
                    label: `RETURN_ARTICLE_BASKET.RETURN_CONDITION.REASON_LIST.${reason.reasonCode}`
                };
            });
        });
    }

    private handleAsyncJob(batchJobId: string, successCallback: Function, errorCallback: Function, journalId: string) {
        this.timer = interval(5000).pipe(
            switchMap(() => this.articleReturnService.asyncReturnOrderProcess(batchJobId))
        ).subscribe(
            (res: ReturnOrderBatchJobs) => {
                const job = res.batchJobs.find(x => x.batchJobId.toString() === batchJobId.toString()) || { status: '' };
                switch (job.status.toUpperCase()) {
                    case BATCH_JOB_STATUS.FINISHED:
                        this.timer.unsubscribe();
                        this.articleReturnService.getReturnOderBatchjob(journalId).subscribe(
                            (returnOrderRes: OrderBatchJobResponse) => {
                                let orderNumber;
                                if (returnOrderRes && returnOrderRes.orderNumbers) {
                                    const temp = returnOrderRes.orderNumbers.find(order => order.journalId === journalId);
                                    orderNumber = temp ? temp.orderId : batchJobId
                                }
                                successCallback(orderNumber);
                            },
                            error => {
                                errorCallback('RETURN_ORDER.ORDER_WARNING_MESSAGE',RETURN_ORDER_MESSAGE_TYPE.WARNING, true);
                            });

                        break;
                    case BATCH_JOB_STATUS.ERROR:
                    case BATCH_JOB_STATUS.CANCELED:
                        this.timer.unsubscribe();
                        errorCallback('RETURN_ORDER.BATCH_JOB_ERROR_MESSAGE', RETURN_ORDER_MESSAGE_TYPE.ERROR);
                        break;
                    case BATCH_JOB_STATUS.HOLD:
                    case BATCH_JOB_STATUS.CANCELLING:
                        this.timer.unsubscribe();
                        errorCallback('RETURN_ORDER.PROCESSING_IN_BACKGROUND_RETRY_ERROR', RETURN_ORDER_MESSAGE_TYPE.WARNING);
                        break;
                    default:
                        break;
                }
            },
            errRes => {
                this.timer.unsubscribe();
                errorCallback('RETURN_ORDER.ORDER_WARNING_MESSAGE', RETURN_ORDER_MESSAGE_TYPE.WARNING);
            }
        );
    }

    private handleNotifyMessage(batchJobId, message, type: RETURN_ORDER_MESSAGE_TYPE = RETURN_ORDER_MESSAGE_TYPE.ERROR, showWarningTitle = false) {
        this.router.navigate(['return', 'confirmation'], {
            queryParams: {
                messageType: type,
                batchJobId: batchJobId,
                errorMessage: message ? message : null,
                dynamicTitle: showWarningTitle
            }
        });
    }
}
