import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'sag-article-pcc-number',
  templateUrl: './article-pcc-number.component.html',
  styleUrls: ['./article-pcc-number.component.scss']
})
export class SagArticlePccNumberComponent implements OnInit {
  @Input() pnrnPccs: string[] = [];
  @Input() label: string = 'ARTICLE.PRODUCT_COMMERCIAL_CODE';
  @Input() customClass: string = '';
  @Input() showLabel: boolean = true;

  pnrnPccsDisplay: string = '';

  constructor () { }

  ngOnInit() {
    this.pnrnPccsDisplay = this.getPnrnPccsDisplay();
  }

  getPnrnPccsDisplay() {
    return this.pnrnPccs && this.pnrnPccs.length > 0 ? this.pnrnPccs.join(', ') : '';
  }

}
