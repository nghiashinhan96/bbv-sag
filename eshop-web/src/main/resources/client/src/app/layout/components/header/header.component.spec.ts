import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { UserService } from 'src/app/core/services/user.service';

import { HeaderComponent } from './header.component';

xdescribe('HeaderComponent', () => {
  let component: HeaderComponent;
  let fixture: ComponentFixture<HeaderComponent>;
  let userServiceSpy = jasmine.createSpyObj('UserService', ['salesUser', 'isSalesOnBeHalf', 'hasWholesalerPermission']);

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [HeaderComponent],
      providers: [
        {
          provide: UserService,
          useValue: userServiceSpy
        }
      ]
    })
      .overrideTemplate(HeaderComponent, '<div>Fake HeaderComponent</div>')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
