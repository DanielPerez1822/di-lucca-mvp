package com.odontosys.odontosys_api.domain.port.in.schedule;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.response.DentistScheduleResponse;

public interface GetDentistScheduleUseCase {

    List<DentistScheduleResponse> execute(UUID dentistId);
}
