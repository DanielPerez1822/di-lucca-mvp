package com.odontosys.odontosys_api.application.auth.command;

public record ResetPasswordCommand(String email, String code, String newPassword) {
}
