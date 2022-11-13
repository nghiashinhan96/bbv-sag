import { Directive, HostBinding, Input } from '@angular/core';

@Directive({
    selector: '[flexRow]'
})
export class SagFlexRowDirective {
    heightOfItem: number = 17; // Height of an item in the aws-models-collection-filter
    defaultRowItems: number = 7 // The default number of row items

    @Input('heightOfItem') set inputHeightOfItem(value) { this.heightOfItem = value };
    @Input('flexRow') set inputFlexRow(value) { this.row = ( value * this.heightOfItem) + 'px' };
    @HostBinding('style.maxHeight') row = (this.defaultRowItems * this.heightOfItem) + 'px';
}
