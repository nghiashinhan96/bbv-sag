import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'connect-tabello-info',
  templateUrl: './tabello-info.component.html',
  styleUrls: ['./tabello-info.component.scss']
})
export class TabelloInfoComponent implements OnInit {

  @Input() links;  

  constructor() { }

  ngOnInit() {
  }

}
