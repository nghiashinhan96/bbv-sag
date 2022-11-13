import { Component, OnInit, OnDestroy } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { FeedbackModalComponent } from './components/feedback-modal/feedback-modal.component';
import { UserService } from '../core/services/user.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { UserDetail } from '../core/models/user-detail.model';
import { FeedbackRecordingService } from './services/feedback-recording.service';
import { AppModalService } from '../core/services/app-modal.service';

@Component({
    selector: 'connect-feedback',
    templateUrl: 'feedback.component.html',
    styleUrls: ['feedback.component.scss']
})
export class FeedbackComponent implements OnInit, OnDestroy {
    private destroy$ = new Subject<boolean>();

    constructor(
        private bsModalService: BsModalService,
        private userService: UserService,
        private feedbackRecordingService: FeedbackRecordingService,
        private appModal: AppModalService
    ) { }

    ngOnInit() {
        this.userService.userDetail$
            .pipe(
                takeUntil(this.destroy$)
            )
            .subscribe((user: UserDetail) => {

            });
    }

    ngOnDestroy() {
        this.destroy$.next(true);
        this.destroy$.complete();
    }

    openModal() {
        this.appModal.modals =  this.bsModalService.show(FeedbackModalComponent, {
            ignoreBackdropClick: true
        });
    }
}
