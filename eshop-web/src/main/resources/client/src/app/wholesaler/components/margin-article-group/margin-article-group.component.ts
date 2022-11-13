import { Component, OnInit, OnDestroy } from '@angular/core';
import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { DEFAULT_LANG_CODE } from 'src/app/core/conts/app-lang-code.constant';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { MarginArticleGroupFormModalComponent } from '../margin-article-group-form-modal/margin-article-group-form-modal.component';
import { MARGIN_VALUES } from '../../enums/margin-mgt.enum';
import { AppModalService } from 'src/app/core/services/app-modal.service';
@Component({
  selector: 'connect-margin-article-group',
  templateUrl: './margin-article-group.component.html',
  styleUrls: ['./margin-article-group.component.scss']
})
export class MarginArticleGroupComponent implements OnInit, OnDestroy {
  currentLangCode: string = DEFAULT_LANG_CODE;
  margins = MARGIN_VALUES;

  private bsModalRef: BsModalRef;

  constructor (
    private appStorage: AppStorageService,
    private modalService: BsModalService,
    private appModal: AppModalService
  ) {
    this.currentLangCode = this.appStorage.appLangCode || DEFAULT_LANG_CODE;
  }

  ngOnInit() {
  }

  ngOnDestroy() {
    if (this.bsModalRef) {
      this.bsModalRef.hide();
    }
  }

  editArtGrp(event) {
    this.bsModalRef = this.modalService.show(MarginArticleGroupFormModalComponent, {
      initialState: {
        artGroup: event.artGroup || event,
        title: 'MARGIN_MANAGE.EDIT_ARTICL_GROUP',
        callback: (res) => {
          const modalHidden = this.modalService.onHidden.subscribe(data => {
            modalHidden.unsubscribe();
            if (event.callback) {
              event.callback(res);
            }
          });
        }
      },
      ignoreBackdropClick: true
    });

    this.appModal.modals = this.bsModalRef;
  }
}
