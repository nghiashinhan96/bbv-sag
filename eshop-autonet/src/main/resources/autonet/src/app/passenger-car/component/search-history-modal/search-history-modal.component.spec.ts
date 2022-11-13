import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchHistoryModalComponent } from './search-history-modal.component';

xdescribe('SearchHistoryModalComponent', () => {
  let component: SearchHistoryModalComponent;
  let fixture: ComponentFixture<SearchHistoryModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SearchHistoryModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchHistoryModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
