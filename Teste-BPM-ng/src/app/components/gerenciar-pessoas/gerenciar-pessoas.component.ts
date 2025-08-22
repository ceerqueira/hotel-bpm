import { Component, OnInit } from '@angular/core';
import { PessoaService } from '../../services/pessoa.service';
import { Pessoa } from '../../models/pessoa.model';
import { PageResponse } from '../../models/check-in.model';

@Component({
  selector: 'app-gerenciar-pessoas',
  templateUrl: './gerenciar-pessoas.component.html',
  styleUrls: ['./gerenciar-pessoas.component.css']
})
export class GerenciarPessoasComponent implements OnInit {
  pessoas: Pessoa[] = [];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  editingPessoa: Pessoa | null = null;
  showEditForm = false;
  showAddForm = false;
  searchTerm = '';

  constructor(private pessoaService: PessoaService) { }

  ngOnInit(): void {
    this.carregarPessoas();
  }

  carregarPessoas(): void {
    this.loading = true;
    this.pessoaService.buscarPessoas(this.searchTerm, this.currentPage, this.pageSize)
      .subscribe({
        next: (response: PageResponse<Pessoa>) => {
          this.pessoas = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar pessoas:', error);
          this.loading = false;
        }
      });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.carregarPessoas();
  }

  onSearch(): void {
    this.currentPage = 0;
    this.carregarPessoas();
  }

  adicionarPessoa(): void {
    this.editingPessoa = {
      nome: '',
      documento: '',
      telefone: ''
    };
    this.showAddForm = true;
  }

  editarPessoa(pessoa: Pessoa): void {
    this.editingPessoa = { ...pessoa };
    this.showEditForm = true;
  }

  salvarPessoa(): void {
    if (this.editingPessoa) {
      if (this.showAddForm) {
        // Adicionar nova pessoa
        this.pessoaService.cadastrarPessoa(this.editingPessoa)
          .subscribe({
            next: () => {
              this.carregarPessoas();
              this.cancelarEdicao();
            },
            error: (error) => {
              console.error('Erro ao cadastrar pessoa:', error);
            }
          });
      } else if (this.editingPessoa.id) {
        // Atualizar pessoa existente
        this.pessoaService.atualizarPessoa(this.editingPessoa.id, this.editingPessoa)
          .subscribe({
            next: () => {
              this.carregarPessoas();
              this.cancelarEdicao();
            },
            error: (error) => {
              console.error('Erro ao atualizar pessoa:', error);
            }
          });
      }
    }
  }

  cancelarEdicao(): void {
    this.editingPessoa = null;
    this.showEditForm = false;
    this.showAddForm = false;
  }

  deletarPessoa(id: number): void {
    if (confirm('Tem certeza que deseja deletar esta pessoa?')) {
      this.pessoaService.deletarPessoa(id)
        .subscribe({
          next: () => {
            this.carregarPessoas();
          },
          error: (error) => {
            console.error('Erro ao deletar pessoa:', error);
          }
        });
    }
  }

  limparBusca(): void {
    this.searchTerm = '';
    this.currentPage = 0;
    this.carregarPessoas();
  }
}
