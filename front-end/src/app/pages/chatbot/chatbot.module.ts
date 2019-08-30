import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { IonicModule } from '@ionic/angular';

import { ChatbotPage } from './chatbot.page';
import { ChatContentComponent } from './chat-content/chat-content.component';
import { InputFieldComponent } from './input-field/input-field.component';

const routes: Routes = [
  {
    path: '',
    component: ChatbotPage
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    RouterModule.forChild(routes)
  ],
  declarations: [ChatbotPage, ChatContentComponent, InputFieldComponent]
})
export class ChatbotPageModule {}
