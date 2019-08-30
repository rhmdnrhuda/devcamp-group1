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

  constructor(
    private http:HttpClient
  ) { 
    this.deviceId = "123";
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
}
