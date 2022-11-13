import { AppStorageService } from 'src/app/core/services/app-storage.service';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FooterComponent } from './footer.component';
import { TranslateModule } from '@ngx-translate/core';
import { SagCommonModule } from 'sag-common';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;
  let appStorageServiceSpy = jasmine.createSpyObj('AppStorageService', ['appVersion']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FooterComponent],
      imports: [
        TranslateModule.forRoot(),
        SagCommonModule.forRoot()
      ],
      providers: [
        {
          provide: AppStorageService,
          useValue: appStorageServiceSpy
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
