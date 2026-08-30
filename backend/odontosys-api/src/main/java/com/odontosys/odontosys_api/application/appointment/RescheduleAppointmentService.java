package com.odontosys.odontosys_api.application.appointment;

import java.util.UUID;
import com.odontosys.odontosys_api.application.appointment.command.RescheduleAppointmentCommand;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;
import com.odontosys.odontosys_api.domain.exception.AppointmentNotFoundException;
import com.odontosys.odontosys_api.domain.exception.SlotNotAvailableException;
import com.odontosys.odontosys_api.domain.model.Appointment;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.domain.model.Patient;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.appointment.RescheduleAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.AvailabilitySlotRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class RescheduleAppointmentService implements RescheduleAppointmentUseCase {

    private final AppointmentRepositoryPort appointmentRepository;
    private final AvailabilitySlotRepositoryPort slotRepository;
    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final EmailSenderPort emailSender;

    public RescheduleAppointmentService(AppointmentRepositoryPort appointmentRepository,
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
    public AppointmentResponse execute(UUID appointmentId, RescheduleAppointmentCommand command) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Cita no encontrada con ID: " + appointmentId));

        AvailabilitySlot newSlot = slotRepository.findById(command.newSlotId())
                .orElseThrow(() -> new SlotNotAvailableException("El nuevo slot no existe con ID: " + command.newSlotId()));

        if (!newSlot.isAvailable()) {
            throw new SlotNotAvailableException("El nuevo slot seleccionado no está disponible");
        }

        // Release old slot
        slotRepository.findById(appointment.getSlotId()).ifPresent(oldSlot -> {
            oldSlot.markAsAvailable();
            slotRepository.save(oldSlot);
        });

        // Book new slot
        newSlot.markAsBooked();
        slotRepository.save(newSlot);

        // Update appointment
        appointment.reschedule(newSlot.getId(), newSlot.getDate(), newSlot.getStartTime(), newSlot.getEndTime());
        Appointment updated = appointmentRepository.save(appointment);

        // Send email notification to patient
        patientRepository.findById(appointment.getPatientId()).ifPresent(patient -> {
            if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
                String dentistName = userRepository.findById(appointment.getDentistId())
                        .map(u -> "Dr. " + u.getFirstName() + " " + u.getLastName())
                        .orElse("su odontólogo");
                String patientName = patient.getFirstName() + " " + patient.getLastName();

                emailSender.sendAppointmentRescheduledNotification(
                        patient.getEmail(),
                        patientName,
                        dentistName,
                        newSlot.getDate(),
                        newSlot.getStartTime()
                );
            }
        });

        return AppointmentResponse.fromDomain(updated);
    }
}
