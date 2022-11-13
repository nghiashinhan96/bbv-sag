import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PassengerCarArticleSearchComponent } from './passenger-car-article-search.component';

xdescribe('PassengerCarArticleSearchComponent', () => {
  let component: PassengerCarArticleSearchComponent;
  let fixture: ComponentFixture<PassengerCarArticleSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PassengerCarArticleSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PassengerCarArticleSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
