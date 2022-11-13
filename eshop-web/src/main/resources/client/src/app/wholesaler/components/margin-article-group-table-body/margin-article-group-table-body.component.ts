import { Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges, OnDestroy } from '@angular/core';
import { MarginArticleGroupModel } from '../../models/margin-article-group.model';
import { TablePage } from 'sag-table';
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app-lang-code.constant';
import { SubSink } from 'subsink';
import { MarginService } from '../../services/margin.service';

@Component({
  selector: 'connect-margin-article-group-table-body',
  templateUrl: './margin-article-group-table-body.component.html',
  styleUrls: ['./margin-article-group-table-body.component.scss']
})
export class MarginArticleGroupTableBodyComponent implements OnInit, OnChanges, OnDestroy {
  @Input() currentLangCode: string = DEFAULT_LANG_CODE;
  @Input() margins: number[] = [];
  @Input() page: TablePage;
  @Input() isViewTree: false;
  @Input() numberOfElements: number;

  @Output() pageChangedEmit = new EventEmitter<any>();
  @Output() editEmit = new EventEmitter<any>();
  @Output() deleteEmit = new EventEmitter<any>();
  @Output() expandEmit = new EventEmitter<any>();
  @Output() searchTreeRootEmit = new EventEmitter<any>();

  private subs = new SubSink();
  articleGroups: MarginArticleGroupModel[] = [];

  constructor (
    private marginService: MarginService
  ) { }

  ngOnInit() {
    this.subs.sink = this.marginService.articleGroups$
      .subscribe(data => {
        this.articleGroups = data;
      });
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.isViewTree && !changes.isViewTree.firstChange) {
      this.isViewTree = changes.isViewTree.currentValue;
    }

    if(changes.numberOfElements && !changes.numberOfElements) {
      this.numberOfElements = changes.numberOfElements.currentValue;
    }
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  pageChanged(event) {
    this.pageChangedEmit.emit(event);
  }

  editArtGroup(event) {
    this.editEmit.emit(event);
  }

  deleteArtGroup(event) {
    this.deleteEmit.emit(event);
  }

  expandArtGroup(event) {
    this.expandEmit.emit(event);
  }

  searchTreeRoot(event) {
    this.searchTreeRootEmit.emit(event);
  }
}
