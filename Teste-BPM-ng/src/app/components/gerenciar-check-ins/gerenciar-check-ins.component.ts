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
  pageSize = 5;
  totalElements = 0;
  totalPages = 0;
  
  currentPageAtivos = 0;
  totalElementsAtivos = 0;
  totalPagesAtivos = 0;
  
  currentPageFinalizados = 0;
  totalElementsFinalizados = 0;
  totalPagesFinalizados = 0;
  
  loading = false;
  loadingAtivos = false;
  loadingFinalizados = false;
  editingCheckIn: CheckIn | null = null;
  showEditForm = false;
  activeTab = 'todos';

  pageSizeOptions = [5, 10, 20, 50];

  constructor(private checkInService: CheckInService) { }

  ngOnInit(): void {
    this.carregarCheckIns();
    this.carregarHospedesAtivos();
    this.carregarHospedesFinalizados();
  }

  setActiveTab(tab: string): void {
    this.activeTab = tab;
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
    this.loadingAtivos = true;
    this.checkInService.buscarHospedesAtivos(this.currentPageAtivos, this.pageSize)
      .subscribe({
        next: (response: PageResponse<CheckIn>) => {
          this.checkInsAtivos = response.content || [];
          this.totalElementsAtivos = response.totalElements;
          this.totalPagesAtivos = response.totalPages;
          this.loadingAtivos = false;
        },
        error: (error) => {
          console.error('Erro ao carregar hóspedes ativos:', error);
          this.checkInsAtivos = [];
          this.loadingAtivos = false;
        }
      });
  }

  carregarHospedesFinalizados(): void {
    this.loadingFinalizados = true;
    this.checkInService.buscarHospedesFinalizados(this.currentPageFinalizados, this.pageSize)
      .subscribe({
        next: (response: PageResponse<CheckIn>) => {
          this.checkInsFinalizados = response.content || [];
          this.totalElementsFinalizados = response.totalElements;
          this.totalPagesFinalizados = response.totalPages;
          this.loadingFinalizados = false;
        },
        error: (error) => {
          console.error('Erro ao carregar hóspedes finalizados:', error);
          this.checkInsFinalizados = [];
          this.loadingFinalizados = false;
        }
      });
  }

  onPageChange(page: number): void {
    this.currentPage = page;
    this.carregarCheckIns();
  }

  onPageChangeAtivos(page: number): void {
    this.currentPageAtivos = page;
    this.carregarHospedesAtivos();
  }

  onPageChangeFinalizados(page: number): void {
    this.currentPageFinalizados = page;
    this.carregarHospedesFinalizados();
  }

  onPageSizeChange(event: Event): void {
    const target = event.target as HTMLSelectElement;
    const newSize = parseInt(target.value);
    this.pageSize = newSize;
    this.currentPage = 0;
    this.currentPageAtivos = 0;
    this.currentPageFinalizados = 0;
    
    if (this.activeTab === 'todos') {
      this.carregarCheckIns();
    } else if (this.activeTab === 'ativos') {
      this.carregarHospedesAtivos();
    } else if (this.activeTab === 'finalizados') {
      this.carregarHospedesFinalizados();
    }
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
