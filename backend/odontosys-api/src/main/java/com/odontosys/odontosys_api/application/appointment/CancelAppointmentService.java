package com.odontosys.odontosys_api.application.appointment;

import java.util.UUID;
import com.odontosys.odontosys_api.domain.exception.AppointmentNotFoundException;
import com.odontosys.odontosys_api.domain.model.Appointment;
import com.odontosys.odontosys_api.domain.port.in.appointment.CancelAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.AvailabilitySlotRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class CancelAppointmentService implements CancelAppointmentUseCase {

    private final AppointmentRepositoryPort appointmentRepository;
    private final AvailabilitySlotRepositoryPort slotRepository;
    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final EmailSenderPort emailSender;

    public CancelAppointmentService(AppointmentRepositoryPort appointmentRepository,
                                    AvailabilitySlotRepositoryPort slotRepository,
                                    PatientRepositoryPort patientRepository,
                                    UserRepositoryPort userRepository,
                                    EmailSenderPort emailSender) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.emailSender = emailSender;
    }

    @Override
    public void execute(UUID appointmentId, String cancelReason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con ID: " + appointmentId));

        // Release associated slot
        slotRepository.findById(appointment.getSlotId()).ifPresent(slot -> {
            slot.markAsAvailable();
            slotRepository.save(slot);
        });

        // Cancel appointment
        appointment.cancel(cancelReason);
        appointmentRepository.save(appointment);

        // Send email notification to patient
        patientRepository.findById(appointment.getPatientId()).ifPresent(patient -> {
            if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
                String dentistName = userRepository.findById(appointment.getDentistId())
                        .map(u -> "Dr. " + u.getFirstName() + " " + u.getLastName())
                        .orElse("su odontólogo");
                String patientName = patient.getFirstName() + " " + patient.getLastName();

                emailSender.sendAppointmentCancelledNotification(
                        patient.getEmail(),
                        patientName,
                        dentistName,
                        appointment.getAppointmentDate(),
                        appointment.getStartTime(),
                        cancelReason
                );
            }
        });
    }
}
