import { Component, Input, OnInit, OnChanges, SimpleChanges } from '@angular/core';

@Component({
  selector: 'connect-card-ads-item',
  templateUrl: './card-ads-item.component.html',
  styleUrls: ['./card-ads-item.component.scss']
})
export class CardAdsItemComponent implements OnInit, OnChanges {
  @Input() item: any;
  @Input() nodeId: string;
  @Input() treeId: string;

  custom4 = '';
  custom1 = '';

  constructor() { }

  ngOnInit() {
    this.custom1 = `UNIPART-${this.treeId}`;
    this.custom4 = `UNIPART-${this.nodeId}`;
  }

  ngOnChanges(change: SimpleChanges) {
    if(change.nodeId && change.nodeId.currentValue && !change.nodeId.firstChange) {
      this.nodeId = change.nodeId.currentValue;
      this.custom4 = `UNIPART-${this.nodeId}`;
    }

    if(change.treeId && change.treeId.currentValue && !change.treeId.firstChange) {
      this.treeId = change.treeId.currentValue;
      this.custom1 = `UNIPART-${this.treeId}`;
    }
  }

}
