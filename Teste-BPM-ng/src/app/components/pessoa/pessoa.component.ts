import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PessoaService } from '../../services/pessoa.service';
import { Pessoa } from '../../models/pessoa.model';

@Component({
  selector: 'app-pessoa',
  templateUrl: './pessoa.component.html',
  styleUrls: ['./pessoa.component.css']
})
export class PessoaComponent {
  pessoaForm: FormGroup;
  loading = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private pessoaService: PessoaService
  ) {
    this.pessoaForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(2)]],
      documento: ['', [Validators.required, Validators.minLength(11)]],
      telefone: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  onSubmit(): void {
    if (this.pessoaForm.valid) {
      this.loading = true;
      this.errorMessage = '';
      this.successMessage = '';

      const pessoa: Pessoa = this.pessoaForm.value;

      this.pessoaService.cadastrarPessoa(pessoa).subscribe({
        next: (response) => {
          this.successMessage = 'Pessoa cadastrada com sucesso!';
          this.pessoaForm.reset();
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao cadastrar pessoa:', error);
          this.errorMessage = 'Erro ao cadastrar pessoa';
          this.loading = false;
        }
      });
    }
  }
}
