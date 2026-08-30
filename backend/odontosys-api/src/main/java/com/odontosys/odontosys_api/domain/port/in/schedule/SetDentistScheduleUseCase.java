package com.odontosys.odontosys_api.domain.port.in.schedule;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.command.SetDentistScheduleCommand;
import com.odontosys.odontosys_api.application.schedule.response.DentistScheduleResponse;

public interface SetDentistScheduleUseCase {

    List<DentistScheduleResponse> execute(UUID dentistId, List<SetDentistScheduleCommand> commands);
}
