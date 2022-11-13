import { Component, Input } from '@angular/core';

@Component({
    selector: 'connect-notification',
    templateUrl: 'notification.component.html'
})
export class NotificationComponent {
    @Input() messages: string[] = [];
    @Input() type: 'ERROR' | 'WARNING' | 'SUCCESS' = 'WARNING';
}
