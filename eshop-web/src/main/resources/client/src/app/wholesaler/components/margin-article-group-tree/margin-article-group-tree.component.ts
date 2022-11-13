import { Component, Input, OnInit, ViewChild, Output, EventEmitter, OnDestroy } from '@angular/core';
import { MarginArticleGroupModel } from '../../models/margin-article-group.model';
import { SubSink } from 'subsink';
import { MarginService } from '../../services/margin.service';
import { AngularTreeGridComponent } from 'angular-tree-grid';
import { TranslateService } from '@ngx-translate/core';
import { CellActionComponent } from '../cell-action/cell-action.component';
import { SpinnerService } from 'src/app/core/utils/spinner';
import { TablePage } from 'sag-table';
import { remove } from 'lodash';

@Component({
  selector: 'connect-margin-article-group-tree',
  templateUrl: './margin-article-group-tree.component.html',
  styleUrls: ['./margin-article-group-tree.component.scss']
})
export class MarginArticleGroupTreeComponent implements OnInit, OnDestroy {
  @ViewChild('angularGrid', { static: true }) angularGrid: AngularTreeGridComponent;

  configs;

  @Input() currentLangCode: string;
  @Input() margins: number[] = [];
  @Input() page: TablePage;
  @Input() numberOfElements: number;

  @Output() editEmit = new EventEmitter<any>();
  @Output() deleteEmit = new EventEmitter<any>();
  @Output() expandEmit = new EventEmitter<any>();
  @Output() searchEmit = new EventEmitter<any>();

  subs = new SubSink();
  articleGroups: any;
  originArtGrps: MarginArticleGroupModel[] = [];

  private spinnerSelector = '.margin-article-group-table';

  constructor (
    public marginService: MarginService,
    private translateService: TranslateService
  ) {
    this.subs.sink = this.marginService.articleGroups$
      .subscribe(data => {
        this.originArtGrps = [...data];

        this.articleGroups = data;
      });

    this.add();
    this.edit();
    this.delete();
  }

  ngOnInit() {
    this.configs = {
      id_field: 'leafId',
      parent_id_field: 'parentLeafId',
      parent_display_field: 'sagArticleGroup',
      data_loading_text: '',
      css: {
        expand_class: 'fa fa-caret-right',
        collapse_class: 'fa fa-caret-down'
      },
      intermediate_indent: 24,
      load_children_on_expand: true,
      leaf_indent: 28,
      row_class_function: function (rec) {
        if (!rec.hasChild) {
          return 'un-expand';
        } else {
          return '';
        }
      },
      columns: [
        {
          name: 'sagArticleGroup',
          header: this.translateService.instant('MARGIN_MANAGE.ARTICLE_GROUP'),
          width: '20%'
        },
        {
          name: 'sagArticleGroupDescDisplay',
          header: this.translateService.instant('MARGIN_MANAGE.ARTICLE_GROUP_DES'),
          width: '15%'
        },
        {
          name: 'customArticleGroup',
          header: this.translateService.instant('MARGIN_MANAGE.GH_ARTICLE_GROUP_COL'),
          width: '10%'
        },
        {
          name: 'customArticleGroupDesc',
          header: this.translateService.instant('MARGIN_MANAGE.GH_ARTICLE_GROUP_DES_COL'),
          width: '15%'
        },
        {
          name: 'margin1',
          filter: false,
          header: '',
          width: '5%',
          css_class: 'margin-value text-center'
        },
        {
          name: 'margin2',
          header: '',
          filter: false,
          width: '5%',
          css_class: 'margin-value text-center'
        },
        {
          name: 'margin3',
          header: '',
          filter: false,
          width: '5%',
          css_class: 'margin-value text-center'
        },
        {
          name: 'margin4',
          header: '',
          filter: false,
          width: '5%',
          css_class: 'margin-value text-center'
        },
        {
          name: 'margin5',
          header: '',
          filter: false,
          width: '5%',
          css_class: 'margin-value text-center'
        },
        {
          name: 'margin6',
          header: '',
          filter: false,
          width: '5%',
          css_class: 'margin-value text-center'
        },
        {
          name: 'margin7',
          header: '',
          filter: false,
          width: '5%',
          css_class: 'margin-value text-center'
        },
        {
          name: '',
          header: '',
          filter: false,
          width: '5%',
          type: 'custom',
          component: CellActionComponent
        }
      ]
    };
  }

  ngOnDestroy() {
    this.subs.unsubscribe();
  }

  add() {
    this.subs.sink = this.marginService.addSubject.asObservable()
      .subscribe(data => {
        this.addItem(data);
      });
  }

  edit() {
    this.subs.sink = this.marginService.editSubject.asObservable()
      .subscribe(data => {
        this.editItem(data);
      });
  }

  delete() {
    this.subs.sink = this.marginService.deleteSubject.asObservable()
      .subscribe(data => {
        this.deleteItem(data);
      });
  }

