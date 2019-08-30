import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChatbotService } from '../../services/chatbot.service';
import { compareAsc, format } from 'date-fns'
import { TextToSpeech } from '@ionic-native/text-to-speech/ngx';
import {
  RxSpeechRecognitionService,
  resultList,
} from '@kamiazya/ngx-speech-recognition';
import { AndroidPermissions } from '@ionic-native/android-permissions/ngx';
import { Chat } from 'src/app/models/chat';

@Component({
  selector: 'app-riwayat',
  templateUrl: './riwayat.page.html',
  styleUrls: ['./riwayat.page.scss'],
})
export class RiwayatPage implements OnInit, AfterViewInit {
  balance: Number;
  speaking: boolean;
  startDate = Date();
  endDate = Date();
  command : string;
  isListening : boolean;

  constructor(
    private chatbotService : ChatbotService,
    private route: ActivatedRoute,
    private tts : TextToSpeech,
    public speechRecognition : RxSpeechRecognitionService, 
    public androidPermissions : AndroidPermissions) { 
  }

  ngAfterViewInit(){
    this.route.params.subscribe( async (params) => {
      let speechText = "";
      if(params['speak']){
        await this.chatbotService.getTransaction(this.setFormatDate(this.startDate), this.setFormatDate(this.endDate));
        this.chatbotService.transactions.forEach(transaction => {
          let sum = 0;
          const tanggal = transaction.tanggal;
          speechText += '\nPada '+tanggal+', riwayat transaksi anda adalah: ';
          transaction.data.forEach(element => {
            sum += element.Amount;
            speechText += element.Amount.toString() + " rupiah pada kategori: " + element.Category+'. ';
          }); 
          speechText += 'Total adalah ' + sum.toString() +'. ';
        });
        this.speaking = true;
        this.tts.speak({
          text: speechText,
          locale: 'id-ID'
        }).then(() => {this.speaking = false; });
      }
    });
  }
  voice() {
    this.isListening = true;
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
    this.speechRecognition
      .listen()
      .pipe(resultList)
      .subscribe(
        (list: SpeechRecognitionResultList) => {
          this.command = list.item(0).item(0).transcript;
          console.log('RxComponent:onresult', this.command, list);
        },
        (error) => {
          console.log('error: '+ error)
        },
        () => {
          this.sendMessage();
          this.isListening = false;
        });
  }
  sendMessage(){
    this.chatbotService.sendChat(new Chat('Anda',this.command, 'https://prd-wret.s3-us-west-2.amazonaws.com/assets/palladium/production/s3fs-public/styles/full_width/public/thumbnails/image/placeholder-profile_2_0.png'))
      .subscribe((res) => {
        // TODO
      });
    this.command = "";
  }
  stopSpeech(){
    this.tts.speak('');
    this.speaking = false;
  }
  ngOnInit() {
    this.chatbotService.getBalance();
  }
  
  setFormatDate(dateTime){
    dateTime = new Date(dateTime);
    dateTime = format(dateTime, "yyyy-MM-dd");
    return dateTime;
  }

}
