import { BsModalRef, ModalModule } from 'ngx-bootstrap/modal';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { SagCurrencyModule, SagCurrencyPipe } from 'sag-currency';

import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ShoppingExportModalComponent } from './shopping-export-modal.component';
import { ShoppingExportService } from '../../services/shopping-export.service';
import { TranslateModule } from '@ngx-translate/core';
import { UserService } from 'src/app/core/services/user.service';
import { UserServiceStub } from 'src/tests/services/UserService.stub';

xdescribe('ShoppingExportModalComponent', () => {
  let component: ShoppingExportModalComponent;
  let fixture: ComponentFixture<ShoppingExportModalComponent>;
  let bsModalRefSpy = jasmine.createSpyObj('BsModalRef', ['hide']);
  let userServiceStub= null;
  let shoppingExportServiceSpy = jasmine.createSpyObj('ShoppingExportService', ['']);
  let sagCurrencyPipeSpy = jasmine.createSpyObj('SagCurrencyPipe', ['']);

  beforeEach(async(() => {
    userServiceStub = new UserServiceStub();

    TestBed.configureTestingModule({
      declarations: [ShoppingExportModalComponent],
      imports: [
        TranslateModule.forRoot(),
        ModalModule.forRoot(),
        HttpClientTestingModule,
        SagCurrencyModule.forRoot()
      ],
      providers: [
        {
          provide: BsModalRef,
          useValue: bsModalRefSpy
        },
        {
          provide: UserService,
          useValue: userServiceStub
        },
        {
          provide: ShoppingExportService,
          useValue: shoppingExportServiceSpy
        },
        {
          provide: SagCurrencyPipe,
          useValue: sagCurrencyPipeSpy
        }
      ]
    })
      .overrideTemplate(ShoppingExportModalComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ShoppingExportModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
