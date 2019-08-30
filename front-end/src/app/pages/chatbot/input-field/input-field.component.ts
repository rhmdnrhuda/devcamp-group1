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
    const imageUrl = 'https://prd-wret.s3-us-west-2.amazonaws.com/assets/palladium/production/s3fs-public/styles/full_width/public/thumbnails/image/placeholder-profile_2_0.png'
    const chat = new Chat('Anda', this.chatMessage, imageUrl);
    this.addChat.emit(chat);
    this.chatMessage = "";
  }
  
}
