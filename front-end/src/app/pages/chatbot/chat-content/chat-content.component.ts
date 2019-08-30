import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-chat-content',
  templateUrl: './chat-content.component.html',
  styleUrls: ['./chat-content.component.scss'],
})
export class ChatContentComponent implements OnInit {
  @Input() name: string;
  @Input() message: string;
  @Input() imageUrl: string;

  constructor() { }

  ngOnInit() {}

}
