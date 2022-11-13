import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';
import { BuildParamsUtil, SagMessageData } from 'sag-common';
import * as moment from 'moment';
import { uuid } from 'uuidv4';
import { of } from 'rxjs';

import { environment } from 'src/environments/environment';
import { BrandModel } from '../models/margin-brand.model';
import { MarginBrandRequestModel } from '../models/margin-brand-search-request.model';
import { MarginArticleGroupModel } from '../models/margin-article-group.model';
import { MarginArticleGroupSearchRequestModel } from '../models/margin-article-group-search-request.model';

import { MarginSupplierArticleGroupSearchRequestModel } from '../models/margin-supplier-article-group-search-request.model';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { X_SAG_REQUEST_ID_HEADER_NAME } from 'src/app/core/conts/app.constant';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { TranslateService } from '@ngx-translate/core';
import { Subject } from 'rxjs/internal/Subject';

@Injectable({
  providedIn: 'root'
})
export class MarginService {
  private baseUrl = environment.baseUrl;
  private brandPrefix = `${this.baseUrl}wss/margin-by-brand`;
  private articleGroupPrefix = `${this.baseUrl}wss/margin-by-article-group`;

  articleGroupsSubject = new BehaviorSubject<MarginArticleGroupModel[]>(null);

  addSubject = new Subject<any>();
  editSubject = new Subject<any>();
  deleteSubject = new Subject<any>();

  constructor (
    private http: HttpClient,
    private appStorage: AppStorageService,
    private translateService: TranslateService
  ) { }

  getBrands(body: MarginBrandRequestModel, pageable?) {
    const url = `${this.brandPrefix}/search`;
    const suffixUrl = pageable ? BuildParamsUtil.buildParams(pageable) : '';

    return this.http.post(`${url + suffixUrl}`, body);
  }

  getDefaultBrand() {
    return this.http.get(`${this.brandPrefix}/default`).pipe(
      map(data => new BrandModel(data))
    );
  }

  searchBrand(params) {
    const suffix = BuildParamsUtil.buildParams(params);

    return this.http.get(`${this.brandPrefix}/brand/search${suffix}`);
  }

  createBrand(brand: BrandModel) {
    return this.http.post(`${this.brandPrefix}/create`, brand).pipe(
      map(data => new BrandModel(data) || null)
    );
  }

  editBrand(brand: BrandModel) {
    return this.http.put(`${this.brandPrefix}/update`, brand).pipe(
      map(data => new BrandModel(data) || null)
    );
  }

  deleteBrand(id: number) {
    return this.http.delete(`${this.brandPrefix}/delete/${id}`);
  }

  getDefaultArticleGroup() {
    return this.http.get(`${this.articleGroupPrefix}/get-default`).pipe(
      map(data => new MarginArticleGroupModel(data))
    );
  }

  searchArticleGroup(body: MarginArticleGroupSearchRequestModel) {
    const request = new MarginArticleGroupSearchRequestModel(body);

    const param = BuildParamsUtil.buildParams({
      page: request.page,
      size: request.size
    });

    return this.http.post(`${this.articleGroupPrefix}/search${param}`, request.getRequestDto())
      .pipe(
        catchError(err => of(null))
      );
  }

  searchArticleGroupByRoot(params) {
    const param = BuildParamsUtil.buildParams(params);
    return this.http.get(`${this.articleGroupPrefix}/search-root${param}`)
      .pipe(
        catchError(err => of(null))
      );
  }

  editArticleGroup(artGroup: MarginArticleGroupModel) {
    const body = new MarginArticleGroupModel(artGroup);

    return this.http.put(`${this.articleGroupPrefix}/update`, body.getUpdateDto()).pipe(
      map(data => new MarginArticleGroupModel(data) || null)
    );
  }

  deleteArticleGroup(id: number) {
    return this.http.delete(`${this.articleGroupPrefix}/delete/${id}`);
  }

