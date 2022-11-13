import { Component, OnInit, Input } from '@angular/core';
export interface MessengerData {
  message: string;
  type: 'ERROR' | 'WARNING' | 'SUCCESS';
  params?: any;
}
@Component({
  selector: 'backoffice-messenger',
  templateUrl: './messenger.component.html',
  styleUrls: ['./messenger.component.scss']
})
export class MessengerComponent implements OnInit {

  @Input() data: MessengerData;

  constructor() { }

  ngOnInit() {
  }

}
