import { Component, OnInit } from '@angular/core';
import { UniqueDeviceID } from '@ionic-native/unique-device-id/ngx';
import { HttpClient } from '@angular/common/http';
import { HTTP } from '@ionic-native/http/ngx';
import { AlertController } from '@ionic/angular';
import { map } from 'rxjs/operators';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.page.html',
  styleUrls: ['./chatbot.page.scss'],
})

export class ChatbotPage implements OnInit {
  private baseUrl = "http://10.50.218.17:8000/";
  deviceId : any;
  balance : any;

  constructor(
    private uniqueDeviceID : UniqueDeviceID,
    private http : HttpClient,
    public alertController: AlertController) { }

  ngOnInit() {
    this.deviceId = "123";
    this.http.get(this.baseUrl + 'getBalance?userid=' + this.deviceId, {responseType: 'text'})
      .subscribe(res => {
        this.balance = res;
        if(this.balance <= 0){
          this.presentAlert();
        }
      });
    // this.uniqueDeviceID.get()
    //   .then((uuid: any) => {
    //     this.deviceId = uuid;
    //     console.log("device id : " + this.deviceId);
    //     console.log(this.http.get(this.baseUrl+"getBalance?userid="+this.deviceId));
    //   })
    //   .catch((error: any) => {
    //     this.deviceId = "error "+error;
    //   });
  }

  async presentAlert() {
    const alert = await this.alertController.create({
      header: 'Saldo Habis',
      message: 'Catatan saldo anda kosong, silahkan memasukan informasi saldo anda dengan fitur chat kami.',
      buttons: ['Ok']
    });

    await alert.present();
  }

}
