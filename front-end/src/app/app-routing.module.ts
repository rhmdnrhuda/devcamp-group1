import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./pages/chatbot/chatbot.module').then(m => m.ChatbotPageModule)
  },
  { path: 'chatbot', loadChildren: './pages/chatbot/chatbot.module#ChatbotPageModule' },
  { path: 'riwayat', loadChildren: './pages/riwayat/riwayat.module#RiwayatPageModule' }
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
