import { Directive, OnInit, ElementRef, Input } from '@angular/core';
declare var JsBarcode;
const BARCODE_CONFIG = {
    textAlign: 'center',
    textPosition: 'bottom',
    fontOptions: 'bold',
    fontSize: 16,
    textMargin: 5,
    height: 80
};

@Directive({
    selector: '[connectBarcode]'
})
export class ConnectBarCodeDirective implements OnInit {
    @Input() connectBarcode: string;
    constructor(private element: ElementRef) { }

    ngOnInit(): void {
        JsBarcode(this.element.nativeElement, this.connectBarcode, BARCODE_CONFIG);
    }

}
