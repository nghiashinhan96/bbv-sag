import { Component, ElementRef, EventEmitter, HostListener, Input, Output } from '@angular/core';
import { FormGroup } from "@angular/forms";
import { FavoriteItem } from "../../models/favorite-item.model";

@Component({
    selector: 'sag-article-favorite-list-content',
    templateUrl: './article-favorite-list-content.component.html'
})
export class SagArticleFavoriteListContentComponent {
    @Input() search: FormGroup;
    @Input() items: any[];

    @Output() itemClick = new EventEmitter();
    @Output() removeFavorite = new EventEmitter();
    @Output() editFavorite = new EventEmitter();
    @Output() doSearch = new EventEmitter();
    @Output() clearValue = new EventEmitter();
    @Output() scrollEnd = new EventEmitter();
    @Output() outsideClick = new EventEmitter();

    constructor(
        private ref: ElementRef
    ) { }

    @HostListener('document:click', ['$event']) onClickHandler(event: any) {
        const isInside = this.ref.nativeElement.contains(event.target);
        const container = document.querySelector('sag-article-favorite-list');
        const isContainer = container && container.contains(event.target);
        const editModal = document.querySelector('modal-container .article-favorite-edit-modal');
        const editModalContainer = editModal && editModal.parentElement || editModal;
        const isEditComment = editModalContainer && (editModalContainer.contains(event.target) || editModalContainer.isSameNode(event.target));
        const isSelectLastestComment = event.target.classList.contains('lastest-comment-item');
        if (isInside || isContainer || isEditComment || isSelectLastestComment) {
            return;
        }
        this.outsideClick.emit();
    }

    onItemClick(item: any) {
        this.itemClick.emit(item);
    }

    onRemoveFavorite(item: any) {
        this.removeFavorite.emit(item);
    }

    onEditFavorite(item: FavoriteItem) {
        this.editFavorite.emit(item);
    }

    onDoSearch() {
        this.doSearch.emit();
    }

    onClearValue(event?) {
        this.clearValue.emit(event);
    }

    onScrollEnd() {
        this.scrollEnd.emit();
    }
}