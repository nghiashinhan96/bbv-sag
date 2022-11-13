import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'connect-scroll-top',
    templateUrl: 'scroll-top.component.html'
})
export class ScrollTopComponent implements OnInit {
    constructor() { }

    ngOnInit() { }

    goTop() {
        window.scrollTo(0, 0);
    }
}
