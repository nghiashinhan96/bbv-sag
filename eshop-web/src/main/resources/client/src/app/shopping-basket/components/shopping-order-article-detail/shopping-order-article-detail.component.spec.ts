import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { MarkedHtmlPipe } from 'sag-common';
import { MockCartItem } from 'src/tests/mock-data/cartItem';
import { RouterTestingModule } from '@angular/router/testing';
import { ShoppingBasketItemModel } from 'src/app/core/models/shopping-basket-item.model';
import { ShoppingOrderArticleDetailComponent } from './shopping-order-article-detail.component';
import { TranslateModule } from '@ngx-translate/core';
import { UserService } from 'src/app/core/services/user.service';

xdescribe('ShoppingOrderArticleDetailComponent', () => {
  let component: ShoppingOrderArticleDetailComponent;
  let fixture: ComponentFixture<ShoppingOrderArticleDetailComponent>;
  let appStorageServiceSpy = jasmine.createSpyObj('AppStorageService', ['appVersion']);
  let userServiceSpy = jasmine.createSpyObj('UserService', ['userPrice']);
  let markedHtmlPipeSpy = jasmine.createSpyObj('MarkedHtmlPipe', ['transform']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ShoppingOrderArticleDetailComponent],
      imports: [
        TranslateModule.forRoot(),
        RouterTestingModule
      ],
      providers:[
        {
          provide: AppStorageService,
          useValue: appStorageServiceSpy
        },
        {
          provide: UserService,
          useValue: userServiceSpy
        },
        {
          provide: MarkedHtmlPipe,
          useValie: markedHtmlPipeSpy
        }
      ]
    })
      .overrideTemplate(ShoppingOrderArticleDetailComponent, 'Fake html')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingOrderArticleDetailComponent);
    component = fixture.componentInstance;
    component.cartItem = new ShoppingBasketItemModel(MockCartItem);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
