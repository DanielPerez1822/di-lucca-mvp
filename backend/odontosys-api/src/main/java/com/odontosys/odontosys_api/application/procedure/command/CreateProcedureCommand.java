package com.odontosys.odontosys_api.application.procedure.command;

import java.math.BigDecimal;

public record CreateProcedureCommand(
        String name,
        String description,
        BigDecimal price,
        Integer duration
) {
}
