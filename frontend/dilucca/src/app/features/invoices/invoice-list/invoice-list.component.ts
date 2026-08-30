import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Invoice, InvoiceStatus } from '../../../core/models/invoice.model';
import { InvoiceService } from '../../../core/services/invoice.service';
import { PaymentFormComponent } from '../payment-form/payment-form.component';

@Component({
  selector: 'app-invoice-list',
  standalone: true,
  imports: [CommonModule, FormsModule, PaymentFormComponent],
  templateUrl: './invoice-list.component.html',
  styleUrl: './invoice-list.component.css'
})
export class InvoiceListComponent implements OnInit {

  invoices = signal<Invoice[]>([]);
  loading = signal(false);
  showPaymentModal = signal(false);
  selectedInvoice = signal<Invoice | null>(null);

  filterStatus: InvoiceStatus | '' = '';

  constructor(private invoiceService: InvoiceService) {}

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(): void {
    this.loading.set(true);
    this.invoiceService.getInvoices(undefined, (this.filterStatus as InvoiceStatus) || undefined).subscribe({
      next: (data) => {
        this.invoices.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  openPaymentModal(invoice: Invoice): void {
    this.selectedInvoice.set(invoice);
    this.showPaymentModal.set(true);
  }

  closeModal(): void {
    this.showPaymentModal.set(false);
    this.selectedInvoice.set(null);
  }

  onPaymentSaved(): void {
    this.closeModal();
    this.loadInvoices();
  }

  showEditModal = signal(false);
  editInvoiceTarget = signal<Invoice | null>(null);
  editStatus = signal<InvoiceStatus>('PENDING');
  editPaidAmount = signal<number>(0);
  savingEdit = signal(false);

  openEditModal(invoice: Invoice): void {
    this.editInvoiceTarget.set(invoice);
    this.editStatus.set(invoice.status);
    this.editPaidAmount.set(invoice.paidAmount);
    this.showEditModal.set(true);
  }

  closeEditModal(): void {
    this.showEditModal.set(false);
    this.editInvoiceTarget.set(null);
  }

  saveInvoiceUpdate(): void {
    const invoice = this.editInvoiceTarget();
    if (!invoice) return;

    this.savingEdit.set(true);
    this.invoiceService.updateInvoice(invoice.id, this.editStatus(), this.editPaidAmount()).subscribe({
      next: (updatedInvoice) => {
        this.savingEdit.set(false);
        this.closeEditModal();
        alert(`¡Factura ${updatedInvoice.invoiceNumber} actualizada correctamente! El comprobante actualizado ha sido enviado al correo del paciente.`);
        this.loadInvoices();
      },
      error: (err) => {
        this.savingEdit.set(false);
        alert(err.message || 'Error al actualizar la factura');
      }
    });
  }

  sendEmail(invoice: Invoice): void {
    this.invoiceService.sendInvoiceEmail(invoice.id).subscribe({
      next: () => {
        alert(`¡Comprobante HTML reenviado con éxito al correo del paciente para la factura ${invoice.invoiceNumber}!`);
      }
    });
  }

  getStatusBadgeClass(status: InvoiceStatus): string {
    const classes: Record<InvoiceStatus, string> = {
      PENDING: 'badge badge-warning',
      PARTIALLY_PAID: 'badge badge-info',
      PAID: 'badge badge-success',
      CANCELLED: 'badge badge-danger'
    };
    return classes[status] || 'badge';
  }

  getStatusText(status: InvoiceStatus): string {
    const text: Record<InvoiceStatus, string> = {
      PENDING: 'PENDIENTE',
      PARTIALLY_PAID: 'PAGO PARCIAL',
      PAID: 'PAGADA',
      CANCELLED: 'CANCELADA'
    };
    return text[status] || status;
  }
}
