import { AfterViewInit, Component } from '@angular/core';

@Component({
    selector: 'connect-gtmotive',
    templateUrl: 'gtmotive.component.html'
})
export class GtmotiveComponent implements AfterViewInit {
    ngAfterViewInit() {
        const gtmotive = document.querySelector('.gte-wrapper') as HTMLElement;
        if (gtmotive) {
            gtmotive.style.top = `${parseInt(document.body.style.paddingTop, 10)}px`;
        }
    }
}
