import { Directive, OnInit, ElementRef } from '@angular/core';
import { ThemeService } from '../services/theme.service';

@Directive({
  selector: '[connectAppTheme]'
})
export class AppThemeDirective implements OnInit {

  constructor(
    private themeService: ThemeService,
    private body: ElementRef
  ) { }

  ngOnInit(): void {
    throw new Error("Method not implemented.");
  }

}
