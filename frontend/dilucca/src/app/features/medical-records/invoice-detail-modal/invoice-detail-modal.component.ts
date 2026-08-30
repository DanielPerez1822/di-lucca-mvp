import { Component, Input, Output, EventEmitter, OnInit, signal } from '@angular/core';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { Invoice } from '../../../core/models/invoice.model';
import { Patient } from '../../../core/models/patient.model';
import { User } from '../../../core/models/user.model';
import { InvoiceService } from '../../../core/services/invoice.service';

@Component({
  selector: 'app-invoice-detail-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './invoice-detail-modal.component.html',
  styleUrl: './invoice-detail-modal.component.css'
})
export class InvoiceDetailModalComponent implements OnInit {

  @Input() medicalRecordId!: string;
  @Input() patient!: Patient;
  @Input() dentistName!: string;
  @Output() closed = new EventEmitter<void>();

  invoice = signal<Invoice | null>(null);
  loading = signal(true);
  sendingEmail = signal(false);
  emailSent = signal(false);
  error = signal<string | null>(null);

  constructor(private invoiceService: InvoiceService) {}

  ngOnInit(): void {
    this.loadInvoice();
  }

  loadInvoice(): void {
    this.loading.set(true);
    this.error.set(null);

    // Try to create (or get existing) invoice from medical record
    this.invoiceService.createInvoiceFromMedicalRecord(this.medicalRecordId).subscribe({
      next: (inv) => {
        this.invoice.set(inv);
        this.loading.set(false);
      },
      error: (err) => {
        // If invoice already exists, try to fetch by medicalRecordId via getInvoices
        this.invoiceService.getInvoices().subscribe({
          next: (invoices) => {
            const found = invoices.find(i => i.medicalRecordId === this.medicalRecordId);
            if (found) {
              this.invoice.set(found);
            } else {
              this.error.set('No se pudo cargar la factura. Intenta nuevamente.');
            }
            this.loading.set(false);
          },
          error: () => {
            this.error.set('Error al cargar la factura.');
            this.loading.set(false);
          }
        });
      }
    });
  }

  statusLabel(status: string): string {
    const labels: Record<string, string> = {
      PENDING: 'Pendiente',
      PARTIALLY_PAID: 'Parcialmente Pagada',
      PAID: 'Pagada',
      CANCELLED: 'Cancelada'
    };
    return labels[status] ?? status;
  }

  sendEmail(): void {
    const inv = this.invoice();
    if (!inv) return;
    this.sendingEmail.set(true);
    this.invoiceService.sendInvoiceEmail(inv.id).subscribe({
      next: () => {
        this.sendingEmail.set(false);
        this.emailSent.set(true);
        setTimeout(() => this.emailSent.set(false), 4000);
      },
      error: () => {
        this.sendingEmail.set(false);
        alert('No se pudo enviar el correo. Verifica el email del paciente.');
      }
    });
  }

