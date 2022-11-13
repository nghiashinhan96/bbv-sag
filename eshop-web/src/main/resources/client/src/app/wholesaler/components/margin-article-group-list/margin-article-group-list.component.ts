import { Component, OnInit, OnDestroy, Input, Output, EventEmitter } from '@angular/core';
import { TablePage } from 'sag-table';
import { MarginArticleGroupModel } from '../../models/margin-article-group.model';
import { SubSink } from 'subsink';
import { MarginService } from '../../services/margin.service';

@Component({
  selector: 'connect-margin-article-group-list',
  templateUrl: './margin-article-group-list.component.html',
  styleUrls: ['./margin-article-group-list.component.scss']
})
export class MarginArticleGroupListComponent implements OnInit, OnDestroy {
  @Input() margins: number[] = [];
  @Input() page: TablePage;

  @Output() editEmit = new EventEmitter<any>();
  @Output() deleteEmit = new EventEmitter<any>();

  articleGroups: MarginArticleGroupModel[] = [];

  subs = new SubSink();

  constructor (
    private marginService: MarginService
  ) { }

  ngOnInit() {
    this.subs.sink = this.marginService.articleGroups$.subscribe(data => this.articleGroups = data || []);
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }


  editArtGroup(item) {
    this.editEmit.emit(item);
  }

  deleteArtGroup(item) {
    this.deleteEmit.emit(item);
  }
}
