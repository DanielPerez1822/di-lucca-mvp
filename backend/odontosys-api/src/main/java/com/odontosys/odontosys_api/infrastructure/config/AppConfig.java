package com.odontosys.odontosys_api.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.odontosys.odontosys_api.application.appointment.CancelAppointmentService;
import com.odontosys.odontosys_api.application.appointment.CreateAppointmentService;
import com.odontosys.odontosys_api.application.appointment.GetAppointmentByIdService;
import com.odontosys.odontosys_api.application.appointment.ListAppointmentsService;
import com.odontosys.odontosys_api.application.appointment.RescheduleAppointmentService;
import com.odontosys.odontosys_api.application.auth.CreateUserService;
import com.odontosys.odontosys_api.application.auth.GetCurrentUserService;
import com.odontosys.odontosys_api.application.auth.LoginService;
import com.odontosys.odontosys_api.application.auth.LogoutService;
import com.odontosys.odontosys_api.application.auth.RefreshTokenService;
import com.odontosys.odontosys_api.application.auth.RequestPasswordResetService;
import com.odontosys.odontosys_api.application.auth.ResetPasswordService;
import com.odontosys.odontosys_api.application.invoice.CreateInvoiceService;
import com.odontosys.odontosys_api.application.invoice.GetInvoiceByIdService;
import com.odontosys.odontosys_api.application.invoice.ListInvoicesService;
import com.odontosys.odontosys_api.application.invoice.RegisterPaymentService;
import com.odontosys.odontosys_api.application.invoice.SendInvoiceEmailService;
import com.odontosys.odontosys_api.application.invoice.UpdateInvoiceService;
import com.odontosys.odontosys_api.application.medicalrecord.CreateMedicalRecordService;
import com.odontosys.odontosys_api.application.medicalrecord.GetMedicalRecordByIdService;
import com.odontosys.odontosys_api.application.medicalrecord.GetPatientMedicalHistoryService;
import com.odontosys.odontosys_api.application.medicalrecord.UpdateMedicalRecordService;
import com.odontosys.odontosys_api.application.patient.CreatePatientService;
import com.odontosys.odontosys_api.application.patient.DeletePatientService;
import com.odontosys.odontosys_api.application.patient.GetPatientByIdService;
import com.odontosys.odontosys_api.application.patient.ListPatientsService;
import com.odontosys.odontosys_api.application.patient.UpdatePatientService;
import com.odontosys.odontosys_api.application.procedure.CreateProcedureService;
import com.odontosys.odontosys_api.application.procedure.DeleteProcedureService;
import com.odontosys.odontosys_api.application.procedure.GetProcedureByIdService;
import com.odontosys.odontosys_api.application.procedure.ListProceduresService;
import com.odontosys.odontosys_api.application.procedure.UpdateProcedureService;
import com.odontosys.odontosys_api.application.schedule.GenerateSlotsService;
import com.odontosys.odontosys_api.application.schedule.GetAvailableSlotsService;
import com.odontosys.odontosys_api.application.schedule.GetDentistScheduleService;
import com.odontosys.odontosys_api.application.schedule.SetDentistScheduleService;
import com.odontosys.odontosys_api.domain.port.in.appointment.CancelAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.CreateAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.GetAppointmentByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.ListAppointmentsUseCase;
import com.odontosys.odontosys_api.domain.port.in.appointment.RescheduleAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.CreateUserUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.GetCurrentUserUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.LoginUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.LogoutUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.RefreshTokenUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.RequestPasswordResetUseCase;
import com.odontosys.odontosys_api.domain.port.in.auth.ResetPasswordUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.CreateInvoiceFromMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.GetInvoiceByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.ListInvoicesUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.RegisterPaymentUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.SendInvoiceEmailUseCase;
import com.odontosys.odontosys_api.domain.port.in.invoice.UpdateInvoiceUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.CreateMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.GetMedicalRecordByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.GetPatientMedicalHistoryUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.UpdateMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.CreatePatientUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.DeletePatientUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.GetPatientByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.ListPatientsUseCase;
import com.odontosys.odontosys_api.domain.port.in.patient.UpdatePatientUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.CreateProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.DeleteProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.GetProcedureByIdUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.ListProceduresUseCase;
import com.odontosys.odontosys_api.domain.port.in.procedure.UpdateProcedureUseCase;
import com.odontosys.odontosys_api.domain.port.in.schedule.GenerateSlotsUseCase;
import com.odontosys.odontosys_api.domain.port.in.schedule.GetAvailableSlotsUseCase;
import com.odontosys.odontosys_api.domain.port.in.schedule.GetDentistScheduleUseCase;
import com.odontosys.odontosys_api.domain.port.in.schedule.SetDentistScheduleUseCase;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.AvailabilitySlotRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.DentistScheduleRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import com.odontosys.odontosys_api.domain.port.out.InvoiceRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.MedicalRecordRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PasswordEncoderPort;
import com.odontosys.odontosys_api.domain.port.out.PasswordResetTokenRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PaymentRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.RefreshTokenRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.TokenProviderPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

@Configuration
public class AppConfig {

