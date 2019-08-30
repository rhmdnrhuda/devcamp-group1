import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { Chat } from '../../../models/chat';
import {
  RxSpeechRecognitionService,
  resultList,
} from '@kamiazya/ngx-speech-recognition';

@Component({
  selector: 'app-input-field',
  templateUrl: './input-field.component.html',
  styleUrls: ['./input-field.component.scss'],
})
export class InputFieldComponent implements OnInit {
  chatMessage : any;
  @Output() addChat = new EventEmitter<Chat>();
  constructor(public speechRecognition : RxSpeechRecognitionService) { }

  ngOnInit() {}

  sendMessage(){
    const imageUrl = 'https://prd-wret.s3-us-west-2.amazonaws.com/assets/palladium/production/s3fs-public/styles/full_width/public/thumbnails/image/placeholder-profile_2_0.png'
    const chat = new Chat('Anda', this.chatMessage, imageUrl);
    this.addChat.emit(chat);
    this.chatMessage = "";
  }
  listen() {
    console.log('test');
    this.speechRecognition
      .listen()
      .pipe(resultList)
      .subscribe(
        (list: SpeechRecognitionResultList) => {
          this.chatMessage = list.item(0).item(0).transcript;
          console.log('RxComponent:onresult', this.chatMessage, list);
        },
        (error) => {
          console.log('error: '+ error)
        },
        () => {
          this.sendMessage();
        });
  }
}
