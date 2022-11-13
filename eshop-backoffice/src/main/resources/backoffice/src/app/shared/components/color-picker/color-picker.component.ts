import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'backoffice-color-picker',
  templateUrl: './color-picker.component.html',
  styleUrls: ['./color-picker.component.scss']
})
export class ColorPickerComponent implements OnInit {
  @Input() color: string; // hex color
  @Output() updateColorEmit = new EventEmitter<string>();

  constructor() { }

  ngOnInit() {
  }

  updateColor(color) {
    this.updateColorEmit.emit(color);
    this.color = color;
  }
}
