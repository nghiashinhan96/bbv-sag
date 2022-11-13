import { Component, ViewChild, OnInit } from '@angular/core';

import { SalesSavingComponent } from '../sales-saving/sales-saving.component';
import { AadAccountService } from '../../services/aad-accounts/aad-accounts-service';
import { SalesSavingItemModel } from '../../model/sales-saving-item.model';


@Component({
    selector: 'backoffice-sales-creating',
    templateUrl: './sales-creating.component.html'
})
export class SalesCreatingComponent implements OnInit {

    @ViewChild(SalesSavingComponent, { static: false }) child: SalesSavingComponent;
    public model: SalesSavingItemModel;

    constructor(private aadAccountService: AadAccountService) {

    }

    ngOnInit() {
        this.model = SalesSavingItemModel.getDefaultModel();
    }

    public create(model: SalesSavingItemModel) {
        this.aadAccountService.create(model).subscribe(
            res => {
                this.child.handleSavingSuccessfullyCase();
            },
            err => {
                this.child.handleSavingFailedCase(err);
            });
    }

    public canDeactivate() {
        return this.child.handleDataChanged();
    }
}
