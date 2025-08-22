import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CheckInComponent } from './components/check-in/check-in.component';
import { PessoaComponent } from './components/pessoa/pessoa.component';
import { GerenciarCheckInsComponent } from './components/gerenciar-check-ins/gerenciar-check-ins.component';
import { GerenciarPessoasComponent } from './components/gerenciar-pessoas/gerenciar-pessoas.component';
import { PaginationComponent } from './components/pagination/pagination.component';

@NgModule({
  declarations: [
    AppComponent,
    CheckInComponent,
    PessoaComponent,
    GerenciarCheckInsComponent,
    GerenciarPessoasComponent,
    PaginationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
