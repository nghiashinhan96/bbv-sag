import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageDeleteModalComponent } from './message-delete-modal.component';

xdescribe('MessageDeleteModalComponent', () => {
  let component: MessageDeleteModalComponent;
  let fixture: ComponentFixture<MessageDeleteModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MessageDeleteModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MessageDeleteModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
