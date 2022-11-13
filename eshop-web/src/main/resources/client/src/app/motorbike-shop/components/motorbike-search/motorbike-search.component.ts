import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SEARCH_MODE } from 'sag-article-list';
import { LIB_VEHICLE_MOTO_SEARCH_MAKE_MODEL, LIB_VEHICLE_SEARCH_FREETEXT } from 'sag-article-search';
import { AnalyticEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { AnalyticLoggingService } from 'src/app/analytic-logging/services/analytic-logging.service';
import { FeedbackVehicleSearch } from 'src/app/feedback/models/feedback-vehicle-search.model';
import { FeedbackRecordingService } from 'src/app/feedback/services/feedback-recording.service';
import { get } from 'lodash';
import { first } from 'rxjs/operators';
import { UserDetail } from 'src/app/core/models/user-detail.model';
import { UserService } from 'src/app/core/services/user.service';

@Component({
    selector: 'connect-motorbike-search',
    templateUrl: './motorbike-search.component.html',
    styleUrls: ['./motorbike-search.component.scss']
})
export class MotorbikeSearchComponent implements OnInit {
    user: UserDetail;

    constructor(
        private router: Router,
        private translateService: TranslateService,
        private activatedRoute: ActivatedRoute,
        private fbRecordingService: FeedbackRecordingService,
        private analyticService: AnalyticLoggingService,
        private userService: UserService
    ) { }

    ngOnInit() {
        this.user = this.userService.userDetail;
    }

    searchVehicle(data) {
        const searchType = data.searchType;
        const search = data.search || {};
        let feedbackModel;
        this.sendVehicleSearchDataAnalytic(data);
        switch (searchType) {
            case LIB_VEHICLE_SEARCH_FREETEXT:
                feedbackModel = new FeedbackVehicleSearch({
                    type: this.translateService.instant('SEARCH.VEHICLES'),
                    searchText: data.info
                });

                this.router.navigate(['../vehicle', search], {
                    queryParams: {
                        keywords: data.info,
                        searchMode: SEARCH_MODE.VEHICLE_CODE
                    },
                    relativeTo: this.activatedRoute
                });
                break;
            case LIB_VEHICLE_MOTO_SEARCH_MAKE_MODEL:
                this.router.navigate(['../vehicle', data.body.yearFrom], {
                    queryParams: {
                        keywords: data.searchKey,
                        searchMode: SEARCH_MODE.MAKE_MODEL_TYPE
                    },
                    relativeTo: this.activatedRoute
                });
                break;
        }

        if (feedbackModel) {
            this.fbRecordingService.recordVehicleSearch(feedbackModel);
        }
    }

    private sendVehicleSearchDataAnalytic(data: any) {
        setTimeout(() => {
            const eventType = AnalyticEventType.VEHICLE_SEARCH_EVENT;
            let request: any;
            switch (data.searchType) {
                case LIB_VEHICLE_SEARCH_FREETEXT:
                    request = this.analyticService.createVehicleSearchEventData(
                        {
                            vehSearchTermEnteredTsKzNc: get(data, 'analytic.searchTerm'),
                            vehSearchNumberOfHits: get(data, 'analytic.numberOfHits')
                        }
                    );
                    break;
                case LIB_VEHICLE_MOTO_SEARCH_MAKE_MODEL:
                    let values = [];
                    try {
                        values = JSON.parse(data.searchKey || '[]') || [];
                    } catch { }
                    request = this.analyticService.createMotoMakeModelSearchEventData(
                        {
                            vehSearchTypeSelected: 'veh_moto_make_model',
                            vehBrandSelected: values[0],
                            vehModelSelected: values[2],
                            vehVehicleIdResult: get(data, 'body.yearFrom'),
                            vehVehicleNameResult: values.length > 0 ? values.join(' ') : data.searchKey
                        }
                    );
                default:
                    break;
            }
            this.analyticService.postEventFulltextSearch(request, eventType)
                .pipe(first())
                .toPromise();
        });
    }
}
