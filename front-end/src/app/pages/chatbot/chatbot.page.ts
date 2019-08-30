import { Component, OnInit, ViewChild } from '@angular/core';
import { AlertController, IonContent } from '@ionic/angular';
import { map } from 'rxjs/operators';
import { ChatbotService } from '../../services/chatbot.service';
import { Storage } from '@ionic/storage';
import { Chat } from '../../models/chat';
import { AndroidPermissions } from '@ionic-native/android-permissions/ngx';
import { ActivatedRoute, Router } from '@angular/router';
import { TextToSpeech } from '@ionic-native/text-to-speech/ngx';

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
    private storage : Storage,
    public androidPermissions : AndroidPermissions,
    public route : ActivatedRoute,
    private router : Router,
    private tts : TextToSpeech) { }
    public message = '';
    public speaking : boolean;
    
  ngOnInit() {
    this.route.params.subscribe(
      params => {
        this.scrollToBottom();
      });
    
    this.loadSavedChats();
    this.chatbotService.getBalance();
    this.storage.get('firstTime').then((firstTime) => {
      if(!firstTime){
        this.storage.set('firstTime', true);
        this.firstTime();
      }
    });
    this.androidPermissions.checkPermission(this.androidPermissions.PERMISSION.RECORD_AUDIO).then(
      result => {
        if(!result.hasPermission){
          this.androidPermissions.requestPermission(this.androidPermissions.PERMISSION.RECORD_AUDIO);
        }
      },
      err => {
        console.log(err);
        this.androidPermissions.requestPermission(this.androidPermissions.PERMISSION.RECORD_AUDIO)
      }
    );
  }
  
  addChat(chat : Chat){
    this.chats.push({
      name: chat.name,
      message: chat.message,
      imageUrl: chat.imageUrl
    });
    this.storage.set('chats', this.chats);
    this.scrollToBottom();
    this.chatbotService.sendChat(chat).subscribe(res => {
      JSON.parse(res).forEach(msg => {
        this.addBotChat(msg);
      });
    });
  }
  addBotChat(message : string){
    const askForReport = message.includes('getReport');
    if(askForReport){
      message = 'Saya akan menyiapkan laporannya sesaat lagi.';
    }
    this.chats.push(new Chat('Wallet Bot', message, 'https://image.flaticon.com/icons/png/512/65/65508.png'));
    this.storage.set('chats', this.chats);
    this.scrollToBottom();
    console.log('speak');
    this.speaking = true;
    this.tts.speak({
      text: message,
      locale: 'id-ID'
    })
      .then(
        () => {this.speaking = false; })
      .catch((reason: any) => console.log(reason));
    if(askForReport){
      this.router.navigate(['/riwayat', {speak : true}]);
    }
  }
  stopSpeech(){
    this.tts.speak('');
    this.speaking=false;
  }
  firstTime(){
    this.addBotChat('Hai, ini percobaan pertamamu ya?\n Untuk dapat mencatat transaksi dan saldo mu, mohon masukkan saldo awal dulu ya.\n Contoh: Saldo awalku 10000 rupiah.');
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
