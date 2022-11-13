import { BsModalRef, ModalModule } from 'ngx-bootstrap/modal';
import { ComponentFixture, TestBed, async } from '@angular/core/testing';

import { TranslateModule } from '@ngx-translate/core';
import { VideoPlayerComponent } from './video-player.component';


describe('VideoPlayerComponent', () => {
  let component: VideoPlayerComponent;
  let fixture: ComponentFixture<VideoPlayerComponent>;
  let bsModalRefSpy = jasmine.createSpyObj('BsModalRef', ['hide']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [VideoPlayerComponent],
      imports: [
        ModalModule.forRoot(),
        TranslateModule.forRoot()
      ],
      providers: [
        {
          provide: BsModalRef,
          useValue: bsModalRefSpy
        }
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VideoPlayerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
