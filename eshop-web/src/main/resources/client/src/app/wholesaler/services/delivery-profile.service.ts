import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { of } from 'rxjs';
import { environment } from 'src/environments/environment';
import { DeliveryProfileModel, DeliveryProfileRequestModel } from '../models/delivery-profile.model';
import { TourAssignModel } from '../models/tour-assign.model';
import { ADD_DP_TOUR, CREATE_DP, GET_DP, SEARCH_DP, UPDATE_DP, UPDATE_DP_TOUR, REMOVE_DP, REMOVE_DP_TOUR } from './constant';
@Injectable()
export class DeliveryProfileService {
    private readonly BASE_URL = environment.baseUrl;

    constructor(
        private http: HttpClient
    ) {
    }

    getDeliverProfileList(criteria: DeliveryProfileRequestModel, pagenation?) {
        let url = pagenation ? `${SEARCH_DP}?page=${pagenation.currentPage}&size=${pagenation.itemsPerPage}` : SEARCH_DP;
        url = `${this.BASE_URL}${url}`;
        return this.http.post(url, criteria);
    }

    createDeliveryProfile(profile) {
        let url = `${this.BASE_URL}${CREATE_DP}`;
        return this.http.post(url, profile);
    }

    updateDeliveryProfile(profile) {
        let url = `${this.BASE_URL}${UPDATE_DP}`;
        return this.http.put(url, profile);
    }

    deleteDeliveryProfile(profileId) {
        let url = `${this.BASE_URL}${REMOVE_DP(profileId)}`;
        return this.http.delete(url);
    }

    addTour(tour) {
        let url = `${this.BASE_URL}${ADD_DP_TOUR}`;
        return this.http.post(url, tour)
    }

    updateTour(tour) {
        let url = `${this.BASE_URL}${UPDATE_DP_TOUR}`;
        return this.http.put(url, tour)
    }

    deleteTour(tourId) {
        let url = `${this.BASE_URL}${REMOVE_DP_TOUR(tourId)}`;
        return this.http.delete(url); 
    }

    getDeliveryProfile(id) {
        let url = `${this.BASE_URL}${GET_DP}/${id}`;
        return this.http.get(url);
    }
}
