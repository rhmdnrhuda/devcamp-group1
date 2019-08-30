import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Chat } from '../models/chat';
import { ApiResponse } from '../models/apiResponse';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {
  deviceId : any;
  private baseUrl = "http://10.50.218.17:8000/";
  balance : any;
  transactions: any;
  startDate;
  endDate;

  constructor(
    private http:HttpClient
  ) { 
    this.deviceId = "xxx";
    this.getBalance;
  }
  getBalance(){
    this.http.get(this.baseUrl + 'getBalance?userid=' + this.deviceId, {responseType: 'text'})
      .subscribe(res => {
        this.balance = res;
      });
  }
  sendChat(chat : Chat){
    const payload = new ApiResponse(chat.name, chat.message);
    return this.http.post(this.baseUrl+'addTransaction', JSON.stringify(payload), {responseType: 'text'});
  }

  getTransaction(startdate,enddate){
    // console.log(startdate);
    
    if(this.startDate != startdate || this.endDate != enddate){
      this.http.get(this.baseUrl + 'getTransactions?userid=' + this.deviceId+ '&startdate='+startdate+'&enddate='+enddate, {responseType: 'text'})
      .subscribe(res => {
        this.transactions = JSON.parse(res);
      });
      this.startDate = startdate;
      this.endDate = enddate;
    }
  }
}
