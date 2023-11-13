package com.auth.wow.libre.infrastructure.handler;

import com.auth.wow.libre.domain.model.shared.GenericResponse;
import com.auth.wow.libre.domain.model.shared.NotNullValuesDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ManagerExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<GenericResponse<NotNullValuesDto>> methodArgumentNotValidException(MethodArgumentNotValidException e) {

    NotNullValuesDto invalidData = new NotNullValuesDto();
    List<String> errors = new ArrayList<>();

    for (FieldError data : e.getBindingResult().getFieldErrors()) {
      errors.add(String.format("Invalid Field: %s, Cause: %s", data.getField(), data.getDefaultMessage()));
    }

    invalidData.setNumberOfInvalid(e.getBindingResult().getFieldErrorCount());
    invalidData.setValuesInvalid(errors);

    GenericResponse<NotNullValuesDto> response = new GenericResponse<>();
    response.setMessage("The submitted fields are invalid.");
    response.setCode(400);
    response.setData(invalidData);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

}
