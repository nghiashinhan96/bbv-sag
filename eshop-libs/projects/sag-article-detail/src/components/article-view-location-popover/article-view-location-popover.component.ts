import { Component, Input, OnInit, Renderer2, ElementRef, HostListener } from '@angular/core';
import { ArticleAvailLocationItemModel } from '../../models/article-avail-location-item.model';

@Component({
  selector: 'sag-article-view-location-popover',
  templateUrl: './article-view-location-popover.component.html',
  styleUrls: ['./article-view-location-popover.component.scss']
})
export class SagArticleViewLocationPopoverComponent implements OnInit {
  @Input() locations: ArticleAvailLocationItemModel[] = [];
  @Input() popupRef: any;

  private documentListener: any;

  constructor (
    private renderer: Renderer2,
    private ref: ElementRef
  ) { }

  @HostListener('click', ['$event'])
  onClickElement(event: any) {
    const checkClick = this.ref.nativeElement.contains(event.target);
    if (checkClick) {
      // do not excute document click;
      event.stopPropagation();
    }
  }

  ngOnInit() {
    setTimeout(() => {
      this.bindDocumentEditListener();
    });
  }

  closePopup() {
    this.unbindDocumentEditListener();
    this.popupRef.hide();
  }

  private bindDocumentEditListener() {
    if (!this.documentListener) {
      this.documentListener = this.renderer.listen('document', 'click', () => {
        this.unbindDocumentEditListener();
        this.popupRef.hide();
      });
    }
  }

  private unbindDocumentEditListener() {
    if (this.documentListener) {
      this.documentListener();
      this.documentListener = null;
    }
  }

}
