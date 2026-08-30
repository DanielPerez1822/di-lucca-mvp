package com.odontosys.odontosys_api.infrastructure.adapter.in.web;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.odontosys.odontosys_api.domain.exception.AppointmentNotFoundException;
import com.odontosys.odontosys_api.domain.exception.DomainException;
import com.odontosys.odontosys_api.domain.exception.InvalidCredentialsException;
import com.odontosys.odontosys_api.domain.exception.InvalidResetCodeException;
import com.odontosys.odontosys_api.domain.exception.InvalidTokenException;
import com.odontosys.odontosys_api.domain.exception.InvoiceNotFoundException;
import com.odontosys.odontosys_api.domain.exception.MedicalRecordNotFoundException;
import com.odontosys.odontosys_api.domain.exception.PatientAlreadyExistsException;
import com.odontosys.odontosys_api.domain.exception.PatientNotFoundException;
import com.odontosys.odontosys_api.domain.exception.ProcedureAlreadyExistsException;
import com.odontosys.odontosys_api.domain.exception.ProcedureNotFoundException;
import com.odontosys.odontosys_api.domain.exception.ScheduleNotFoundException;
import com.odontosys.odontosys_api.domain.exception.SlotNotAvailableException;
import com.odontosys.odontosys_api.domain.exception.UserAlreadyExistsException;
import com.odontosys.odontosys_api.domain.exception.UserDisabledException;
import com.odontosys.odontosys_api.domain.exception.UserNotFoundException;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.ErrorResponseDto;
import com.odontosys.odontosys_api.infrastructure.adapter.in.web.dto.response.ValidationErrorResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<ErrorResponseDto> handleUserDisabled(UserDisabledException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidToken(InvalidTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(401, "Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(InvalidResetCodeException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidResetCode(InvalidResetCodeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(PatientAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handlePatientAlreadyExists(PatientAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(ProcedureAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleProcedureAlreadyExists(ProcedureAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePatientNotFound(PatientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(ProcedureNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProcedureNotFound(ProcedureNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleScheduleNotFound(ScheduleNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleAppointmentNotFound(AppointmentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(MedicalRecordNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleMedicalRecordNotFound(MedicalRecordNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleInvoiceNotFound(InvoiceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDto(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(SlotNotAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleSlotNotAvailable(SlotNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(409, "Conflict", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponseDto> handleDomainException(DomainException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponseDto(422, "Unprocessable Entity", ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponseDto(403, "Forbidden", "No tiene permisos para realizar esta acción"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDto> handleValidation(MethodArgumentNotValidException ex) {
        List<ValidationErrorResponseDto.FieldErrorDto> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(field -> new ValidationErrorResponseDto.FieldErrorDto(field.getField(), field.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ValidationErrorResponseDto(400, "Validation Error", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(500, "Internal Server Error", "Ha ocurrido un error inesperado"));
    }
}
