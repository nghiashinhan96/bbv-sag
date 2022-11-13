import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, Subject } from 'rxjs';
import { SagVehicleListConfigService } from './vehicle-list-config.service';
import { KEY_RAW } from '../enums/vehicle-list.enum';
import { TranslateService } from '@ngx-translate/core';


const DATE_FORMAT_SEPARATOR = ['-', '/'];
const DEFAULT_DATE_SEPARATOR = '/';

@Injectable()
export class VehicleListService {
  scrollerSubject = new Subject<any>();
  scrollderObservable = this.scrollerSubject.asObservable();

  constructor(
    private http: HttpClient,
    private config: SagVehicleListConfigService,
    private translateService: TranslateService
  ) { }

  searchVehicles(queryData, params?): Observable<any> {
    const url = `${this.config.baseUrl}search/vehicles/`;
    return this.http.post(url, queryData, { params, observe: 'body' });
  }

  searchVehiclesAdvance(queryData, params?): Observable<any> {
    const url = `${this.config.baseUrl}search/vehicles/advance`;
    return this.http.post(url, queryData, { params, observe: 'body' });
  }

  buildFiltering(filterDropdowns, columns) {
    if (!filterDropdowns) {
      return;
    }

    Object.keys(filterDropdowns).forEach(key => {
      if (filterDropdowns.hasOwnProperty(key)) {
        switch (key) {
          case KEY_RAW.vehicle_built_year_from:
            if (filterDropdowns[KEY_RAW.vehicle_built_year_from]) {
              const yearFilterData = filterDropdowns[KEY_RAW.vehicle_built_year_from].map(year => this.formatDateWithMonthAndYear(year));
              const colYear = columns.find(item => item.keyRaw === KEY_RAW.vehicle_built_year_from);
              if (colYear) {
                colYear.options = this.loadVehicleFilterDropdown(yearFilterData);
              }
            }
            break;
          default:
            const col = columns.find(item => item.keyRaw === KEY_RAW[key]);
            if (col) {
              col.options = this.loadVehicleFilterDropdown(filterDropdowns[key]);
            }
            break;
        }
      }
    });
  }

  formatDateWithMonthAndYear(date, dateSeparator = '/') {
    if (!this.isValidObj(date)) {
      return '';
    }
    const year = date.toString().substring(0, 4);
    const month = date.toString().substring(4, date.length);
    return `${('0' + month).slice(-2)}${dateSeparator}${year}`;
  }

  getDateSeparator(dateValStr) {
    return DATE_FORMAT_SEPARATOR.find(separator => dateValStr.indexOf(separator) !== -1) || DEFAULT_DATE_SEPARATOR;
}

  private loadVehicleFilterDropdown(filterData) {
    const optionArr = [{ value: '', label: this.translateService.instant('TYRE.ALL') }];
    const filtered = filterData.map(f => {
      return { value: f, label: f };
    });
    if (filtered) {
      filtered.sort((a, b) => a.label.toString().localeCompare(b.label.toString()));
    }
    return [...optionArr, ...filtered];
  }

  private isValidObj(obj) {
    if (!obj || obj === '0') {
        return false;
    }
    return true;
}
}
