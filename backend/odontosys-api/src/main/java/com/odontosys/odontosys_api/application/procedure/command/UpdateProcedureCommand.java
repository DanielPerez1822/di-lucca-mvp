package com.odontosys.odontosys_api.application.procedure.command;

import java.math.BigDecimal;

public record UpdateProcedureCommand(
        String name,
        String description,
        BigDecimal price,
        Integer duration
) {
}
