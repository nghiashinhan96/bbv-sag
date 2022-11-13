import { Injectable } from '@angular/core';
import { SpinnerService } from '../utils/spinner';
import { environment } from 'src/environments/environment';
import { ProjectId } from 'sag-common';

@Injectable({
  providedIn: 'root'
})
export class ArticleSearchConfigIntegrateService {
  baseUrl: string;
  spinner: any;
  affiliate: string;
  projectId: ProjectId;
  constructor() {
    this.spinner = SpinnerService;
    this.baseUrl = environment.baseUrl;
    this.affiliate = environment.affiliate;
    this.projectId = ProjectId.AUTONET;
  }
}
