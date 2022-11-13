import {
    Directive,
    OnInit,
    AfterViewInit,
    OnChanges,
    Input,
    Output,
    EventEmitter,
    ElementRef,
    SimpleChanges,
    HostListener
} from '@angular/core';
import { SAG_COMMON_ASC_LOWERCASE, SAG_COMMON_DESC_LOWERCASE } from '../constants/sag-common.constant';
import { SagCommonSortObj } from '../models/sag-lib-sort-obj';

@Directive({
    selector: '[sagCommonSortable]'
})
export class SagCommonSortableDirective implements OnInit, AfterViewInit, OnChanges {
    private handler = this.createHandler('fa fa-sort');
    private ascHandler = this.createHandler('fa fa-sort-asc');
    private descHandler = this.createHandler('fa fa-sort-desc');
    private currentHandler;
    @Input() sagCommonSortable = '';
    @Input() sort: SagCommonSortObj;
    @Output() sortChange: EventEmitter<SagCommonSortObj> = new EventEmitter<SagCommonSortObj>();
    constructor(private el: ElementRef) { }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes.sort && !changes.sort.firstChange) {
            if (this.sort.field !== this.sagCommonSortable) {
                if (!this.currentHandler.isEqualNode(this.handler)) {
                    this.removeCurrentHanlder();
                    this.renderHanlder(null); // reset
                }
            } else if (this.sort.force) {
                this.removeCurrentHanlder();
                this.renderHanlder(this.sort);
            }
        }
    }

    ngOnInit() {

    }

    @HostListener('click')
    onMouseClick() {
        const result: SagCommonSortObj = {
            field: this.sagCommonSortable,
            direction: (this.sort && this.sort.field === this.sagCommonSortable) ? this.sort.direction : ''
        };
        if (result.direction === SAG_COMMON_ASC_LOWERCASE) {
            result.direction = SAG_COMMON_DESC_LOWERCASE;
        } else {
            result.direction = SAG_COMMON_ASC_LOWERCASE;
        }
        this.sort = { ...result };
        this.sortChange.emit(this.sort);
        this.removeCurrentHanlder();
        this.renderHanlder(this.sort);
    }

    ngAfterViewInit(): void {
        this.el.nativeElement.classList.add('sortable');
        if (this.sort && this.sort.field === this.sagCommonSortable) {
            this.renderHanlder(this.sort);
        } else {
            this.renderHanlder(null); // initial
        }
    }

    private renderHanlder(sort: SagCommonSortObj) {
        if (sort && sort.direction === SAG_COMMON_ASC_LOWERCASE) {
            this.currentHandler = this.ascHandler.cloneNode();
        } else if (sort && sort.direction === SAG_COMMON_DESC_LOWERCASE) {
            this.currentHandler = this.descHandler.cloneNode();
        } else {
            this.currentHandler = this.handler.cloneNode();
        }
        this.el.nativeElement.appendChild(this.currentHandler);
    }

    private createHandler(customClass?) {
        const handler = document.createElement('i');
        handler.className = customClass;
        handler.style.marginLeft = '5px';
        return handler;
    }

    private removeCurrentHanlder() {
        if (this.currentHandler) {
            this.currentHandler.parentNode.removeChild(this.currentHandler);
        }
    }
}
