import { Component, Input, OnInit, Output, EventEmitter, OnChanges, SimpleChanges, AfterViewInit } from '@angular/core';
import { WspTreeModel } from '../../models/wsp-tree.model';
import { WSP_LINK_TYPES, WSP_SHOP_LINK } from 'sag-article-list';
import { UserDetailOwnSetting } from 'src/app/core/models/user-detail-own-setting.mode';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'connect-uni-tree',
  templateUrl: './uni-tree.component.html',
  styleUrls: ['./uni-tree.component.scss']
})
export class UniTreeComponent implements OnInit, OnChanges, AfterViewInit {
  trees: WspTreeModel[] = [];
  disabledLeftIcon = true;
  disabledRightIcon = false;

  @Input() userDetailSetting: UserDetailOwnSetting;
  @Input() treeId: string;
  @Input() set uniTrees(val: WspTreeModel[]) {
    if(val !== this.trees) {
      this.trees = val;
    }
  }

  @Output() treeIdChange = new EventEmitter<WspTreeModel>();
  @Output() uniTreesChange = new EventEmitter<WspTreeModel[]>();

  get uniTrees() {
    return this.trees;
  }

  constructor(
    private router: Router,
    private activeRouter: ActivatedRoute
  ) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    const interval = setInterval(() => {
      if (!this.trees || this.trees.length === 0) {
        return;
      }
      const treeEl = document.getElementById(`tree-${this.trees[0].treeId}`);
      if (!treeEl) {
        return;
      }
      clearInterval(interval);
      this.initUniTreeWidth();
    }, 100);
  }

  ngOnChanges(changes: SimpleChanges) {
    if(changes.treeId && changes.treeId.currentValue) {
      this.treeId = changes.treeId.currentValue;
    }
  }

  activeTab(tree: WspTreeModel) {
    switch (tree.treeExternalServiceType) {
      case WSP_LINK_TYPES.service:
        if (tree.treeExternalServiceAttribute === 'thule') {
          const link = this.userDetailSetting && this.userDetailSetting.externalUrls && this.userDetailSetting.externalUrls.thule || '';
          window.open(link, '_self');
        }

        break;
      case WSP_LINK_TYPES.shop:
        if (WSP_SHOP_LINK[tree.treeExternalServiceAttribute]) {
          this.router.navigate([WSP_SHOP_LINK[tree.treeExternalServiceAttribute]]);
        }

        break;
      case WSP_LINK_TYPES.link:
        if (tree.treeExternalServiceAttribute) {
          window.open(tree.treeExternalServiceAttribute, '_blank');
        }

        break;
      default:
        tree.active = true;
        this.uniTreesChange.emit(this.trees);

        this.treeId = tree.treeId;
        this.treeIdChange.emit(tree);

        this.router.navigate(['/wsp'], {
          queryParams: {
            treeId: this.treeId
          },
          relativeTo: this.activeRouter
        });

        break;
    }
  }

  private initUniTreeWidth() {
    const resultEl = document.getElementById('uniTreeEl');
    const rowWidth = resultEl.offsetWidth;
    const rows = [];
    let rowIndex = 0;

    let currentRowWidth = 0;
    this.trees.forEach(tree => {
      const treeEl = document.getElementById(`tree-${tree.treeId}`);
      const treeWidth = treeEl.offsetWidth + (tree.treeImage ? 25 : 0);
      treeEl.style.minWidth = `${treeWidth}px`;
      if (currentRowWidth + treeWidth > rowWidth) {
        rowIndex++;
        currentRowWidth = treeWidth;
      } else {
        currentRowWidth += treeWidth;
      }
      if (!rows[rowIndex]) {
        rows[rowIndex] = [];
      }
      if (rowIndex % 2 === 0) {
        rows[rowIndex] = [...rows[rowIndex], treeEl];
      } else {
        rows[rowIndex] = [treeEl, ...rows[rowIndex]];
      }
    });
    let order = 1;
    rows.forEach((row, index) => {
      row.forEach(tree => {
        if (index < rowIndex) {
          tree.style.flexGrow = 1;
        }
        tree.style.order = order;
        tree.style.webkitOrder = order;
        order++;
      })
    });
    if (rowIndex % 2 !== 0) {
      resultEl.style.justifyContent = 'flex-end';
    }
  }
}
