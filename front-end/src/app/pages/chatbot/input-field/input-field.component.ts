import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Chat } from '../../../models/chat';

@Component({
  selector: 'app-input-field',
  templateUrl: './input-field.component.html',
  styleUrls: ['./input-field.component.scss'],
})
export class InputFieldComponent implements OnInit {
  chatMessage : any;
  @Output() addChat = new EventEmitter<Chat>();
  constructor() { }

  ngOnInit() {}

  sendMessage(){
    const chat = new Chat('Anda', this.chatMessage, 'https://image.flaticon.com/icons/png/512/65/65508.png');
    this.addChat.emit(chat);
    this.chatMessage = "";
  }
  
}
