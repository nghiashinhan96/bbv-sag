import { Component, Input } from '@angular/core';

@Component({
    selector: 'connect-offer-header',
    templateUrl: 'offer-header.component.html',
    styleUrls: ['offer-header.component.scss']
})
export class OfferHeaderComponent {
    tabs = [{
        heading: 'OFFERS.MANAGE_OFFERS',
        routerLink: '/offers'
    }, {
        heading: 'OFFERS.MANAGE_CUSTOMERS',
        routerLink: '/offers/customers'
    }, {
        heading: 'OFFERS.MANAGE_YOUR_OWN_WORK',
        routerLink: '/offers/works'
    }, {
        heading: 'OFFERS.MANAGE_YOUR_ARTICLES',
        routerLink: '/offers/articles'
    }];

    @Input() title: string;

    constructor() { }
}
