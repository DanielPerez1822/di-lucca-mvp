package com.odontosys.odontosys_api.application.schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.odontosys.odontosys_api.application.schedule.response.AvailabilitySlotResponse;
import com.odontosys.odontosys_api.domain.model.AvailabilitySlot;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;
import com.odontosys.odontosys_api.domain.port.in.schedule.GenerateSlotsUseCase;
import com.odontosys.odontosys_api.domain.port.out.AvailabilitySlotRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.DentistScheduleRepositoryPort;

public class GenerateSlotsService implements GenerateSlotsUseCase {

    private final DentistScheduleRepositoryPort scheduleRepository;
    private final AvailabilitySlotRepositoryPort slotRepository;

    public GenerateSlotsService(DentistScheduleRepositoryPort scheduleRepository,
                                AvailabilitySlotRepositoryPort slotRepository) {
        this.scheduleRepository = scheduleRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public List<AvailabilitySlotResponse> execute(UUID dentistId, LocalDate startDate, LocalDate endDate) {
        List<DentistSchedule> schedules = scheduleRepository.findByDentistId(dentistId);
        if (schedules.isEmpty()) {
            return List.of();
        }

        Map<DayOfWeek, List<DentistSchedule>> schedulesByDay = schedules.stream()
                .collect(Collectors.groupingBy(DentistSchedule::getDayOfWeek));

        List<AvailabilitySlot> newSlots = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            List<DentistSchedule> daySchedules = schedulesByDay.get(dayOfWeek);

            if (daySchedules != null && !daySchedules.isEmpty()) {
                for (DentistSchedule daySchedule : daySchedules) {
                    if (daySchedule.isActive()) {
                        LocalTime currentStartTime = daySchedule.getStartTime();
                        LocalTime dayEndTime = daySchedule.getEndTime();
                        int duration = daySchedule.getSlotDurationMinutes();

                        while (currentStartTime.plusMinutes(duration).isBefore(dayEndTime) || currentStartTime.plusMinutes(duration).equals(dayEndTime)) {
                            LocalTime slotEndTime = currentStartTime.plusMinutes(duration);

                            // Check if slot falls inside lunch break interval
                            boolean isBreakSlot = daySchedule.isHasBreak()
                                    && daySchedule.getBreakStartTime() != null
                                    && daySchedule.getBreakEndTime() != null
                                    && !(slotEndTime.isBefore(daySchedule.getBreakStartTime()) || slotEndTime.equals(daySchedule.getBreakStartTime())
                                         || currentStartTime.isAfter(daySchedule.getBreakEndTime()) || currentStartTime.equals(daySchedule.getBreakEndTime()));

                            if (!isBreakSlot) {
                                boolean exists = slotRepository.existsByDentistIdAndDateAndStartTime(dentistId, currentDate, currentStartTime);
                                if (!exists) {
                                    AvailabilitySlot slot = AvailabilitySlot.create(dentistId, currentDate, currentStartTime, slotEndTime);
                                    newSlots.add(slot);
                                }
                            }

                            currentStartTime = slotEndTime;
                        }
                    }
                }
            }

            currentDate = currentDate.plusDays(1);
        }

        List<AvailabilitySlot> savedSlots = slotRepository.saveAll(newSlots);

        // Return all slots between startDate and endDate
        List<AvailabilitySlot> allSlots = slotRepository.findByDentistIdAndDateBetween(dentistId, startDate, endDate);
        return allSlots.stream()
                .map(AvailabilitySlotResponse::fromDomain)
                .toList();
    }
}
