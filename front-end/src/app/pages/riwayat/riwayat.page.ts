import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ChatbotService } from '../../services/chatbot.service';
import { compareAsc, format } from 'date-fns'

@Component({
  selector: 'app-riwayat',
  templateUrl: './riwayat.page.html',
  styleUrls: ['./riwayat.page.scss'],
})
export class RiwayatPage implements OnInit, AfterViewInit {
  balance: Number;
  startDate: any;
  endDate: any;

  
  constructor(private chatbotService : ChatbotService) { 
  }

  ngAfterViewInit(){
  }

  ngOnInit() {
    this.chatbotService.getBalance();
  }

  setFormatDate(dateTime){
    dateTime = new Date(dateTime);
    dateTime = dateTime.toISOString().slice(0,10);
    
    return dateTime;
  }

}
