import { Component, OnInit, Input, TemplateRef, EventEmitter, Output } from '@angular/core';
import { PopoverDirective } from 'ngx-bootstrap/popover';

@Component({
  selector: 'sag-custom-favorite-list',
  templateUrl: './custom-favorite-list.component.html',
  styleUrls: ['./custom-favorite-list.component.scss']
})
export class SagCustomFavoriteListComponent implements OnInit {
  @Input() customPopover: PopoverDirective;
  @Input() template: TemplateRef<any>;
  @Input() includeLeafNode = true;

  @Output() onSelectItemEmit = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  viewFavorite(event) {
    this.onSelectItemEmit.emit(event);
  }
}
