import { Component, OnInit, AfterViewInit, Renderer2, OnDestroy } from '@angular/core';

@Component({
    selector: 'autonet-scroller',
    templateUrl: './scroller.component.html',
    styleUrls: ['./scroller.component.scss']
})
export class ScrollerComponent implements OnInit, OnDestroy {


    private documentListener;
    isShown = false;
    constructor(private renderer: Renderer2) { }

    ngOnInit() {
        if (!this.documentListener) {
            this.documentListener = this.renderer.listen('window', 'scroll', (event) => {
                this.checkView();
            });
        }
    }

    scrollTop() {
        window.scroll({
            top: 0,
            left: 0,
            behavior: 'smooth'
        });
    }

    ngOnDestroy(): void {
        if (this.documentListener) {
            this.documentListener();
            this.documentListener = null;
        }
    }
    private checkView() {
        this.isShown = (document.documentElement.scrollTop || 0) > 100;
    }
}