  downloadPdf(): void {
    const inv = this.invoice();
    if (!inv) return;

    const patientFullName = `${this.patient.firstName} ${this.patient.lastName}`;
    const issueDate = new Date(inv.issueDate).toLocaleDateString('es-CO', {
      year: 'numeric', month: 'long', day: 'numeric'
    });

    const itemRows = inv.items.map(item => `
      <tr>
        <td>${item.description}</td>
        <td style="text-align:center">${item.quantity}</td>
        <td style="text-align:right">$${item.unitPrice.toLocaleString('es-CO')} COP</td>
        <td style="text-align:right">$${item.lineTotal.toLocaleString('es-CO')} COP</td>
      </tr>
    `).join('');

    const html = `
      <!DOCTYPE html>
      <html lang="es">
      <head>
        <meta charset="UTF-8"/>
        <title>Factura ${inv.invoiceNumber} — DI-LUCCA</title>
        <style>
          * { margin: 0; padding: 0; box-sizing: border-box; }
          body { font-family: Arial, sans-serif; color: #1e293b; padding: 40px; font-size: 13px; }
          .header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 32px; border-bottom: 3px solid #1EA296; padding-bottom: 20px; }
          .clinic-name { font-size: 22px; font-weight: bold; color: #1EA296; }
          .clinic-sub { font-size: 11px; color: #64748b; margin-top: 4px; }
          .invoice-info { text-align: right; }
          .invoice-num { font-size: 18px; font-weight: bold; color: #1e293b; }
          .badge { display: inline-block; padding: 3px 10px; border-radius: 99px; font-size: 11px; font-weight: 600; margin-top: 6px; }
          .PENDING { background: #fdf3dc; color: #92600a; }
          .PAID { background: #e3f3e6; color: #0f7b3a; }
          .PARTIALLY_PAID { background: #d9efec; color: #12756c; }
          .CANCELLED { background: #fee9e7; color: #b34033; }
          .parties { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-bottom: 28px; }
          .party-box { background: #f8fafc; border: 1px solid #e2e8f0; border-radius: 8px; padding: 14px; }
          .party-box h4 { font-size: 10px; text-transform: uppercase; letter-spacing: 1px; color: #64748b; margin-bottom: 8px; }
          .party-box p { font-size: 13px; font-weight: 600; margin-bottom: 2px; }
          .party-box span { font-size: 12px; color: #64748b; }
          table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
          thead { background: #1EA296; color: white; }
          th { padding: 10px 12px; text-align: left; font-size: 11px; text-transform: uppercase; letter-spacing: 0.5px; }
          td { padding: 9px 12px; border-bottom: 1px solid #f1f5f9; }
          tr:last-child td { border-bottom: none; }
          .totals { margin-left: auto; width: 260px; }
          .totals-row { display: flex; justify-content: space-between; padding: 6px 0; font-size: 13px; }
          .totals-row.total { border-top: 2px solid #1EA296; font-weight: bold; font-size: 16px; color: #12756C; padding-top: 10px; margin-top: 4px; }
          .footer { margin-top: 40px; text-align: center; font-size: 11px; color: #94a3b8; border-top: 1px solid #e2e8f0; padding-top: 16px; }
        </style>
      </head>
      <body>
        <div class="header">
          <div>
            <div class="clinic-name">🦷 Consultorio DI-LUCCA</div>
            <div class="clinic-sub">Odontología Integral • Bogotá, Colombia</div>
            <div class="clinic-sub">Tel: +57 300 000 0000 • info@dilucca.com</div>
          </div>
          <div class="invoice-info">
            <div class="invoice-num">Factura N° ${inv.invoiceNumber}</div>
            <div style="margin-top:4px; font-size:12px; color:#64748b">Fecha: ${issueDate}</div>
            <span class="badge ${inv.status}">${this.statusLabel(inv.status)}</span>
          </div>
        </div>

        <div class="parties">
          <div class="party-box">
            <h4>Paciente</h4>
            <p>${patientFullName}</p>
            <span>${this.patient.documentType}: ${this.patient.documentNumber}</span><br/>
            ${this.patient.phone ? `<span>Tel: ${this.patient.phone}</span><br/>` : ''}
            ${this.patient.email ? `<span>${this.patient.email}</span>` : ''}
          </div>
          <div class="party-box">
            <h4>Atendido por</h4>
            <p>${this.dentistName}</p>
            <span>Odontólogo — DI-LUCCA</span>
          </div>
        </div>

        <table>
          <thead>
            <tr>
              <th>Procedimiento / Descripción</th>
              <th style="text-align:center">Cant.</th>
              <th style="text-align:right">Precio Unit.</th>
              <th style="text-align:right">Total</th>
            </tr>
          </thead>
          <tbody>
            ${itemRows || '<tr><td colspan="4" style="text-align:center;color:#94a3b8">Sin ítems registrados</td></tr>'}
          </tbody>
        </table>

        <div class="totals">
          <div class="totals-row"><span>Subtotal</span><span>$${inv.subtotal.toLocaleString('es-CO')} COP</span></div>
          ${inv.taxAmount ? `<div class="totals-row"><span>IVA</span><span>$${inv.taxAmount.toLocaleString('es-CO')} COP</span></div>` : ''}
          ${inv.discountAmount ? `<div class="totals-row"><span>Descuento</span><span>-$${inv.discountAmount.toLocaleString('es-CO')} COP</span></div>` : ''}
          <div class="totals-row total"><span>TOTAL</span><span>$${inv.totalAmount.toLocaleString('es-CO')} COP</span></div>
          <div class="totals-row" style="color:#0f7b3a"><span>Pagado</span><span>$${inv.paidAmount.toLocaleString('es-CO')} COP</span></div>
          <div class="totals-row" style="color:#b34033"><span>Saldo pendiente</span><span>$${(inv.totalAmount - inv.paidAmount).toLocaleString('es-CO')} COP</span></div>
        </div>

        <div class="footer">
          Gracias por confiar en Consultorio DI-LUCCA • Este documento es generado electrónicamente y no requiere firma física.
        </div>
      </body>
      </html>
    `;

    const iframe = document.createElement('iframe');
    iframe.style.position = 'fixed';
    iframe.style.width = '0';
    iframe.style.height = '0';
    iframe.style.border = 'none';
    iframe.style.opacity = '0';
    document.body.appendChild(iframe);

    const doc = iframe.contentWindow?.document;
    if (!doc) { document.body.removeChild(iframe); return; }
    doc.open();
    doc.write(html);
    doc.close();

    iframe.contentWindow?.focus();
    setTimeout(() => {
      iframe.contentWindow?.print();
      setTimeout(() => document.body.removeChild(iframe), 2000);
    }, 500);
  }

  close(): void {
    this.closed.emit();
  }
}
