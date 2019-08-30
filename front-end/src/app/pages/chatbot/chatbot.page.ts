import { Component, OnInit, ViewChild } from '@angular/core';
import { AlertController, IonContent } from '@ionic/angular';
import { map } from 'rxjs/operators';
import { ChatbotService } from '../../services/chatbot.service';
import { Storage } from '@ionic/storage';
import { Chat } from '../../models/chat';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.page.html',
  styleUrls: ['./chatbot.page.scss'],
})

export class ChatbotPage implements OnInit { 
  @ViewChild('content', {static: false}) private content: any;
  public available : any;
  public chats : Chat[] = [];
  constructor(
    public alertController: AlertController,
    private chatbotService : ChatbotService,
    private storage : Storage) { }
    public message = '';
  ngOnInit() {
    this.loadSavedChats();
    this.chatbotService.getBalance();
    this.storage.get('firstTime').then((firstTime) => {
      if(!firstTime){
        this.storage.set('firstTime', true);
        this.firstTime();
      }
    });
  }
  
  addChat(chat : Chat){
    this.chats.push({
      name: chat.name,
      message: chat.message,
      imageUrl: chat.imageUrl
    });
    this.scrollToBottom();
    this.chatbotService.sendChat(chat).subscribe(res => {
      JSON.parse(res).forEach(msg => {
        this.addBotChat(msg);
      });
    });
  }
  addBotChat(message){
    this.chats.push(new Chat('Wallet Bot', message, 'https://image.flaticon.com/icons/png/512/65/65508.png'));
    this.scrollToBottom();
  }
  firstTime(){
    this.addBotChat('Hai, ini percobaan pertamamu ya?');
  }
  loadSavedChats(){
    this.storage.get('chats').then((chats) => {
      this.chats = chats || [];
    });
  }
  async presentAlert() {
    const alert = await this.alertController.create({
      header: 'Saldo Habis',
      message: 'Catatan saldo anda kosong, silahkan memasukan informasi saldo anda dengan fitur chat kami.',
      buttons: ['Ok']
    });

    await alert.present();
  }

  scrollToBottom(){
    setTimeout(() => {
      this.content.scrollToBottom();
    });
  }

}
