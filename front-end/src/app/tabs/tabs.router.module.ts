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
              import('../chatbot/chatbot.module').then(m => m.ChatbotPageModule)
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
