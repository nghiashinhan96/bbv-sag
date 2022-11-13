import { Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';
import { PopoverDirective } from 'ngx-bootstrap/popover';
import { SubSink } from 'subsink';
import { BrowserUtil } from 'sag-common';
import { FavoriteItem } from '../../models/favorite-item.model';
import { FavoriteService } from '../../services/favorite.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { debounceTime, distinctUntilChanged, filter } from 'rxjs/operators';
import { FAVORITE_BROADCAST_EVENT } from '../../consts/article-detail.const';

@Component({
    selector: 'sag-article-favorite-edit-modal',
    templateUrl: './article-favorite-edit-modal.component.html',
    styleUrls: ['./article-favorite-edit-modal.component.scss']
})
export class SagArticleFavoriteEditModalComponent implements OnInit, OnDestroy {
    @Input() title = "FAVORITE.ADD_FAVORITE"
    @Input() data: FavoriteItem;
    @Input() mode;

    onSave: any;
    onRemove: any;
    commentForm: FormGroup;

    latestComments = [];
    isIE = BrowserUtil.isIE();

    private subs = new SubSink();

    shouldShowRemoveBtn: boolean = false;

    @ViewChild('pop', { static: false }) popover: PopoverDirective;
    constructor(
        private bsModalService: BsModalService,
        public bsModalRef: BsModalRef,
        private favoriteService: FavoriteService,
        private fb: FormBuilder
    ) { }

    ngOnInit() {
        const conditionsToShowRemoveBtn = [FAVORITE_BROADCAST_EVENT.EDIT_ARTICLE, FAVORITE_BROADCAST_EVENT.EDIT_LEAF, FAVORITE_BROADCAST_EVENT.EDIT_VEHICLE];
        if (conditionsToShowRemoveBtn.indexOf(this.mode) !== -1) {
            this.shouldShowRemoveBtn = true;
        }
        this.commentForm = this.fb.group({
            comment: this.data && this.data.comment || ''
        });
        this.resetSearch();
        this.subs.sink = this.commentForm.get('comment').valueChanges.pipe(
            debounceTime(600),
            distinctUntilChanged(),
            filter((val: string) => {
                const text = val.replace(/\s/g, '');
                return text.length > 2;
            })
        ).subscribe(val => {
            if (val) {
                this.favoriteService.searchForComment(val).subscribe((res: any) => {
                    this.latestComments = res || [];
                    this.showPopup()
                })
            }
        });
    }

    resetSearch() {
        this.subs.sink = this.favoriteService.getListOfLatestComment().subscribe((res: any) => {
            this.latestComments = res || [];
        });
    }

    ngOnDestroy(): void {
        this.subs.unsubscribe();
    }

    removeFavorite() {
        if (this.onRemove) {
            this.onRemove(this.data);
        };
        this.hideModal();
    }

    saveFavorite() {
        if (this.onSave) {
            const item = { ...this.data };
            item.comment = this.comment;
            this.onSave(item)
        };
        this.hideModal();
    }

    onChooseComment(comment) {
        if (this.commentForm) {
            this.commentForm.get('comment').setValue(comment);
        }
        if (this.popover) {
            this.popover.hide();
        }
    }

    get comment() {
        return this.commentForm && this.commentForm.get('comment') && this.commentForm.get('comment').value || '';
    }

    showPopup() {
        if (this.popover) {
            this.popover.show();
        }
    }

    hideModal() {
        if (this.bsModalRef) {
            const sub = this.bsModalService.onHidden.subscribe(() => {
                const elements = document.querySelectorAll('.modal.show');
                if (elements && elements.length > 0) {
                    document.body.classList.add('modal-open');
                }
                sub.unsubscribe();
            });
            this.bsModalRef.hide();
        }
    }
}
