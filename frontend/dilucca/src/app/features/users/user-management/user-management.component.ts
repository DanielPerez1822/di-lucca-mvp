import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserManagementService, CreateUserPayload, UpdateUserPayload } from '../../../core/services/user-management.service';
import { Role, User } from '../../../core/models/user.model';

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  users = signal<User[]>([]);
  loading = signal<boolean>(false);
  errorMessage = signal<string | null>(null);
  successMessage = signal<string | null>(null);

  // Filters
  searchTerm = signal<string>('');
  selectedRoleFilter = signal<string>('ALL');

  // Modal State
  showModal = signal<boolean>(false);
  isEditing = signal<boolean>(false);
  editingUserId = signal<string | null>(null);

  // Form Fields
  formFirstName = signal<string>('');
  formLastName = signal<string>('');
  formEmail = signal<string>('');
  formPassword = signal<string>('');
  formPhone = signal<string>('');
  formDocumentNumber = signal<string>('');
  formRole = signal<Role>('DENTIST');
  formActive = signal<boolean>(true);

  filteredUsers = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    const roleFilter = this.selectedRoleFilter();

    return this.users().filter(u => {
      const fullName = `${u.firstName} ${u.lastName}`.toLowerCase();
      const email = u.email.toLowerCase();
      const doc = (u.documentNumber || '').toLowerCase();

      const matchesSearch = !term || fullName.includes(term) || email.includes(term) || doc.includes(term);
      const matchesRole = roleFilter === 'ALL' || u.roles.includes(roleFilter as Role);

      return matchesSearch && matchesRole;
    });
  });

  constructor(private userManagementService: UserManagementService) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.userManagementService.getUsers().subscribe({
      next: (list) => {
        this.users.set(list);
        this.loading.set(false);
      },
      error: (err) => {
        this.errorMessage.set(err.error?.message || 'Error al cargar la lista de usuarios');
        this.loading.set(false);
      }
    });
  }

  openCreateModal(): void {
    this.isEditing.set(false);
    this.editingUserId.set(null);
    this.formFirstName.set('');
    this.formLastName.set('');
    this.formEmail.set('');
    this.formPassword.set('');
    this.formPhone.set('');
    this.formDocumentNumber.set('');
    this.formRole.set('DENTIST');
    this.formActive.set(true);
    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  openEditModal(user: User): void {
    this.isEditing.set(true);
    this.editingUserId.set(user.id);
    this.formFirstName.set(user.firstName);
    this.formLastName.set(user.lastName);
    this.formEmail.set(user.email);
    this.formPassword.set('');
    this.formPhone.set(user.phone || '');
    this.formDocumentNumber.set(user.documentNumber || '');

    // Set non-admin role if possible
    const mainRole = user.roles.find(r => r !== 'ADMIN') || 'DENTIST';
    this.formRole.set(mainRole as Role);
    this.formActive.set(user.active);

    this.errorMessage.set(null);
    this.showModal.set(true);
  }

  closeModal(): void {
    this.showModal.set(false);
  }

  saveUser(): void {
    this.errorMessage.set(null);
    this.successMessage.set(null);

    if (!this.formFirstName() || !this.formLastName()) {
      this.errorMessage.set('Nombre y Apellido son obligatorios');
      return;
    }

    if (!this.isEditing()) {
      if (!this.formEmail()) {
        this.errorMessage.set('El correo electrónico es obligatorio');
        return;
      }
      if (!this.formPassword() || this.formPassword().length < 8) {
        this.errorMessage.set('La contraseña debe tener al menos 8 caracteres');
        return;
      }
    }

    if (this.formRole() === 'ADMIN' as any) {
      this.errorMessage.set('No se puede asignar el rol Administrador');
      return;
    }

    if (this.isEditing() && this.editingUserId()) {
      const payload: UpdateUserPayload = {
        firstName: this.formFirstName(),
        lastName: this.formLastName(),
        phone: this.formPhone(),
        documentNumber: this.formDocumentNumber(),
        roles: [this.formRole()],
        active: this.formActive()
      };

      this.userManagementService.updateUser(this.editingUserId()!, payload).subscribe({
        next: () => {
          this.successMessage.set('Usuario actualizado exitosamente');
          this.closeModal();
          this.loadUsers();
          setTimeout(() => this.successMessage.set(null), 4000);
        },
        error: (err) => {
          this.errorMessage.set(err.error?.message || 'Error al actualizar el usuario');
        }
      });
    } else {
      const payload: CreateUserPayload = {
        email: this.formEmail(),
        password: this.formPassword(),
        firstName: this.formFirstName(),
        lastName: this.formLastName(),
        phone: this.formPhone(),
        documentNumber: this.formDocumentNumber(),
        roles: [this.formRole()]
      };

      this.userManagementService.createUser(payload).subscribe({
        next: () => {
          this.successMessage.set('Usuario creado exitosamente');
          this.closeModal();
          this.loadUsers();
          setTimeout(() => this.successMessage.set(null), 4000);
        },
        error: (err) => {
          this.errorMessage.set(err.error?.message || 'Error al crear el usuario');
        }
      });
    }
  }

  toggleUserStatus(user: User): void {
    if (user.roles.includes('ADMIN')) {
      alert('No se puede desactivar la cuenta del Administrador Principal');
      return;
    }

    const action = user.active ? 'desactivar' : 'activar';
    if (!confirm(`¿Está seguro que desea ${action} la cuenta de ${user.firstName} ${user.lastName}?`)) {
      return;
    }

    if (user.active) {
      this.userManagementService.deactivateUser(user.id).subscribe({
        next: () => {
          this.successMessage.set(`Usuario ${user.firstName} ${user.lastName} desactivado`);
          this.loadUsers();
          setTimeout(() => this.successMessage.set(null), 4000);
        },
        error: (err) => alert(err.error?.message || 'Error al cambiar estado')
      });
    } else {
      const payload: UpdateUserPayload = {
        firstName: user.firstName,
        lastName: user.lastName,
        phone: user.phone,
        documentNumber: user.documentNumber,
        roles: user.roles,
        active: true
      };
      this.userManagementService.updateUser(user.id, payload).subscribe({
        next: () => {
          this.successMessage.set(`Usuario ${user.firstName} ${user.lastName} activado`);
          this.loadUsers();
          setTimeout(() => this.successMessage.set(null), 4000);
        },
        error: (err) => alert(err.error?.message || 'Error al activar usuario')
      });
    }
  }

  getRoleLabel(role: Role): string {
    switch (role) {
      case 'ADMIN': return 'Administrador';
      case 'DENTIST': return 'Odontólogo';
      case 'SECRETARY_ASSISTANT': return 'Asistente / Secretaria';
      default: return role;
    }
  }

  getRoleBadgeClass(role: Role): string {
    switch (role) {
      case 'ADMIN': return 'badge-role-admin';
      case 'DENTIST': return 'badge-role-dentist';
      case 'SECRETARY_ASSISTANT': return 'badge-role-assistant';
      default: return 'badge-role-default';
    }
  }
}
