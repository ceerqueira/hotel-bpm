import { Component, OnInit } from '@angular/core';
import { CheckInService } from '../../services/check-in.service';
import { CheckIn, CheckInUpdate, PageResponse } from '../../models/check-in.model';

@Component({
  selector: 'app-gerenciar-check-ins',
  templateUrl: './gerenciar-check-ins.component.html',
  styleUrls: ['./gerenciar-check-ins.component.css']
})
export class GerenciarCheckInsComponent implements OnInit {
  checkIns: CheckIn[] = [];
  checkInsAtivos: CheckIn[] = [];
  checkInsFinalizados: CheckIn[] = [];
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;
  loading = false;
  editingCheckIn: CheckIn | null = null;
  showEditForm = false;
  activeTab = 'todos';

  constructor(private checkInService: CheckInService) { }

  ngOnInit(): void {
    this.carregarCheckIns();
    this.carregarHospedesAtivos();
    this.carregarHospedesFinalizados();
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;

    // Recarregar dados específicos da aba selecionada
    if (tab === 'ativos') {
      this.carregarHospedesAtivos();
    } else if (tab === 'finalizados') {
      this.carregarHospedesFinalizados();
    } else if (tab === 'todos') {
      this.carregarCheckIns();
    }
  }

  carregarCheckIns(): void {
    this.loading = true;
    this.checkInService.listarTodosCheckIns(this.currentPage, this.pageSize)
      .subscribe({
        next: (response: PageResponse<CheckIn>) => {
          this.checkIns = response.content;
          this.totalElements = response.totalElements;
          this.totalPages = response.totalPages;
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar check-ins:', error);
          this.loading = false;
        }
      });
  }

  carregarHospedesAtivos(): void {
    this.loading = true;
    this.checkInService.buscarHospedesAtivos()
      .subscribe({
        next: (response: PageResponse<CheckIn>) => {
          this.checkInsAtivos = response.content || [];
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar hóspedes ativos:', error);
          this.checkInsAtivos = [];
          this.loading = false;
        }
      });
  }

  carregarHospedesFinalizados(): void {
    this.loading = true;
    this.checkInService.buscarHospedesFinalizados()
      .subscribe({
        next: (response: PageResponse<CheckIn>) => {
          this.checkInsFinalizados = response.content || [];
          this.loading = false;
        },
        error: (error) => {
          console.error('Erro ao carregar hóspedes finalizados:', error);
          this.checkInsFinalizados = [];
          this.loading = false;
        }
      });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.carregarCheckIns();
  }

  editarCheckIn(checkIn: CheckIn): void {
    this.editingCheckIn = { ...checkIn };
    this.showEditForm = true;
  }

  editarCheckInPorId(id: number): void {
    this.checkInService.buscarCheckInPorId(id)
      .subscribe({
        next: (checkIn: CheckIn) => {
          this.editingCheckIn = { ...checkIn };
          this.showEditForm = true;
        },
        error: (error) => {
          console.error('Erro ao buscar check-in:', error);
        }
      });
  }

  salvarEdicao(): void {
    if (this.editingCheckIn && this.editingCheckIn.id) {
      const checkInUpdate: CheckInUpdate = {
        dataEntrada: this.editingCheckIn.dataEntrada,
        dataSaidaPrevista: this.editingCheckIn.dataSaidaPrevista,
        adicionalVeiculo: this.editingCheckIn.adicionalVeiculo
      };

      this.checkInService.atualizarCheckIn(this.editingCheckIn.id, checkInUpdate)
        .subscribe({
          next: () => {
            this.carregarCheckIns();
            this.carregarHospedesAtivos();
            this.carregarHospedesFinalizados();
            this.cancelarEdicao();
          },
          error: (error) => {
            console.error('Erro ao atualizar check-in:', error);
          }
        });
    }
  }

  cancelarEdicao(): void {
    this.editingCheckIn = null;
    this.showEditForm = false;
  }

  deletarCheckIn(id: number): void {
    if (confirm('Tem certeza que deseja deletar este check-in?')) {
      this.checkInService.deletarCheckIn(id)
        .subscribe({
          next: () => {
            this.carregarCheckIns();
            this.carregarHospedesAtivos();
            this.carregarHospedesFinalizados();
          },
          error: (error) => {
            console.error('Erro ao deletar check-in:', error);
          }
        });
    }
  }

  formatarData(data: string): string {
    if (!data) return 'Não informado';
    try {
      return new Date(data).toLocaleString('pt-BR');
    } catch (error) {
      return 'Data inválida';
    }
  }

  formatarValor(valor: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL'
    }).format(valor);
  }

  calcularValorTotal(checkIn: CheckIn): number {
    if (!checkIn || checkIn.valorTotal === undefined || checkIn.valorTotal === null) {
      return 0;
    }
    return checkIn.valorTotal;
  }
}
