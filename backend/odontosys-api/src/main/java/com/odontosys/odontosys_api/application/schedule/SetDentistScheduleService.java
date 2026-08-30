package com.odontosys.odontosys_api.application.schedule;

import java.util.List;
import java.util.UUID;
import com.odontosys.odontosys_api.application.schedule.command.SetDentistScheduleCommand;
import com.odontosys.odontosys_api.application.schedule.response.DentistScheduleResponse;
import com.odontosys.odontosys_api.domain.exception.UserNotFoundException;
import com.odontosys.odontosys_api.domain.model.DentistSchedule;
import com.odontosys.odontosys_api.domain.model.User;
import com.odontosys.odontosys_api.domain.port.in.schedule.SetDentistScheduleUseCase;
import com.odontosys.odontosys_api.domain.port.out.DentistScheduleRepositoryPort;
import com.odontosys.odontosys_api.domain.port.out.UserRepositoryPort;

import org.springframework.transaction.annotation.Transactional;

public class SetDentistScheduleService implements SetDentistScheduleUseCase {

    private final DentistScheduleRepositoryPort scheduleRepository;
    private final UserRepositoryPort userRepository;

    public SetDentistScheduleService(DentistScheduleRepositoryPort scheduleRepository, UserRepositoryPort userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<DentistScheduleResponse> execute(UUID dentistId, List<SetDentistScheduleCommand> commands) {
        User user = userRepository.findById(dentistId)
                .orElseThrow(() -> new UserNotFoundException("Usuario/Dentista no encontrado con ID: " + dentistId));

        // Replace existing schedules for this dentist
        scheduleRepository.deleteByDentistId(dentistId);

        List<DentistSchedule> newSchedules = commands.stream()
                .map(cmd -> DentistSchedule.create(dentistId, cmd.dayOfWeek(), cmd.startTime(), cmd.endTime(), cmd.slotDurationMinutes(), cmd.hasBreak(), cmd.breakStartTime(), cmd.breakEndTime()))
                .toList();

        List<DentistSchedule> savedSchedules = scheduleRepository.saveAll(newSchedules);
        return savedSchedules.stream()
                .map(DentistScheduleResponse::fromDomain)
                .toList();
    }
}
