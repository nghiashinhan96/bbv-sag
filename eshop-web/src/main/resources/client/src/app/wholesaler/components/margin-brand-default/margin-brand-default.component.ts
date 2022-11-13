import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';

import { MarginService } from '../../services/margin.service';
import { BrandModel } from '../../models/margin-brand.model';
import { FormBuilder } from '@angular/forms';
import { SubSink } from 'subsink';
import { finalize } from 'rxjs/operators';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { MARGIN_VALUES } from '../../enums/margin-mgt.enum';

@Component({
  selector: 'connect-margin-brand-default',
  templateUrl: './margin-brand-default.component.html',
  styleUrls: ['./margin-brand-default.component.scss']
})
export class MarginBrandDefaultComponent implements OnInit, OnDestroy {
  brand: BrandModel;
  margins = MARGIN_VALUES;

  @Output() editEmit = new EventEmitter<any>();

  private subs = new SubSink();

  constructor (
    private marginService: MarginService,
    private fb: FormBuilder
  ) { }

  ngOnInit() {
    this.getDefaultBrand();
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  getDefaultBrand() {
    this.subs.sink = this.marginService.getDefaultBrand()
      .pipe(
        finalize(() => {
          SpinnerService.stop();
        })
      )
      .subscribe(brand => {
        this.brand = brand || null;
      });
  }

  edit() {
    this.editEmit.emit({
      brand: this.brand,
      callback: (event) => {
        this.getDefaultBrand();
      }
    });
  }
}
