import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SearchEventTarget } from 'sag-article-list';
import { WspEventType } from 'src/app/analytic-logging/enums/event-type.enums';
import { Brand, GenArtTile } from '../../models/genart-tile.model';

@Component({
    selector: 'connect-card-genart-item',
    templateUrl: './card-genart-item.component.html',
    styleUrls: ['./card-genart-item.component.scss']
})
export class CardGenartItemComponent implements OnInit {

    @Input() tile: GenArtTile;
    @Input() index = 0;
    @Input() totalTiles = 0;
    @Output() onSelectedBrands = new EventEmitter<any>();

    MAX_DISPLAY_BRAND = 5;
    MAX_BRAND_PER_COLUMN = 20;
    COLUMN_WIDTH = 170;
    MINIUM_ROW_THRESHOLD = 9;
    POPUP_POS_THRESHOLD = 2;
    TILE_PER_ROW = 3;

    displayBrands: Brand[] = [];
    nonDisplayBrands: Brand[] = [];
    popupWidth = 0;

    TARGET = SearchEventTarget;

    constructor() { }

    ngOnInit() {
        this.handleBrandList();
    }

    handleBrandList() {
        const brands = (this.tile.brands || []).sort((brandA, brandB) => brandA.brandName > brandB.brandName ? 1 : -1);
        if (this.tile.brands.length <= this.MAX_DISPLAY_BRAND) {
            this.displayBrands = [...brands];
            this.nonDisplayBrands = [];
            return;
        }

        this.displayBrands = brands.slice(0, this.MAX_DISPLAY_BRAND);
        this.nonDisplayBrands = brands.slice(this.MAX_DISPLAY_BRAND);
        this.popupWidth = Math.ceil(this.nonDisplayBrands.length / this.MAX_BRAND_PER_COLUMN) * this.COLUMN_WIDTH - 20;
    }

    selectAll(target?: string) {
        const data = {
            gaId: this.tile.gaId,
            gaText: this.tile.gaText,
            brandIds: [],
            suppliers: [],
            event: {
                type: WspEventType.GENART,
                target
            }
        };
        this.onSelectedBrands.emit(data);
    }

    selectBrand(brand: Brand) {
        const data = {
            gaId: this.tile.gaId,
            gaText: this.tile.gaText,
            brandIds: [brand.brandId],
            suppliers: [brand.brandName],
            event: {
                type: WspEventType.GENART,
                target: SearchEventTarget.TILE_CONTENT
            }
        };
        this.onSelectedBrands.emit(data);
    }
}
