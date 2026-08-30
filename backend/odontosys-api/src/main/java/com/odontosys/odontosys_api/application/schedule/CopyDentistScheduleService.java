package com.odontosys.odontosys_api.application.schedule;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.odontosys.odontosys_api.application.schedule.command.SetDentistScheduleCommand;
import com.odontosys.odontosys_api.application.schedule.response.DentistScheduleResponse;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;
import com.odontosys.odontosys_api.domain.model.Role;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.out.DentistScheduleRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

@Service
public class CopyDentistScheduleService {

    private final DentistScheduleRepositoryPort scheduleRepository;
    private final UserRepositoryPort userRepository;

    public CopyDentistScheduleService(DentistScheduleRepositoryPort scheduleRepository, UserRepositoryPort userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public List<DentistScheduleResponse> copySchedule(UUID sourceDentistId, UUID targetDentistId, boolean copyToAll) {
        List<DentistSchedule> sourceSchedules = scheduleRepository.findByDentistId(sourceDentistId);
        if (sourceSchedules.isEmpty()) {
            return List.of();
        }

        List<UUID> targetIds;
        if (copyToAll) {
            targetIds = userRepository.findAll().stream()
                    .filter(u -> u.getRoles().contains(Role.DENTIST) && !u.getId().equals(sourceDentistId))
                    .map(User::getId)
                    .toList();
        } else {
            targetIds = List.of(targetDentistId);
        }

        for (UUID targetId : targetIds) {
            scheduleRepository.deleteByDentistId(targetId);
            List<DentistSchedule> copied = sourceSchedules.stream()
                    .map(s -> DentistSchedule.create(
                            targetId,
                            s.getDayOfWeek(),
                            s.getStartTime(),
                            s.getEndTime(),
                            s.getSlotDurationMinutes(),
                            s.isHasBreak(),
                            s.getBreakStartTime(),
                            s.getBreakEndTime()
                    ))
                    .toList();
            scheduleRepository.saveAll(copied);
        }

        return sourceSchedules.stream()
                .map(DentistScheduleResponse::fromDomain)
                .toList();
    }
}
