import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'summarizer',
    pathMatch: 'full'
  },
  {
    path: 'summarizer',
    loadComponent: () =>
      import('./features/summarizer/summarizer.component').then(m => m.SummarizerComponent)
  }
];