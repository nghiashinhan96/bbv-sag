import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { ArticleListConfigService } from './article-list-config.service';

@Injectable()
export class VehicleSearchService {

    constructor(
        private config: ArticleListConfigService,
        private http: HttpClient) { }

    getVehiclesByVehId(vehId) {
        const url = `${this.config.baseUrl}search/vehicles/type/Id/${vehId}`;
        return this.http.get(url, { observe: 'body' }).pipe(map((res: any) => {
            return res;
        }));
    }
}