    @Bean
    public LoginUseCase loginUseCase(
            UserRepositoryPort userRepository,
            RefreshTokenRepositoryPort refreshTokenRepository,
            PasswordEncoderPort passwordEncoder,
            TokenProviderPort tokenProvider,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationMs
    ) {
        return new LoginService(userRepository, refreshTokenRepository, passwordEncoder, tokenProvider, refreshTokenExpirationMs);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder
    ) {
        return new CreateUserService(userRepository, passwordEncoder);
    }

    @Bean
    public RefreshTokenUseCase refreshTokenUseCase(
            RefreshTokenRepositoryPort refreshTokenRepository,
            UserRepositoryPort userRepository,
            TokenProviderPort tokenProvider,
            @Value("${jwt.refresh-token-expiration}") long refreshTokenExpirationMs
    ) {
        return new RefreshTokenService(refreshTokenRepository, userRepository, tokenProvider, refreshTokenExpirationMs);
    }

    @Bean
    public LogoutUseCase logoutUseCase(RefreshTokenRepositoryPort refreshTokenRepository) {
        return new LogoutService(refreshTokenRepository);
    }

    @Bean
    public GetCurrentUserUseCase getCurrentUserUseCase(UserRepositoryPort userRepository) {
        return new GetCurrentUserService(userRepository);
    }

    @Bean
    public RequestPasswordResetUseCase requestPasswordResetUseCase(
            UserRepositoryPort userRepository,
            PasswordResetTokenRepositoryPort resetTokenRepository,
            EmailSenderPort emailSender,
            @Value("${password.reset.code-expiration-minutes:15}") int codeExpirationMinutes
    ) {
        return new RequestPasswordResetService(userRepository, resetTokenRepository, emailSender, codeExpirationMinutes);
    }

    @Bean
    public ResetPasswordUseCase resetPasswordUseCase(
            UserRepositoryPort userRepository,
            PasswordResetTokenRepositoryPort resetTokenRepository,
            RefreshTokenRepositoryPort refreshTokenRepository,
            PasswordEncoderPort passwordEncoder
    ) {
        return new ResetPasswordService(userRepository, resetTokenRepository, refreshTokenRepository, passwordEncoder);
    }

    // ─── Patient Use Cases ────────────────────────────────────

    @Bean
    public CreatePatientUseCase createPatientUseCase(PatientRepositoryPort patientRepository) {
        return new CreatePatientService(patientRepository);
    }

    @Bean
    public UpdatePatientUseCase updatePatientUseCase(PatientRepositoryPort patientRepository) {
        return new UpdatePatientService(patientRepository);
    }

    @Bean
    public GetPatientByIdUseCase getPatientByIdUseCase(PatientRepositoryPort patientRepository) {
        return new GetPatientByIdService(patientRepository);
    }

    @Bean
    public ListPatientsUseCase listPatientsUseCase(PatientRepositoryPort patientRepository) {
        return new ListPatientsService(patientRepository);
    }

    @Bean
    public DeletePatientUseCase deletePatientUseCase(PatientRepositoryPort patientRepository) {
        return new DeletePatientService(patientRepository);
    }

    // ─── Procedure Use Cases ──────────────────────────────────

    @Bean
    public CreateProcedureUseCase createProcedureUseCase(ProcedureRepositoryPort procedureRepository) {
        return new CreateProcedureService(procedureRepository);
    }

    @Bean
    public UpdateProcedureUseCase updateProcedureUseCase(ProcedureRepositoryPort procedureRepository) {
        return new UpdateProcedureService(procedureRepository);
    }

    @Bean
    public GetProcedureByIdUseCase getProcedureByIdUseCase(ProcedureRepositoryPort procedureRepository) {
        return new GetProcedureByIdService(procedureRepository);
    }

    @Bean
    public ListProceduresUseCase listProceduresUseCase(ProcedureRepositoryPort procedureRepository) {
        return new ListProceduresService(procedureRepository);
    }

    @Bean
    public DeleteProcedureUseCase deleteProcedureUseCase(ProcedureRepositoryPort procedureRepository) {
        return new DeleteProcedureService(procedureRepository);
    }

    // ─── Schedule Use Cases ───────────────────────────────────

    @Bean
    public SetDentistScheduleUseCase setDentistScheduleUseCase(DentistScheduleRepositoryPort scheduleRepository, UserRepositoryPort userRepository) {
        return new SetDentistScheduleService(scheduleRepository, userRepository);
    }

    @Bean
    public GetDentistScheduleUseCase getDentistScheduleUseCase(DentistScheduleRepositoryPort scheduleRepository) {
        return new GetDentistScheduleService(scheduleRepository);
    }

    @Bean
    public GenerateSlotsUseCase generateSlotsUseCase(DentistScheduleRepositoryPort scheduleRepository, AvailabilitySlotRepositoryPort slotRepository) {
        return new GenerateSlotsService(scheduleRepository, slotRepository);
    }

    @Bean
    public GetAvailableSlotsUseCase getAvailableSlotsUseCase(AvailabilitySlotRepositoryPort slotRepository) {
        return new GetAvailableSlotsService(slotRepository);
    }

