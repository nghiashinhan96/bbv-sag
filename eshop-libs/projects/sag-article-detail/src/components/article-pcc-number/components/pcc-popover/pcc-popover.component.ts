import { Component, ElementRef, HostListener, Input, OnInit, Renderer2 } from '@angular/core';

@Component({
  selector: 'sag-pcc-popover',
  templateUrl: './pcc-popover.component.html',
  styleUrls: ['./pcc-popover.component.scss']
})
export class SagPccPopoverComponent implements OnInit {
  @Input() pnrnPccs: string[] = [];
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
