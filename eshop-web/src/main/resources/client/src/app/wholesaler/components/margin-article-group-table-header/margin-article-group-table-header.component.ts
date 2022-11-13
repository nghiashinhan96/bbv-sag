import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { MarginArticleGroupSearchRequestModel } from '../../models/margin-article-group-search-request.model';

@Component({
  selector: 'connect-margin-article-group-table-header',
  templateUrl: './margin-article-group-table-header.component.html',
  styleUrls: ['./margin-article-group-table-header.component.scss']
})
export class MarginArticleGroupTableHeaderComponent implements OnInit {
  @Input() margins: number[];
  @Input() searchModel: MarginArticleGroupSearchRequestModel;

  @Output() onTextInputChangeEmit = new EventEmitter<MarginArticleGroupSearchRequestModel>();

  constructor () { }

  ngOnInit() {
  }

  onTextInputChange(event) {
    this.onTextInputChangeEmit.emit(event);
  }
}
