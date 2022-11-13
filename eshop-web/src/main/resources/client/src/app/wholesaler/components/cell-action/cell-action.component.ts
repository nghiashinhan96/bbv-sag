import { MarginService } from './../../services/margin.service';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'connect-cell-action',
  templateUrl: './cell-action.component.html',
  styleUrls: ['./cell-action.component.scss']
})
export class CellActionComponent implements OnInit {
  @Input() row_data: any;

  constructor (
    private marginService: MarginService
  ) { }

  ngOnInit() {}

  editItem() {
    this.marginService.editSubject.next(this.row_data);
  }

  deleteItem() {
    this.marginService.deleteSubject.next(this.row_data);
  }

}
