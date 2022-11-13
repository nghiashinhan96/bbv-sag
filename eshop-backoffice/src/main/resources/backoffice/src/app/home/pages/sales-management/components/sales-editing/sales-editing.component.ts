import { Component, ViewChild, OnInit } from '@angular/core';
import { SalesSavingComponent } from '../sales-saving/sales-saving.component';
import { ActivatedRoute } from '@angular/router';
import { AadAccountService } from '../../services/aad-accounts/aad-accounts-service';
import { SalesSavingItemModel } from '../../model/sales-saving-item.model';


@Component({
    selector: 'backoffice-sales-editing',
    templateUrl: './sales-editing.component.html'
})
export class SalesEditingComponent implements OnInit {

    @ViewChild(SalesSavingComponent, { static: false }) child: SalesSavingComponent;

    public model: SalesSavingItemModel;
    private id: number;

    constructor(
        private aadAccountService: AadAccountService,
        private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.params.subscribe(params => {
            if (params && params.id) {
                this.id = params.id;
                this.aadAccountService.findById(this.id).subscribe(data => {
                    const res: any = data;
                    this.model = new SalesSavingItemModel(res);
                });
            }
        });
    }

    public edit(model: SalesSavingItemModel) {
        this.aadAccountService.edit(model, this.id).subscribe(
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
