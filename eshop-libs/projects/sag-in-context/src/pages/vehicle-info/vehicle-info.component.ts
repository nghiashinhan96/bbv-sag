import { Component, OnInit, Input, Output, EventEmitter, OnDestroy, OnChanges, SimpleChanges } from '@angular/core';
import { SagInContextVehicleInfoDialogComponent } from '../vehicle-info-dialog/vehicle-info-dialog.component';
import { BsModalService } from 'ngx-bootstrap/modal';
import { FAVORITE_BROADCAST_EVENT } from 'sag-article-detail';
import { SubSink } from 'subsink';
import { ActivatedRoute } from '@angular/router';
import { Subject } from 'rxjs';

@Component({
    selector: 'sag-in-context-vehicle-info',
    templateUrl: './vehicle-info.component.html',
    styleUrls: ['./vehicle-info.component.scss']
})
export class SagInContextVehicleInfoComponent implements OnInit, OnDestroy, OnChanges {
    @Input() keywords: string;
    @Input() vehicle: any;
    @Output() refresEmiter = new EventEmitter();

    actionFavorite = FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_VEHICLE;
    breadcrumbs = [];

    private subs = new SubSink();
    private vehicleSubject = new Subject<any>();

    constructor(private modalService: BsModalService, private route: ActivatedRoute,) { }

    ngOnInit() {
        this.subs.sink = this.route.queryParams.subscribe(({ keyword, breadcrumbs }) => {
            if (!breadcrumbs) {
                return;
            }

            try {
                this.breadcrumbs = JSON.parse(breadcrumbs);
                this.subs.sink = this.vehicleSubject.subscribe(vehicle => {
                    if (vehicle) {
                        this.breadcrumbs.push({ navigateTo: '', text: vehicle.vehTypeDesc });
                    }
                });
            } catch (ex) {
                // wrong params
            }
        });
    }

    ngOnDestroy(): void {
        if (this.subs) {
            this.subs.unsubscribe();
        }
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (changes['vehicle'] && changes['vehicle'].currentValue &&  !changes['vehicle'].firstChange) {
            this.vehicleSubject.next(changes['vehicle'].currentValue);
        }
    }

    refresh() {
        this.refresEmiter.emit(true);
    }

    showInfo() {
        this.modalService.show(SagInContextVehicleInfoDialogComponent, {
            ignoreBackdropClick: true,
            initialState: {
                vehicle: this.vehicle
            }
        });
    }
}
