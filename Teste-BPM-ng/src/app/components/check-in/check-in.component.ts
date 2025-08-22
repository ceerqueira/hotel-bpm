import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PessoaService } from '../../services/pessoa.service';
import { CheckInService } from '../../services/check-in.service';
import { CheckInRequest, PageResponse } from '../../models/check-in.model';
import { Pessoa } from '../../models/pessoa.model';

@Component({
  selector: 'app-check-in',
  templateUrl: './check-in.component.html',
  styleUrls: ['./check-in.component.css']
})
export class CheckInComponent implements OnInit {
  checkInForm: FormGroup;
  pessoas: Pessoa[] = [];
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private pessoaService: PessoaService,
    private checkInService: CheckInService
  ) {
    this.checkInForm = this.fb.group({
      dataEntrada: ['', Validators.required],
      dataSaidaPrevista: ['', Validators.required],
      pessoaId: ['', Validators.required],
      adicionalVeiculo: [false]
    });
  }

  ngOnInit(): void {
    this.carregarPessoas();
  }

  carregarPessoas(): void {
    this.pessoaService.buscarPessoas().subscribe({
      next: (response: PageResponse<Pessoa>) => {
        this.pessoas = response.content;
      },
      error: (error) => {
        console.error('Erro ao carregar pessoas:', error);
        this.errorMessage = 'Erro ao carregar lista de pessoas';
      }
    });
  }

  onSubmit(): void {
    if (this.checkInForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const formValue = this.checkInForm.value;
      const pessoaSelecionada = this.pessoas.find(p => p.id === formValue.pessoaId);
      
      if (!pessoaSelecionada) {
        this.errorMessage = 'Pessoa não encontrada';
        this.loading = false;
        return;
      }

      const checkInRequest: CheckInRequest = {
        pessoa: pessoaSelecionada,
        dataEntrada: formValue.dataEntrada,
        dataSaidaPrevista: formValue.dataSaidaPrevista,
        adicionalVeiculo: formValue.adicionalVeiculo
      };

      this.checkInService.realizarCheckIn(checkInRequest).subscribe({
        next: (response) => {
          this.successMessage = 'Check-in realizado com sucesso!';
          this.checkInForm.reset();
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao realizar check-in:', error);
          this.errorMessage = 'Erro ao realizar check-in';
          this.loading = false;
        }
      });
    }
  }
}
