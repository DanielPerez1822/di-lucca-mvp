package com.odontosys.odontosys_api.application.medicalrecord;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.medicalrecord.command.UpdateMedicalRecordCommand;
import com.odontosys.odontosys_api.application.medicalrecord.response.MedicalRecordResponse;
import com.odontosys.odontosys_api.domain.exception.MedicalRecordNotFoundException;
import com.odontosys.odontosys_api.domain.exception.ProcedureNotFoundException;
import com.odontosys.odontosys_api.domain.model.MedicalRecord;
import com.odontosys.odontosys_api.domain.model.MedicalRecordProcedureItem;
import com.odontosys.odontosys_api.domain.model.Procedure;
import com.odontosys.odontosys_api.domain.port.in.medicalrecord.UpdateMedicalRecordUseCase;
import com.odontosys.odontosys_api.domain.port.out.MedicalRecordRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.ProcedureRepositoryPort;

public class UpdateMedicalRecordService implements UpdateMedicalRecordUseCase {

    private final MedicalRecordRepositoryPort medicalRecordRepository;
    private final ProcedureRepositoryPort procedureRepository;

    public UpdateMedicalRecordService(MedicalRecordRepositoryPort medicalRecordRepository,
                                     ProcedureRepositoryPort procedureRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.procedureRepository = procedureRepository;
    }

    @Override
    public MedicalRecordResponse execute(UUID id, UpdateMedicalRecordCommand command) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new MedicalRecordNotFoundException("Registro médico no encontrado con ID: " + id));

        List<MedicalRecordProcedureItem> updatedItems = null;
        if (command.items() != null) {
            updatedItems = new ArrayList<>();
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
                updatedItems.add(item);
            }
        }

        record.update(command.diagnosis(), command.notes(), updatedItems);
        MedicalRecord updated = medicalRecordRepository.save(record);
        return MedicalRecordResponse.fromDomain(updated);
    }
}
