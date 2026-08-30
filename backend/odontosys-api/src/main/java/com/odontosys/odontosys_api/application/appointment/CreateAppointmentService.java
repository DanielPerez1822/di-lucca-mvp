package com.odontosys.odontosys_api.application.appointment;

import com.odontosys.odontosys_api.application.appointment.command.CreateAppointmentCommand;
import com.odontosys.odontosys_api.application.appointment.response.AppointmentResponse;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.exception.SlotNotAvailableException;
import com.odontosys.odontosys_api.domain.exception.UserNotFoundException;
import com.odontosys.odontosys_api.domain.model.Appointment;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.domain.model.Patient;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.appointment.CreateAppointmentUseCase;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.AvailabilitySlotRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.EmailSenderPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class CreateAppointmentService implements CreateAppointmentUseCase {

    private final AppointmentRepositoryPort appointmentRepository;
    private final AvailabilitySlotRepositoryPort slotRepository;
    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final EmailSenderPort emailSender;

    public CreateAppointmentService(AppointmentRepositoryPort appointmentRepository,
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
    public AppointmentResponse execute(CreateAppointmentCommand command) {
        Patient patient = patientRepository.findById(command.patientId())
                .orElseThrow(() -> new PatientNotFoundException("Paciente no encontrado con ID: " + command.patientId()));

        User dentist = userRepository.findById(command.dentistId())
                .orElseThrow(() -> new UserNotFoundException("Dentista no encontrado con ID: " + command.dentistId()));

        AvailabilitySlot slot = slotRepository.findById(command.slotId())
                .orElseThrow(() -> new SlotNotAvailableException("Slot no encontrado con ID: " + command.slotId()));

        if (!slot.isAvailable()) {
            throw new SlotNotAvailableException("El slot seleccionado ya no está disponible");
        }

        // Mark slot as booked
        slot.markAsBooked();
        slotRepository.save(slot);

        // Create appointment
        Appointment appointment = Appointment.create(
                patient.getId(),
                dentist.getId(),
                slot.getId(),
                slot.getDate(),
                slot.getStartTime(),
                slot.getEndTime(),
                command.reason()
        );

        Appointment saved = appointmentRepository.save(appointment);

        // Send confirmation email to patient if email is registered
        if (patient.getEmail() != null && !patient.getEmail().isBlank()) {
            String dentistName = "Dr. " + dentist.getFirstName() + " " + dentist.getLastName();
            String patientName = patient.getFirstName() + " " + patient.getLastName();
            emailSender.sendAppointmentConfirmationNotification(
                    patient.getEmail(), patientName, dentistName, slot.getDate(), slot.getStartTime()
            );
        }

        return AppointmentResponse.fromDomain(saved);
    }
}
