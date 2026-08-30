package com.odontosys.odontosys_api.domain.port.in.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.response.AvailabilitySlotResponse;

public interface GetAvailableSlotsUseCase {

    List<AvailabilitySlotResponse> execute(UUID dentistId, LocalDate startDate, LocalDate endDate);
}
