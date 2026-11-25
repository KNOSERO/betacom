package com.ravcube.betacom.api.exception;

import com.ravcube.betacom.domain.note.InvalidNoteException;
import com.ravcube.betacom.domain.item.ItemVersionConflictException;
import com.ravcube.betacom.domain.item.InvalidItemException;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (firstError, ignored) -> firstError,
                        TreeMap::new));

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed");
        problem.setTitle("Bad Request");
        problem.setProperty("errors", fieldErrors);
        return problem;
    }

    @ExceptionHandler(InvalidNoteException.class)
    public ProblemDetail handleInvalidNote(InvalidNoteException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
        problem.setTitle("Invalid note");
        return problem;
    }

    @ExceptionHandler(InvalidItemException.class)
    public ProblemDetail handleInvalidItem(InvalidItemException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
        problem.setTitle("Invalid item");
        return problem;
    }

    @ExceptionHandler({ItemVersionConflictException.class, OptimisticLockingFailureException.class})
    public ProblemDetail handleVersionConflict(RuntimeException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage());
        problem.setTitle("Version conflict");
        return problem;
    }
}
