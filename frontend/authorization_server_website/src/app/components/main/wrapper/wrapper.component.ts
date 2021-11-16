import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-wrapper',
  templateUrl: './wrapper.component.html',
  styleUrls: ['./wrapper.component.css']
})
export class WrapperComponent implements OnInit {

  @Input() loading = false;
  @Input() maxWidth = '800px';

  constructor() {
  }

  ngOnInit(): void {
  }

}
