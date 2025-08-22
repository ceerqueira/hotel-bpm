import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CheckInComponent } from './components/check-in/check-in.component';
import { PessoaComponent } from './components/pessoa/pessoa.component';
import { GerenciarCheckInsComponent } from './components/gerenciar-check-ins/gerenciar-check-ins.component';
import { GerenciarPessoasComponent } from './components/gerenciar-pessoas/gerenciar-pessoas.component';

const routes: Routes = [
  { path: '', redirectTo: '/check-in', pathMatch: 'full' },
  { path: 'check-in', component: CheckInComponent },
  { path: 'pessoa', component: PessoaComponent },
  { path: 'gerenciar-check-ins', component: GerenciarCheckInsComponent },
  { path: 'gerenciar-pessoas', component: GerenciarPessoasComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
