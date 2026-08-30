package com.odontosys.odontosys_api.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;

public interface DentistScheduleRepositoryPort {

    DentistSchedule save(DentistSchedule schedule);

    List<DentistSchedule> saveAll(List<DentistSchedule> schedules);

    List<DentistSchedule> findByDentistId(UUID dentistId);

    void deleteByDentistId(UUID dentistId);
}