    // ─── Appointment Use Cases ────────────────────────────────

    @Bean
    public CreateAppointmentUseCase createAppointmentUseCase(
            AppointmentRepositoryPort appointmentRepository,
            AvailabilitySlotRepositoryPort slotRepository,
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            EmailSenderPort emailSender
    ) {
        return new CreateAppointmentService(appointmentRepository, slotRepository, patientRepository, userRepository, emailSender);
    }

    @Bean
    public RescheduleAppointmentUseCase rescheduleAppointmentUseCase(
            AppointmentRepositoryPort appointmentRepository,
            AvailabilitySlotRepositoryPort slotRepository,
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            EmailSenderPort emailSender
    ) {
        return new RescheduleAppointmentService(appointmentRepository, slotRepository, patientRepository, userRepository, emailSender);
    }

    @Bean
    public CancelAppointmentUseCase cancelAppointmentUseCase(
            AppointmentRepositoryPort appointmentRepository,
            AvailabilitySlotRepositoryPort slotRepository,
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            EmailSenderPort emailSender
    ) {
        return new CancelAppointmentService(appointmentRepository, slotRepository, patientRepository, userRepository, emailSender);
    }

    @Bean
    public GetAppointmentByIdUseCase getAppointmentByIdUseCase(AppointmentRepositoryPort appointmentRepository) {
        return new GetAppointmentByIdService(appointmentRepository);
    }

    @Bean
    public ListAppointmentsUseCase listAppointmentsUseCase(AppointmentRepositoryPort appointmentRepository) {
        return new ListAppointmentsService(appointmentRepository);
    }

    // ─── MedicalRecord Use Cases ──────────────────────────────

    @Bean
    public CreateMedicalRecordUseCase createMedicalRecordUseCase(
            MedicalRecordRepositoryPort medicalRecordRepository,
            PatientRepositoryPort patientRepository,
            UserRepositoryPort userRepository,
            ProcedureRepositoryPort procedureRepository,
            AppointmentRepositoryPort appointmentRepository,
            CreateInvoiceFromMedicalRecordUseCase createInvoiceUseCase
    ) {
        return new CreateMedicalRecordService(medicalRecordRepository, patientRepository, userRepository, procedureRepository, appointmentRepository, createInvoiceUseCase);
    }

    @Bean
    public GetMedicalRecordByIdUseCase getMedicalRecordByIdUseCase(MedicalRecordRepositoryPort medicalRecordRepository) {
        return new GetMedicalRecordByIdService(medicalRecordRepository);
    }

    @Bean
    public GetPatientMedicalHistoryUseCase getPatientMedicalHistoryUseCase(
            MedicalRecordRepositoryPort medicalRecordRepository,
            PatientRepositoryPort patientRepository
    ) {
        return new GetPatientMedicalHistoryService(medicalRecordRepository, patientRepository);
    }

    @Bean
    public UpdateMedicalRecordUseCase updateMedicalRecordUseCase(
            MedicalRecordRepositoryPort medicalRecordRepository,
            ProcedureRepositoryPort procedureRepository
    ) {
        return new UpdateMedicalRecordService(medicalRecordRepository, procedureRepository);
    }

    // ─── Invoice & Payment Use Cases ──────────────────────────

    @Bean
    public CreateInvoiceFromMedicalRecordUseCase createInvoiceFromMedicalRecordUseCase(
            InvoiceRepositoryPort invoiceRepository,
            MedicalRecordRepositoryPort medicalRecordRepository
    ) {
        return new CreateInvoiceService(invoiceRepository, medicalRecordRepository);
    }

    @Bean
    public RegisterPaymentUseCase registerPaymentUseCase(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository,
            PatientRepositoryPort patientRepository,
            EmailSenderPort emailSender
    ) {
        return new RegisterPaymentService(invoiceRepository, paymentRepository, patientRepository, emailSender);
    }

    @Bean
    public GetInvoiceByIdUseCase getInvoiceByIdUseCase(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository
    ) {
        return new GetInvoiceByIdService(invoiceRepository, paymentRepository);
    }

    @Bean
    public ListInvoicesUseCase listInvoicesUseCase(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository
    ) {
        return new ListInvoicesService(invoiceRepository, paymentRepository);
    }

    @Bean
    public SendInvoiceEmailUseCase sendInvoiceEmailUseCase(
            InvoiceRepositoryPort invoiceRepository,
            PatientRepositoryPort patientRepository,
            PaymentRepositoryPort paymentRepository,
            EmailSenderPort emailSender
    ) {
        return new SendInvoiceEmailService(invoiceRepository, patientRepository, paymentRepository, emailSender);
    }

    @Bean
    public UpdateInvoiceUseCase updateInvoiceUseCase(
            InvoiceRepositoryPort invoiceRepository,
            PaymentRepositoryPort paymentRepository,
            SendInvoiceEmailUseCase sendInvoiceEmailUseCase
    ) {
        return new UpdateInvoiceService(invoiceRepository, paymentRepository, sendInvoiceEmailUseCase);
    }
}
