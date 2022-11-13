import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { CategoryTile, CategoryExternalTile } from 'sag-article-detail';
import { SearchEventTarget } from 'sag-article-list';
import { WspEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { IWspLink } from '../../models/wsp-tree.model';

@Component({
  selector: 'connect-card-tile-item',
  templateUrl: './card-tile-item.component.html',
  styleUrls: ['./card-tile-item.component.scss']
})
export class CardTileItemComponent implements OnInit {
  @Input() tile: CategoryTile;

  @Output() selectLinkEmit = new EventEmitter<IWspLink>();

  TARGET = SearchEventTarget;

  constructor () { }

  ngOnInit() {
  }

  selectLink(externalTile: CategoryExternalTile) {
    this.selectLinkEmit.emit(<IWspLink>{
      type: externalTile.tileLinkType,
      attr: externalTile.tileLinkAttr,
      link: externalTile.tileLink,
      event: {
        type: WspEventType.LINK,
        target: SearchEventTarget.TILE_CONTENT
      }
    });
  }

  selectLeaf(target?: string) {
    this.selectLinkEmit.emit(<IWspLink>{
      type: this.tile.tileType,
      attr: '',
      link: this.tile.tileNodeId,
      event: {
        type: WspEventType.LEAF_NODE,
        target
      }
    });
  }
}
