import { Injectable } from '@angular/core';
import { Observable, of, Subject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { OffersResponse } from '../models/offers-response.model';
import { saveAs } from 'file-saver';
import { OfferPosition } from '../models/offer-position.model';
import { OfferDetail } from '../models/offer-detail.model';
import { OfferPerson } from '../models/offer-person.model';
import { OfferPersonsResponse } from '../models/offer-persons-response.model';
import { OfferArticleModel } from '../models/offer-article.model';
import { ArticleSearchCriteria } from '../models/article-search-criteria.model';
import { Constant } from 'src/app/core/conts/app.constant';
import { OfferArticlesResponse } from '../models/offer-article-response.model';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { OFFER_ITEM_TYPE } from '../enums/offers.enum';
import { TranslateService } from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
export class OffersService {

    private readonly OFFER_NUMBER_MAX_LENGTH = 10;
    private baseUrl = environment.baseUrl;

    public title = new Subject<string>();

    constructor(
        private http: HttpClient,
        private translateService: TranslateService,
        private fbRecordingService: FeedbackRecordingService
    ) { }

    createOffer(ignoreOfflineError = false): Observable<any> {
        const url = `${this.baseUrl}offer/create`;
        let options;
        if (ignoreOfflineError) {
            options = { headers: { IgnoreOfflineError: 'true' }};
        }
        return this.http.post(url, null, options);
    }

    getOffers(page = 0, pageSize = 10, request): Observable<OffersResponse> {
        const url = `${this.baseUrl}offer/search?page=${page}&size=${pageSize}`;
        return this.http.post(url, request).pipe(
            map((res: any) => new OffersResponse(res ? res.offers : {}))
        );
    }

    getOfferById(offerId, ignoreOfflineError = false): Observable<OfferDetail> {
        const url = `${this.baseUrl}offer/${offerId}`;
        let options;
        if (ignoreOfflineError) {
            options = { headers: { IgnoreOfflineError: 'true' }};
        }
        return this.http.post(url, null, options).pipe(
            map((res: any) => {
                if (res && res.offer && res.offer.offerNr) {
                    this.fbRecordingService.recordOfferNr(res.offer.offerNr);
                }
                return new OfferDetail(res && res.offer);
            }),
            catchError(() => {
                return of(null);
            })
        );
    }

    updateOffer(body, calculated?: boolean, ignoreOfflineError = false): Observable<any> {
        if (body && body.offerNumber) {
            this.fbRecordingService.recordOfferNr(body.offerNumber);
        }
        let options;
        if (ignoreOfflineError) {
            options = { headers: { IgnoreOfflineError: 'true' }};
        }
        const url = `${this.baseUrl}offer/update?calculated=${calculated}`;
        return this.http.post(url, body, options);
    }

    deleteOffer(offerId): Observable<any> {
        const url = `${this.baseUrl}offer/${offerId}/remove`;
        return this.http.post(url, null);
    }

    orderOffer(offerPositions: OfferPosition[]): Observable<any> {
        const url = `${this.baseUrl}offer/order`;
        return this.http.post(url, offerPositions);
    }

    createEndCustomer(body): Observable<any> {
        const url = `${this.baseUrl}offer/person/create`;
        return this.http.post(url, body);
    }

    getEndCustomerPerson(personId): Observable<OfferPerson> {
        const url = `${this.baseUrl}offer/person/${personId}`;
        return this.http.get(url).pipe(
            map((res: any) => new OfferPerson(res.offerPerson))
        );
    }

    updateEndCustomerPerson(person): Observable<any> {
        const url = `${this.baseUrl}offer/person/edit/${person.id}`;
        return this.http.put(url, person);
    }

    deleteEndCustomerPerson(personId): Observable<any> {
        const url = `${this.baseUrl}offer/person/remove/${personId}`;
        return this.http.delete(url);
    }

    getPersons(pageNumber = 0, request = {}): Observable<OfferPersonsResponse> {
        const url = `${this.baseUrl}offer/person/search?page=${pageNumber}`;
        return this.http.post(url, request).pipe(
            map((res: any) => new OfferPersonsResponse(res.offerPersons))
        );
    }

    getSalutations(): Observable<any> {
        const url = `${this.baseUrl}offer/person/salutations`;
        return this.http.get(url);
    }

    createOfferArticle(articleModel: OfferArticleModel): Observable<any> {
        return this.http.post(`${this.baseUrl}offer/shop/create`, articleModel);
    }

    getOfferArticles(page: number, size: number, searchCriteria: ArticleSearchCriteria): Observable<OfferArticlesResponse> {
        let url = `${this.baseUrl}offer/shop/search?page=${page}&size=${size}`;
        if (searchCriteria.sort) {
            url += `&sort=${searchCriteria.sort.sortBy}${Constant.COMMA}${searchCriteria.sort.direction}`;
        }
        return this.http.post(url, searchCriteria).pipe(
            map((res: any) => new OfferArticlesResponse(res.shopArticles))
        );
    }

    editOfferArticle(articleId: number, articleModel: OfferArticleModel): Observable<any> {
        return this.http.put(`${this.baseUrl}offer/shop/edit/${articleId}`, articleModel);
    }

    public deleteOfferArticle(articleId: number): Observable<any> {
        return this.http.delete(`${this.baseUrl}offer/shop/remove/${articleId}`);
    }

    exportPdfOfferById(offerId: number, offerNumber: string) {
        return this.exportOfferById(offerId, offerNumber, 'pdf');
    }

    exportRtfOfferById(offerId: number, offerNumber: string) {
        return this.exportOfferById(offerId, offerNumber, 'rtf');
    }

    exportOfferById(offerId: number, offerNumber: string, formatType: string, data = null) {
        const url = `${this.baseUrl}offer/${offerId}/export/${formatType}`;
        const fileName = this.buildOfferFileName(offerNumber, formatType);
        return this.executeDownload(url, fileName);
    }

    exportCurrentOffer(offer: OfferDetail, formatType: string) {
        if (!offer) {
            throw Error('The given offer must not be null');
        }
        const url = `${this.baseUrl}offer/current/export/${formatType}`;
        const fileName = this.buildOfferFileName(offer.offerNr, formatType);
        return this.executeDownload(url, fileName, JSON.stringify(offer));
    }

    executeDownload(url, fileName, data = null) {
        return this.http.post(url, data, {
            responseType: 'arraybuffer'
        }).subscribe((res) => {
            saveAs(new Blob([res]), fileName);
        });
    }

    private buildOfferFileName(offerNumber: string, formatType: string) {
        return `offer_${offerNumber}.${formatType === 'word' ? 'docx' : formatType}`;
    }

    removeBasketItemInOffer(offerPositions) {
        return offerPositions.filter(item =>
            item.type !== OFFER_ITEM_TYPE.VENDOR_ARTICLE.toString()
            && item.type !== OFFER_ITEM_TYPE.VENDOR_ARTICLE_WITHOUT_VEHICLE.toString()
        );
    }

    public async asyncGetOfferById(offerId) {
        const offer = await this.getOfferById(offerId, true).toPromise().catch(err => {
            console.log(err);
            throw err;
        });
        if (!offer) {
            return null;
        }
        return offer;
    }

    public async asyncUpdateOffer(selectedOffer, calculated) {
        // Calculate the offer
        const updatedOffer = await this.updateOffer(selectedOffer, calculated, true).toPromise()
            .catch(err => {
                console.log(err);
                throw err;
            });

        if (!updatedOffer) {
            return null;
        }
        return updatedOffer.offer;
    }

    public async asyncCreateNewOffer() {
        const offer = await this.createOffer(true).toPromise().catch(err => {
            console.log(err);
            throw err;
        });
        if (!offer || !offer.offer) {
            return null;
        }
        return offer.offer;
    }

    public addItemsToOffer(selectedOffer, currentBasket) {
        if (!selectedOffer) {
            return;
        }
        const offerPositions = Object.assign([], selectedOffer.offerPositions);
        currentBasket.items.forEach(item => {
            offerPositions.push(OfferPosition.convertFromBasketItem(item, this.translateService));
            if (item.attachedCartItems) {
                item.attachedCartItems.forEach(attachedCartItem => {
                    const position = OfferPosition.convertFromBasketItem(attachedCartItem, this.translateService);
                    position.pimId = attachedCartItem.articleItem.pimId;
                    offerPositions.push(position);
                });
            }
        });
        selectedOffer.offerPositions = offerPositions;
    }
}
