import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Invoice } from '../../../core/models/invoice.model';
import { InvoiceService } from '../../../core/services/invoice.service';

@Component({
  selector: 'app-payment-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './payment-form.component.html',
  styleUrl: './payment-form.component.css'
})
export class PaymentFormComponent implements OnInit {

  @Input() invoice: Invoice | null = null;
  @Output() saved = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  paymentForm!: FormGroup;
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit(): void {
    const pendingBalance = this.invoice ? (this.invoice.totalAmount - this.invoice.paidAmount) : 0;

    this.paymentForm = this.fb.group({
      amount: [pendingBalance > 0 ? pendingBalance : 0, [Validators.required, Validators.min(1)]],
      paymentMethod: ['CASH', [Validators.required]],
      referenceNumber: [''],
      notes: ['']
    });
  }

  onClose(): void {
    this.closed.emit();
  }

  onSubmit(): void {
    if (this.paymentForm.invalid || !this.invoice) {
      this.paymentForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    this.invoiceService.registerPayment(this.invoice.id, this.paymentForm.value).subscribe({
      next: () => {
        this.loading.set(false);
        this.saved.emit();
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.message || 'Error al registrar el pago');
      }
    });
  }
}
