import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TabsPage } from './tabs.page';

const routes: Routes = [
  {
    path: 'tabs',
    component: TabsPage,
    children: [
      {
        path: 'chatbot',
        children: [
          {
            path: '',
            loadChildren: () =>
              import('../pages/chatbot/chatbot.module').then(m => m.ChatbotPageModule)
          }
        ]
      },
      {
        path: 'riwayat',
        children: [
          {
            path: '',
            loadChildren: () =>
              import('../pages/riwayat/riwayat.module').then(m => m.RiwayatPageModule)
          }
        ]
      },
      {
        path: '',
        redirectTo: '/tabs/chatbot',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '',
    redirectTo: '/tabs/chatbot',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TabsPageRoutingModule {}
