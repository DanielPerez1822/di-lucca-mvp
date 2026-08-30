package com.odontosys.odontosys_api.application.schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.response.AvailabilitySlotResponse;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.domain.model.SlotStatus;
import com.odontosys.odontosys_api.domain.port.in.schedule.GetAvailableSlotsUseCase;
import com.odontosys.odontosys_api.domain.port.out.AvailabilitySlotRepositoryPort;

public class GetAvailableSlotsService implements GetAvailableSlotsUseCase {

    private final AvailabilitySlotRepositoryPort slotRepository;

    public GetAvailableSlotsService(AvailabilitySlotRepositoryPort slotRepository) {
        this.slotRepository = slotRepository;
    }

    @Override
    public List<AvailabilitySlotResponse> execute(UUID dentistId, LocalDate startDate, LocalDate endDate) {
        List<AvailabilitySlot> slots = slotRepository.findByDentistIdAndDateBetweenAndStatus(
                dentistId, startDate, endDate, SlotStatus.AVAILABLE
        );
        return slots.stream()
                .map(AvailabilitySlotResponse::fromDomain)
                .toList();
    }
}
