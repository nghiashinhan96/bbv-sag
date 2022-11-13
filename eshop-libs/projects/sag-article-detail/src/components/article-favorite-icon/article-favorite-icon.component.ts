import { Component, EventEmitter, Input, OnInit, Output, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { BroadcastService } from 'sag-common';
import { FAVORITE_BROADCAST_EVENT } from '../../consts/article-detail.const';
import { ArticleBroadcastKey } from '../../enums/article-broadcast-key.enum';
import { ArticleModel } from '../../models/article.model';
import { FavoriteBusinessService } from '../../services/favorite-business.service';
import { FavoriteService } from '../../services/favorite.service';

@Component({
  selector: 'sag-article-favorite-icon',
  templateUrl: './article-favorite-icon.component.html',
  styleUrls: ['./article-favorite-icon.component.scss']
})
export class SagArticleFavoriteIconComponent implements OnInit, OnDestroy {
  @Input() isFavorited: boolean;
  @Input() favoriteComment: string = null;

  @Input() customClass: string;
  @Input() action: string;

  @Input() article: ArticleModel;
  @Input() uuid: any;

  @Input() gaId: any;
  @Input() vehicle: any;
  @Input() rootModalName: string = '';

  @Output() toggleFavoriteStatusEmit = new EventEmitter<any>();
  @Output() articleChange = new EventEmitter<ArticleModel>();
  @Output() vehicleChange = new EventEmitter<any>();

  favoriteItemEvent$: Subscription;

  constructor (
    private broadcaster: BroadcastService,
    private favoriteService: FavoriteService,
    private favoriteBusinessService: FavoriteBusinessService,
  ) { }

  ngOnInit() {
    this.favoriteItemEvent$ = this.broadcaster.on(ArticleBroadcastKey.FAVORITE_ITEM_EVENT).subscribe((data: any) => {
      switch (data.action) {
        case FAVORITE_BROADCAST_EVENT.EDIT_ARTICLE:
        case FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_ARTICLE: {
          this.handleBroadcastArticle(data, data.action);
          break;
        }
        case FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_LEAF: {
          this.handleBroadcastLeafNode(data);
          break;
        }
        case FAVORITE_BROADCAST_EVENT.EDIT_VEHICLE:
        case FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_VEHICLE:
          this.handleBroadcastVehicle(data, data.action);
          break;
      }
    });
  }

  ngOnDestroy() {
    if (this.favoriteItemEvent$) {
      this.favoriteItemEvent$.unsubscribe();
    }
  }

  toggleFavoriteStatus(event) {
    event.preventDefault();
    event.stopPropagation();

    if (this.action === FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_ARTICLE) {
      this.article.favorite = this.isFavorited;
      this.article.favoriteComment = this.favoriteComment;
      const action = this.isFavorited ? FAVORITE_BROADCAST_EVENT.EDIT_ARTICLE : FAVORITE_BROADCAST_EVENT.ADD_ARTICLE;
      const favoriteItem = this.favoriteService.generateFavoriteItemFromArticle(this.article);
      this.favoriteBusinessService.showFavoriteModal(action, favoriteItem, this.rootModalName);
    }

    if (this.action === FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_LEAF) {
      this.broadcaster.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
        action: this.isFavorited ? FAVORITE_BROADCAST_EVENT.EDIT_LEAF : FAVORITE_BROADCAST_EVENT.ADD_LEAF
      });
    }

    if (this.action === FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_VEHICLE) {
      if (this.vehicle) {
        this.vehicle.favorite = this.isFavorited;
        this.vehicle.favoriteComment = this.favoriteComment;
      }
      const action = this.isFavorited ? FAVORITE_BROADCAST_EVENT.EDIT_VEHICLE : FAVORITE_BROADCAST_EVENT.ADD_VEHICLE;
      const favoriteItem = this.favoriteService.generateFavoriteItemFromVehicle(this.vehicle);
      this.favoriteBusinessService.showFavoriteModal(action, favoriteItem, this.rootModalName);
    }
  }

  handleBroadcastArticle(data, mode) {
    if (data.data && this.article && data.data.articleId == this.article.artid) {
      if (mode === FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_ARTICLE) {
        this.isFavorited = !this.isFavorited;
      }
      this.favoriteComment = this.isFavorited && data.data.comment || '';

      this.article.favorite = this.isFavorited;
      this.article.favoriteComment = this.favoriteComment;
      this.articleChange.emit(this.article);

      this.broadcaster.broadcast(ArticleBroadcastKey.FAVORITE_ITEM_EVENT, {
        action: FAVORITE_BROADCAST_EVENT.UPDATE_SEARCH_ARTICLE_HISTORY,
        article: this.article
      });
    }
  }

  handleBroadcastLeafNode(data) {
    if (data && data.data && data.data.gaId == this.gaId) {
      this.isFavorited = !this.isFavorited;
    }
  }

  handleBroadcastVehicle(data, mode) {
    if (data.data && this.vehicle && data.data.vehicleId == this.vehicle.vehid) {
      if (mode === FAVORITE_BROADCAST_EVENT.TOGGLE_STATUS_VEHICLE) {
        this.isFavorited = !this.isFavorited;
      }
      this.favoriteComment = this.isFavorited && data.data.comment || '';

      this.vehicle.favorite = this.isFavorited;
      this.vehicle.favoriteComment = this.favoriteComment;
      this.vehicleChange.emit(this.vehicle);
    }
  }

}
