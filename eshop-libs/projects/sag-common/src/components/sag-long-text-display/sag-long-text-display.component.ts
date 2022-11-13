import { Component, Input, OnInit, AfterViewInit } from '@angular/core';

@Component({
    selector: 'sag-long-text-display',
    templateUrl: './sag-long-text-display.component.html',
    styleUrls: ['./sag-long-text-display.component.css']
})
export class SagLongTextDisplayComponent implements OnInit {

    @Input() maxLength = 300;
    @Input() content = '';
    isShowMore = false;
    isShowAll = true;

    displayContent = '';
    hiddenContent = '';

    constructor() { }

    ngOnInit() {
        if (this.content && this.content.length > this.maxLength) {
            this.isShowAll = false;
            this.displayContent = this.content.slice(0, this.maxLength - 1);
            this.hiddenContent = this.content.slice(this.maxLength-1);
            return;
        }
        this.isShowAll = true;
        this.displayContent = this.content;
    }

    toggleShow() {
        this.isShowMore = !this.isShowMore;
    }
}