  onExpand(e) {
    const row_data = e.data;
    this.expandItem(row_data, e);
  }

  private addItem(data) {
    const artGroup = new MarginArticleGroupModel(data);
    this.handleUpdateTreeView(artGroup.leafId);
  }

  private deleteItem(data) {
    const artGroup = new MarginArticleGroupModel(data);

    this.deleteEmit.emit({
      artGroup,
      callback: (newRowData: any) => {
        this.handleUpdate(artGroup, newRowData, true);
      }
    });
  }

  private editItem(data) {
    const artGroup = new MarginArticleGroupModel(data);

    this.editEmit.emit({
      artGroup,
      callback: (newRowData: MarginArticleGroupModel) => {
        this.handleUpdate(artGroup, newRowData);
        this.handleUpdateTreeView(artGroup.parentLeafId || artGroup.leafId);
      }
    });
  }

  private expandItem(data, evt) {
    const artGroup = new MarginArticleGroupModel(data);
    if (artGroup.isDoneAppendChilds) {
      setTimeout(() => {
        evt.resolve(this.originArtGrps.filter(it => it.parentLeafId === artGroup.leafId));
      }, 1000);
    } else {
      // Call API for getting childs
      this.expandEmit.emit({
        parentId: artGroup.id,
        callback: (childs) => {

          setTimeout(() => {
            evt.resolve(childs);
          }, 1000);

          setTimeout(() => {
            this.appendItemIntoParent(childs, artGroup.id);
          }, 2000);
        }
      });
    }
  }

  private handleUpdateTreeView(leafId: string, isDeleteMode = false) {
    SpinnerService.start(this.spinnerSelector);

    setTimeout(() => {
      const itemNeedExpand = this.findItemByLeafId(leafId);

      if (itemNeedExpand && itemNeedExpand.isDoneAppendChilds) {
        this.angularGrid.expandRow(leafId);
      } else {
        if (isDeleteMode) {
          this.angularGrid.collapseRow(leafId);
        }
      }

      SpinnerService.stop(this.spinnerSelector);
    }, 1000);
  }

  private appendItemIntoParent(childs: MarginArticleGroupModel[], parentId: number) {
    (childs || []).forEach(element => {
      const index = this.findIndexById(element.id);
      if (index === -1) {
        this.originArtGrps = [...this.originArtGrps.concat([element])];
      }
    });

    const parentIndex = this.findIndexById(parentId);
    if (parentIndex > -1) {
      this.originArtGrps[parentIndex].isDoneAppendChilds = true;
    }
  }

  private handleUpdate(itemUpdate: MarginArticleGroupModel, newData: MarginArticleGroupModel, isDeleteMode = false) {
    if (newData) {
      const data = new MarginArticleGroupModel(newData);
      this.updateRowData(itemUpdate.id, data);
    } else {
      this.removeItemInList(itemUpdate);

      if (itemUpdate.root) {
        this.page.currentPage = this.getPage();
        this.searchEmit.emit(this.page);
      } else {
        this.updateParentHasChildFlagAfterDelete(itemUpdate);
        this.handleUpdateTreeView(itemUpdate.parentLeafId, isDeleteMode);
      }
    }
  }

  private updateRowData(artGrpId: number, newData: MarginArticleGroupModel) {
    this.originArtGrps = this.originArtGrps.map(item => {
      if (item.id === artGrpId) {
        item.customArticleGroup = newData.customArticleGroup;
        item.customArticleGroupDesc = newData.customArticleGroupDesc;
        item.mapped = newData.mapped;

        this.margins.forEach(margin => {
          item[`margin${margin}`] = newData[`margin${margin}`];
        });
      }

      return item;
    });
  }

  private removeItemInList(itemRemoved: MarginArticleGroupModel) {
    remove(this.originArtGrps, { id: itemRemoved.id });
  }

  private updateParentHasChildFlagAfterDelete(itemUpdate: MarginArticleGroupModel) {
    const childs = this.originArtGrps.filter(item => item.parentLeafId === itemUpdate.parentLeafId);

    if (childs.length === 0) {
      const parentIndex = this.findIndexByLeafId(itemUpdate.parentLeafId);
      if (parentIndex > -1) {
        this.originArtGrps[parentIndex].hasChild = false;
        this.originArtGrps[parentIndex].isDoneAppendChilds = false;
      }
    }
  }

  private findIndexById(itemId: number) {
    return this.originArtGrps.findIndex(item => item.id === itemId);
  }

  private findIndexByLeafId(leafId: string) {
    return this.originArtGrps.findIndex(item => item.leafId === leafId);
  }

  private findItemByLeafId(leafId: string) {
    return this.originArtGrps.find(item => item.leafId === leafId);
  }

  private getPage() {
    if (this.numberOfElements === 1) {
      const prevPage = this.page.currentPage - 1;
      return prevPage > 0 ? prevPage : 0;
    }

    return this.page.currentPage;
  }
}
