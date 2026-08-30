package com.odontosys.odontosys_api.application.medicalrecord;

import java.util.ArrayList;
import java.util.List;
import com.odontosys.odontosys_api.application.medicalrecord.command.CreateMedicalRecordCommand;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.exception.ProcedureNotFoundException;
import com.odontosys.odontosys_api.domain.exception.UserNotFoundException;
import com.odontosys.odontosys_api.domain.model.AppointmentStatus;
import com.odontosys.odontosys_api.domain.model.MedicalRecord;
import com.odontosys.odontosys_api.domain.model.MedicalRecordProcedureItem;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.domain.port.in.invoice.CreateInvoiceFromMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.CreateMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.out.AppointmentRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.MedicalRecordRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.PatientRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

public class CreateMedicalRecordService implements CreateMedicalRecordUseCase {

    private final MedicalRecordRepositoryPort medicalRecordRepository;
    private final PatientRepositoryPort patientRepository;
    private final UserRepositoryPort userRepository;
    private final ProcedureRepositoryPort procedureRepository;
    private final AppointmentRepositoryPort appointmentRepository;
    private final CreateInvoiceFromMedicalRecordUseCase createInvoiceUseCase;

    public CreateMedicalRecordService(MedicalRecordRepositoryPort medicalRecordRepository,
                                     PatientRepositoryPort patientRepository,
                                     UserRepositoryPort userRepository,
                                     ProcedureRepositoryPort procedureRepository,
                                     AppointmentRepositoryPort appointmentRepository,
                                     CreateInvoiceFromMedicalRecordUseCase createInvoiceUseCase) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.procedureRepository = procedureRepository;
        this.appointmentRepository = appointmentRepository;
        this.createInvoiceUseCase = createInvoiceUseCase;
    }

    @Override
    public MedicalRecordResponse execute(CreateMedicalRecordCommand command) {
        if (patientRepository.findById(command.patientId()).isEmpty()) {
            throw new PatientNotFoundException("Paciente no encontrado con ID: " + command.patientId());
        }

        if (userRepository.findById(command.dentistId()).isEmpty()) {
            throw new UserNotFoundException("Dentista no encontrado con ID: " + command.dentistId());
        }

        List<MedicalRecordProcedureItem> domainItems = new ArrayList<>();
        if (command.items() != null) {
            for (var itemCmd : command.items()) {
                Procedure procedure = procedureRepository.findById(itemCmd.procedureId())
                        .orElseThrow(() -> new ProcedureNotFoundException("Procedimiento no encontrado con ID: " + itemCmd.procedureId()));

                var item = MedicalRecordProcedureItem.create(
                        procedure.getId(),
                        procedure.getName(),
                        itemCmd.appliedPrice() != null ? itemCmd.appliedPrice() : procedure.getPrice(),
                        itemCmd.toothNumber(),
                        itemCmd.notes()
                );
                domainItems.add(item);
            }
        }

        MedicalRecord record = MedicalRecord.create(
                command.patientId(),
                command.dentistId(),
                command.appointmentId(),
                command.diagnosis(),
                command.notes(),
                domainItems
        );

        // If linked to an appointment, change appointment status to COMPLETED
        if (command.appointmentId() != null) {
            appointmentRepository.findById(command.appointmentId()).ifPresent(appt -> {
                appt.changeStatus(AppointmentStatus.COMPLETED, "Atención clínica completada y registrada en historia clínica");
                appointmentRepository.save(appt);
            });
        }

        MedicalRecord saved = medicalRecordRepository.save(record);

        // Automatically generate invoice for this medical record
        if (createInvoiceUseCase != null) {
            try {
                createInvoiceUseCase.execute(saved.getId());
            } catch (Exception ignored) {
            }
        }

        return MedicalRecordResponse.fromDomain(saved);
    }
}
