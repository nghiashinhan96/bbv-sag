import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'autonet-article-info',
  templateUrl: './article-info.component.html',
  styleUrls: ['./article-info.component.scss']
})
export class ArticleInfoComponent implements OnInit {
  @Input() keywords: string;
  @Input() numberOfSearchedArticles: number;
  @Output() refresEmiter = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  refresh() {
    this.refresEmiter.emit(true);
  }

}
