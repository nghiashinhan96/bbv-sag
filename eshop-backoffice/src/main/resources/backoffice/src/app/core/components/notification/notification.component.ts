import { NotificationModel } from '../../../shared/models/notification.model';
import { Component, Input } from '@angular/core';

@Component({
    selector: "notification",
    templateUrl: 'notification.component.html',
})
export class NotificationComponent {
    @Input() notifier: NotificationModel;
}
