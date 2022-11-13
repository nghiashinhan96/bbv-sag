import { Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CategoryExternalTile, CategoryTile } from 'sag-article-detail';
import { GenArtTile } from '../../models/genart-tile.model';
import { TILE_MODE } from '../../services/constants';

const DEFAULT_ZONE_ID_START = 16;
@Component({
  selector: 'connect-tile-list',
  templateUrl: './tile-list.component.html',
  styleUrls: ['./tile-list.component.scss']
})
export class TileListComponent implements OnInit, OnChanges {
  @Input() items: CategoryTile[] = [];
  @Input() nodeId: string;
  @Input() treeId: string;
  @Input() tileMode = TILE_MODE.NODE_MODE;
  @Output() selectLinkEmit = new EventEmitter<CategoryExternalTile>();

  @Output() selectBrandsEmit = new EventEmitter<any>();
  @Input() genArts: GenArtTile[];

  TILE_MODE = TILE_MODE;
  finalList = [];

  constructor() { }

  ngOnInit() {
    this.finalList = this.items;
    this.addAdvert();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.items && changes.items.currentValue && !changes.items.firstChange) {
      this.finalList = changes.items.currentValue;
      this.addAdvert();
    }
  }

  selectLink(node: CategoryExternalTile) {
    this.selectLinkEmit.emit(node);
  }

  private addAdvert() {
    let pos = 0;

    if(!this.items) {
      return;
    }

    if (this.items.length >= 3) {
      pos = this.items.length % 3;
    } else {
      pos = this.items.length;
    }

    if (pos > 0) {
      for (let i = 0; i < (3 - pos); i++) {
        this.finalList = this.finalList.concat([
          <any>{
            tileType: 'up_ad',
            zoneId: DEFAULT_ZONE_ID_START + i
          }
        ])
      }
    }
  }
}
