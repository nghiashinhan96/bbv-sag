import { Component, OnInit } from "@angular/core";
import { SubSink } from "subsink";
import { uniq } from 'lodash';
import { PAYMENT_METHOD } from "src/app/core/enums/shopping-basket.enum";
import { UserService } from "src/app/core/services/user.service";
import { ANALYTICAL_CARD_PAYMENT } from "../../enums/analytical-card/analytical-card.enum";
import { ActivatedRoute } from "@angular/router";

@Component({
    selector: 'connect-analytical-card',
    templateUrl: './analytical-card.component.html'
})
export class AnalyticalCardComponent implements OnInit {
    cards = [];
    selectedType = ANALYTICAL_CARD_PAYMENT.WHOLESALE;
    subs = new SubSink();

    constructor(
        private activatedRoute: ActivatedRoute,
        private userService: UserService
    ) { }

    ngOnInit() {
        this.subs.sink = this.userService.userDetail$.subscribe(data => {
            if (!data) {
                return;
            }
            const paymentMethod = this.activatedRoute.snapshot.queryParams['paymentMethod'];
            const paymentMethods = (data.orderLocations || []).reduce((acc, val) => {
                const methods = val.paymentMethods.map(p => p.descCode);
                return uniq([...acc, ...methods]);
            }, []);
            paymentMethods.forEach(item => {
                switch (item) {
                    case PAYMENT_METHOD.WHOLESALE:
                        this.cards.push({
                            paymentMethod: ANALYTICAL_CARD_PAYMENT.WHOLESALE,
                            text: 'CONDITION.PAYMENT_METHOD.WHOLESALE',
                            selected: paymentMethod === ANALYTICAL_CARD_PAYMENT.WHOLESALE
                        });
                        break;
                    case PAYMENT_METHOD.CASH:
                        this.cards.push({
                            paymentMethod: ANALYTICAL_CARD_PAYMENT.CASH,
                            text: 'CONDITION.PAYMENT_METHOD.CASH',
                            selected: paymentMethod === ANALYTICAL_CARD_PAYMENT.CASH
                        });
                        break;
                }
            });
            if (this.cards.length && !this.cards.some(c => c.selected)) {
                this.cards[0].selected = true;
            }
        });
    }

    selectTab(selectedItem) {
        this.cards.forEach(item => {
            if (item.paymentMethod === selectedItem.paymentMethod) {
                item.selected = true;
            } else {
                item.selected = false;
            }
        })
    }
}