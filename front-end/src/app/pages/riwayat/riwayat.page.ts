import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ChatbotService } from '../../services/chatbot.service';
import { compareAsc, format } from 'date-fns'
import { TextToSpeech } from '@ionic-native/text-to-speech/ngx';

@Component({
  selector: 'app-riwayat',
  templateUrl: './riwayat.page.html',
  styleUrls: ['./riwayat.page.scss'],
})
export class RiwayatPage implements OnInit, AfterViewInit {
  balance: Number;
  startDate = Date();
  endDate = Date();

  constructor(
    private chatbotService : ChatbotService,
    private route: ActivatedRoute,
    private tts : TextToSpeech) { 
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
        this.tts.speak({
          text: speechText,
          locale: 'id-ID'
        });
      }
    });
  }
  stopSpeech(){
    this.tts.speak('');
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
