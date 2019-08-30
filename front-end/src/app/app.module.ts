import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';


import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import { SplashScreen } from '@ionic-native/splash-screen/ngx';
import { StatusBar } from '@ionic-native/status-bar/ngx';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UniqueDeviceID } from '@ionic-native/unique-device-id/ngx';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HTTP } from '@ionic-native/http/ngx';
import { IonicStorageModule } from '@ionic/storage';
import { SpeechRecognition } from '@ionic-native/speech-recognition/ngx';
import { Camera } from '@ionic-native/camera/ngx';
import {
  SpeechRecognitionModule, RxSpeechRecognitionService
} from '@kamiazya/ngx-speech-recognition';
import { AndroidPermissions } from '@ionic-native/android-permissions/ngx';
import { TextToSpeech } from '@ionic-native/text-to-speech/ngx';

@NgModule({
  declarations: [AppComponent],
  entryComponents: [],
  imports: [
    SpeechRecognitionModule.withConfig({
      lang: 'id-ID',
      interimResults: true,
      maxAlternatives: 10,
    }),
    BrowserModule, 
    IonicModule.forRoot(), 
    AppRoutingModule, 
    IonicStorageModule.forRoot(), 
    HttpClientModule
  ],
  providers: [
    AndroidPermissions,
    HTTP,
    SpeechRecognition,
    RxSpeechRecognitionService,
    Camera,
    TextToSpeech,
    HttpClient,
    UniqueDeviceID,
    StatusBar,
    SplashScreen,
    SpeechRecognition,
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
