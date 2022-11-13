import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Validator } from 'src/app/core/utils/validator';
import { ArticleType } from 'src/app/core/enums/article.enum';
import { OfferArticleModel } from '../../models/offer-article.model';
import { OffersService } from '../../services/offers.services';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { finalize } from 'rxjs/operators';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'connect-offer-article-form-modal',
    templateUrl: './offer-article-form-modal.component.html',
    styleUrls: ['./offer-article-form-modal.component.scss']
})

export class OfferArticleFormModalComponent implements OnInit {
    model: OfferArticleModel = new OfferArticleModel();
    type: ArticleType;

    callback: any;
    canBack: boolean;

    articleForm: FormGroup;
    attemptedSubmit: boolean;
    err: any;
    title: string;
    isArticle: boolean;

    spinnerSelector = '.modal-content';
    affiliateCode = environment.affiliate;

    constructor(
        public modalRef: BsModalRef,
        private formBuilder: FormBuilder,
        private articleSevice: OffersService
    ) { }

    ngOnInit() {
        this.isArticle = this.type === ArticleType.ARTICLE;
        this.articleForm = this.buildForm(this.model);
        this.title = this.getTitle();
    }

    private buildForm(model: OfferArticleModel) {
        return this.formBuilder.group({
            articleNumber: [model.articleNumber, [Validators.required]],
            name: model.name,
            description: model.description,
            amount: [model.amount, [Validators.required, Validator.validateForPositiveNumber]],
            price: [model.price, [Validators.required, Validator.validateForPositiveNumber]]
        });
    }

    save() {
        this.attemptedSubmit = true;
        if (!this.articleForm.valid) { return; }
        const model = this.repairModel(this.articleForm);

        SpinnerService.start(this.spinnerSelector)
        if (this.isCreatingMode()) {
            this.articleSevice.createOfferArticle(model)
                .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
                .subscribe(
                    (res) => {
                        this.handleSuccessResponse();
                    },
                    (err) => {
                        this.handleErrorResponse(err);
                    });
        } else {
            this.articleSevice.editOfferArticle(this.model.id, model)
                .pipe(finalize(() => SpinnerService.stop(this.spinnerSelector)))
                .subscribe(
                    (res) => {
                        this.handleSuccessResponse();
                    },
                    (err) => {
                        this.handleErrorResponse(err);
                    });
        }
    }

    private handleSuccessResponse() {
        this.attemptedSubmit = false;
        this.backToPreviousModel();
    }
    private handleErrorResponse(err) {
        this.err = 'OFFERS.SAVE_UNSUCCESSFULLY';
    }

    private isCreatingMode() {
        return !this.model.id;
    }

    private repairModel(form: FormGroup): OfferArticleModel {
        // let amountData - amount
        return new OfferArticleModel({
            ...form.value,
            type: ArticleType[this.type].toString()
        });
    }

    cancelAction() {
        this.attemptedSubmit = false;
        this.err = null;
        this.backToPreviousModel();
    }

    private getTitle() {
        if (this.isCreatingMode()) {
            if (this.isArticle) {
                return 'OFFERS.OWN_ARTICLE.CREATE_NEW_ARTICLE';
            }
            return 'OFFERS.OWN_WORK.CREATE_NEW_WORK';
        }
        if (this.isArticle) {
            return 'OFFERS.OWN_ARTICLE.EDIT_ARTICLE';
        }
        return 'OFFERS.OWN_WORK.EDIT_WORK';
    }

    allowNumber(event) {
        return Validator.allowNumber(event);
    }

    allowDecimalNumber(event, value) {
        return Validator.allowDecimalNumber(event, value);
    }

    backToPreviousModel() {
        this.modalRef.hide();
        if (this.callback) {
            this.callback();
        }
    }
}