  searchSupplierArtGroups(data: MarginSupplierArticleGroupSearchRequestModel) {
    const body = new MarginSupplierArticleGroupSearchRequestModel(data);

    const param = BuildParamsUtil.buildParams(body.searchParams());

    return this.http.post(`${this.articleGroupPrefix}/search-index${param}`, body.searchBody())
      .pipe(
        catchError(err => of(null))
      );
  }

  createArtGroup(artGrp: MarginArticleGroupModel) {
    const body = new MarginArticleGroupModel(artGrp);

    return this.http.post(`${this.articleGroupPrefix}/create`, body.getCreateDto()).pipe(
      map(data => new MarginArticleGroupModel(data) || null)
    );
  }

  deleteArtGroup(id: number) {
    return this.http.delete(`${this.articleGroupPrefix}/delete/${id}`);
  }

  searchChildById(id: number) {
    return this.http.get(`${this.articleGroupPrefix}/search-child/${id}`)
      .pipe(
        catchError(err => of(null))
      );
  }

  get articleGroups$() {
    return this.articleGroupsSubject.asObservable();
  }


  importBrandData(file: File) {
    let url = `${this.brandPrefix}/import`;
    return this.handleImport(url, file);
  }

  importArticleGroupData(file: File) {
    let url = `${this.articleGroupPrefix}/import`;
    return this.handleImport(url, file);
  }

  exportBrandData(criteria) {
    let url = `${this.brandPrefix}/export-csv`;
    const prefixMargin = this.translateService.instant('MARGIN_MANAGE.MARGINS');
    const prefix = this.translateService.instant('MARGIN_MANAGE.BRANDS');
    const fileName = this.getExportName(`${prefixMargin}_${prefix}_`);
    return this.handleExportData(fileName, url, criteria);
  }

  exportArticleGroupData(path, filename) {
    let url = `${this.articleGroupPrefix}/${path}?languageCode=${this.appStorage.appLangCode || 'de'}`;
    const fileName = this.getExportName(`${filename}_`);
    return this.handleExportCSV(fileName, url);
  }

  exportBrandCsvTemplate() {
    let url = `${this.brandPrefix}/csv-template`;
    const fileName = this.getExportName()
    return this.handleExportCSV(fileName, url);
  }

  getImportOptions() {
    const appToken = this.appStorage.appToken;
    const appLangCode = this.appStorage.appLangCode;
    const headers: any = {
      Accept: 'application/json',
      'X-Client-Version': this.appStorage.appVersion,
      'Access-Control-Max-Age': '3600',
      'Accept-Language': appLangCode || 'de'
    };
    if (appToken) {
      Object.assign(headers, {
        Authorization: `Bearer ${appToken}`,
        [X_SAG_REQUEST_ID_HEADER_NAME]: uuid()
      });
    }
    return { headers };
  }

  getExportName(prefix = 'export_') {
    return `${prefix}${moment().format('YYYYMMDD_HHmm')}.csv`;
  }

  unMapArtGroup(id: number) {
    const param = BuildParamsUtil.buildParams({
      marginArticleGroupId: id
    });

    return this.http.put(`${this.articleGroupPrefix}/unmap/${param}`, {});
  }

  private handleExportCSV(fileName: string, url: string) {
    return this.http.get(url, {
      responseType: 'arraybuffer'
    }).pipe(map(res => {
      saveAs(new Blob([res]), fileName);
      return null;
    }), catchError(err => {
      return of({
        type: 'ERROR',
        message: 'MESSAGES.GENERAL_ERROR'
      } as SagMessageData);
    }));;
  }

  private handleExportData(fileName: string, url: string, criteria?) {
    return this.http.post(url, criteria, {
      responseType: 'arraybuffer'
    }).pipe(map(res => {
      saveAs(new Blob([res]), fileName);
      return null;
    }), catchError(err => {
      return of({
        type: 'ERROR',
        message: 'MESSAGES.GENERAL_ERROR'
      } as SagMessageData);
    }));;
  }

  private handleImport(url: string, file) {
    const data = new FormData();
    data.append('file', file, file.name);

    const options = this.getImportOptions();
    return this.http.post(url, data, options);
  }
}
