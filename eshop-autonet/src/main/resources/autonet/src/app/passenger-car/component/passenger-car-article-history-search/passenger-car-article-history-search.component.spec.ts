import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerCarArticleHistorySearchComponent } from './passenger-car-article-history-search.component';

xdescribe('PassengerCarArticleHistorySearchComponent', () => {
  let component: PassengerCarArticleHistorySearchComponent;
  let fixture: ComponentFixture<PassengerCarArticleHistorySearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassengerCarArticleHistorySearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengerCarArticleHistorySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
