package com.odontosys.odontosys_api.application.schedule;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.response.DentistScheduleResponse;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;
import com.odontosys.odontosys_api.domain.port.in.schedule.GetDentistScheduleUseCase;
import com.odontosys.odontosys_api.domain.port.out.DentistScheduleRepositoryPort;

public class GetDentistScheduleService implements GetDentistScheduleUseCase {

    private final DentistScheduleRepositoryPort scheduleRepository;

    public GetDentistScheduleService(DentistScheduleRepositoryPort scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<DentistScheduleResponse> execute(UUID dentistId) {
        List<DentistSchedule> schedules = scheduleRepository.findByDentistId(dentistId);
        return schedules.stream()
                .map(DentistScheduleResponse::fromDomain)
                .toList();
    }
}
