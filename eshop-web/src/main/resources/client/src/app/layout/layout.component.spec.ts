import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { UserServiceStub } from 'src/tests/services/UserService.stub';
import { UserService } from '../core/services/user.service';
import { FeedbackRecordingService } from '../feedback/services/feedback-recording.service';

import { LayoutComponent } from './layout.component';

xdescribe('LayoutComponent', () => {
  let component: LayoutComponent;
  let fixture: ComponentFixture<LayoutComponent>;
  let feedbackRecordingServiceSpy = jasmine.createSpyObj('FeedbackRecordingService', ['']);
  var userServiceStub = null;

  beforeEach(async(() => {
    userServiceStub = new UserServiceStub();

    TestBed.configureTestingModule({
      declarations: [LayoutComponent],
      imports: [
        RouterTestingModule
      ],
      providers: [
        {
          provide: UserService,
          useValue: userServiceStub
        },
        {
          provide: FeedbackRecordingService,
          useValue: feedbackRecordingServiceSpy
        }
      ]
    })
      .overrideTemplate(LayoutComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    fixture.destroy();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
