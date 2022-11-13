/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { HeaderSaveShoppingBasketModalComponent } from './header-save-shopping-basket-modal.component';

describe('HeaderSaveShoppingBasketModalComponent', () => {
  let component: HeaderSaveShoppingBasketModalComponent;
  let fixture: ComponentFixture<HeaderSaveShoppingBasketModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HeaderSaveShoppingBasketModalComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeaderSaveShoppingBasketModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
