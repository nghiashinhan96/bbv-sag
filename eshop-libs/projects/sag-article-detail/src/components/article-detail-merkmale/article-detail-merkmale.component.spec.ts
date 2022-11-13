import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TranslateModule } from '@ngx-translate/core';
import { ArticleModel } from '../../models/article.model';

import { SagArticleDetailMerkmaleComponent } from './article-detail-merkmale.component';

describe('SagArticleDetailMerkmaleComponent', () => {
    let component: SagArticleDetailMerkmaleComponent;
    let fixture: ComponentFixture<SagArticleDetailMerkmaleComponent>;
    let spy: any;
    const articleMockupObject: Partial<ArticleModel> = {
        genArtTxts: [
            {
                gaid: "11698",
                gatxtdech: "Winterreifen"
            }
        ],
        supplier: "HANKOOK",
        productAddon: "155/60R15 74T W452, Winter PW Hankook, Kraftstoffeffizienz: E, Nasshaftung: C, Rollgeräusch: 71, Geräuschpegel: 2",
        productBrand: "HANKOOK",
        artnrDisplay: "1020473",
    };
    const inputMockupArticle = new ArticleModel(articleMockupObject);

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [SagArticleDetailMerkmaleComponent],
            imports: [
                TranslateModule.forRoot(),
            ]
        })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(SagArticleDetailMerkmaleComponent);
        component = fixture.componentInstance;
        component.article = inputMockupArticle;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });

    it('should render merkmale container', () => {
        const compiled: HTMLElement = fixture.debugElement.nativeElement;
        const containerDiv = compiled.querySelector("div.article-detail-merkmale");
        expect(containerDiv).toBeTruthy();
    });

    it('updateGenArtInfo should work correctly', () => {
        spy = spyOn(component, 'updateGenArtInfo').and.callThrough();
        component.updateGenArtInfo();
        expect(component.updateGenArtInfo).toHaveBeenCalled();
        inputMockupArticle.genArtTxts = [
            {
                gaid: "11698",
                gatxtdech: "Testing value"
            }
        ];
        component.updateGenArtInfo();
        expect(component.genArtDescription).toEqual("Testing value");
        inputMockupArticle.genArtTxts = [];
        component.updateGenArtInfo();
        expect(component.genArtDescription).toEqual("");
    });

    it('checkSpecialRuleToShowProductAddon should work correctly ', () => {
        spy = spyOn(component, 'checkSpecialRuleToShowProductAddon').and.callThrough();
        component.checkSpecialRuleToShowProductAddon();
        expect(component.checkSpecialRuleToShowProductAddon).toHaveBeenCalled();
        const testingValue = "Texting Value";
        inputMockupArticle.productAddon = testingValue;
        component.genArtDescription = testingValue;
        component.shouldShowArticleNumberSection = true;
        component.checkSpecialRuleToShowProductAddon();
        expect(component.shouldShowProductAddonLine).toEqual(false);
    });

    describe('Article number section', () => {
        beforeEach(() => {
            component.shouldShowArticleNumberSection = true;
            fixture.detectChanges();
        });

        it('should render article number title', () => {
            const compiled: HTMLElement = fixture.debugElement.nativeElement;
            const firstHeadTag = compiled.querySelector("div.article-detail-merkmale > h4");
            const articleNumberText = firstHeadTag.textContent;
            expect(articleNumberText).toContain(inputMockupArticle.artnrDisplay);
        });

        it('should render genArt description', () => {
            const compiled: HTMLElement = fixture.debugElement.nativeElement;
            const genArtDescriptionSpanTag = compiled.querySelector("div.article-detail-merkmale > ul > li div > span");
            const genArtDescriptionText = genArtDescriptionSpanTag.textContent;
            expect(genArtDescriptionText).toContain(component.genArtDescription);
        });

        it('should render supplier', () => {
            const compiled: HTMLElement = fixture.debugElement.nativeElement;
            const supplierSpanTag = compiled.querySelector("div.article-detail-merkmale > ul > li > div > span:nth-child(2)");
            const supplierText = supplierSpanTag.textContent;
            expect(supplierText).toContain(inputMockupArticle.supplier);
        });

        it('should render productAddon', () => {
            component.shouldShowProductAddonLine = true;
            fixture.detectChanges();
            const compiled: HTMLElement = fixture.debugElement.nativeElement;
            const productAddonContainerTag = compiled.querySelector("div.article-detail-merkmale > ul > li:nth-child(2)");
            expect(productAddonContainerTag).toBeTruthy();
            const productAddonSpanTag = compiled.querySelector("div.article-detail-merkmale > ul > li:nth-child(2) > div > span");
            const productAddonText = productAddonSpanTag.textContent;
            expect(productAddonText).toContain(inputMockupArticle.productAddon);
        });

        it('should not render productAddon', () => {
            component.shouldShowProductAddonLine = false;
            fixture.detectChanges();
            const compiled: HTMLElement = fixture.debugElement.nativeElement;
            const productAddonContainerTag = compiled.querySelector("div.article-detail-merkmale > ul > li:nth-child(2)");
            expect(productAddonContainerTag).toBeFalsy();
        });
    });

    afterAll(() => {
        TestBed.resetTestingModule();
    });
});