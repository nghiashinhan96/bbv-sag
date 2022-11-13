import { Component, OnInit, OnDestroy, Input, EventEmitter, Output } from '@angular/core';
import { SubSink } from 'subsink';
import { MarginService } from '../../services/margin.service';
import { MarginArticleGroupModel } from '../../models/margin-article-group.model';
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app-lang-code.constant';
@Component({
  selector: 'connect-margin-article-group-default',
  templateUrl: './margin-article-group-default.component.html',
  styleUrls: ['./margin-article-group-default.component.scss']
})
export class MarginArticleGroupDefaultComponent implements OnInit, OnDestroy {
  @Input() margins: number[] = [];
  @Input() currentLangCode: string = DEFAULT_LANG_CODE;

  @Output() editArtGroupEmit = new EventEmitter<any>();

  articleGroupDefault: MarginArticleGroupModel;
  private subs = new SubSink();

  constructor (
    private marginService: MarginService
  ) { }

  ngOnInit() {
    this.getArticleGroupDefault();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  getArticleGroupDefault() {
    this.subs.sink = this.marginService.getDefaultArticleGroup()
      .subscribe(res => {
        this.articleGroupDefault = res || null;
      });
  }

  editItem() {
    this.editArtGroupEmit.emit({
      artGroup: this.articleGroupDefault,
      callback: (event) => {
        this.getArticleGroupDefault();
      }
    });
  }
}
