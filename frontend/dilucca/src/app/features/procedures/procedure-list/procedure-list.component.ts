import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Procedure } from '../../../core/models/procedure.model';
import { ProcedureService } from '../../../core/services/procedure.service';
import { ProcedureFormComponent } from '../procedure-form/procedure-form.component';

@Component({
  selector: 'app-procedure-list',
  standalone: true,
  imports: [CommonModule, FormsModule, ProcedureFormComponent],
  templateUrl: './procedure-list.component.html',
  styleUrl: './procedure-list.component.css'
})
export class ProcedureListComponent implements OnInit {

  allProcedures = signal<Procedure[]>([]);
  loading = signal(false);
  showFormModal = signal(false);
  selectedProcedure = signal<Procedure | null>(null);

  searchQuery = '';
  activeOnly = false;

  constructor(private procedureService: ProcedureService) {}

  ngOnInit(): void {
    this.loadProcedures();
  }

  loadProcedures(): void {
    this.loading.set(true);
    this.procedureService.getProcedures().subscribe({
      next: (data) => {
        this.allProcedures.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  filteredProcedures(): Procedure[] {
    const query = this.searchQuery.trim().toLowerCase();
    return this.allProcedures().filter(p => {
      const matchesSearch = !query ||
        (p.name && p.name.toLowerCase().includes(query)) ||
        (p.description && p.description.toLowerCase().includes(query));
      const matchesActive = !this.activeOnly || p.active;
      return matchesSearch && matchesActive;
    });
  }

  openCreateModal(): void {
    this.selectedProcedure.set(null);
    this.showFormModal.set(true);
  }

  openEditModal(procedure: Procedure): void {
    this.selectedProcedure.set(procedure);
    this.showFormModal.set(true);
  }

  closeModal(): void {
    this.showFormModal.set(false);
    this.selectedProcedure.set(null);
  }

  onProcedureSaved(): void {
    this.closeModal();
    this.loadProcedures();
  }

  onDeactivate(procedure: Procedure): void {
    if (confirm(`¿Estás seguro de desactivar el procedimiento "${procedure.name}"?`)) {
      this.procedureService.deleteProcedure(procedure.id).subscribe({
        next: () => {
          // Soft-deactivate locally
          this.allProcedures.update(list =>
            list.map(p => p.id === procedure.id ? { ...p, active: false } : p)
          );
        }
      });
    }
  }
}
