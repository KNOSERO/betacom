package com.ravcube.betacom.api.exception;

import com.ravcube.betacom.domain.note.InvalidNoteException;
import com.ravcube.betacom.domain.item.AccessItemException;
import com.ravcube.betacom.domain.item.ItemVersionConflictException;
import com.ravcube.betacom.domain.item.InvalidItemException;
import com.ravcube.betacom.domain.permission.InvalidPermissionException;
import com.ravcube.betacom.domain.authentication.InvalidCredentialsException;
import com.ravcube.betacom.domain.authentication.UserAlreadyExistsException;
import com.ravcube.betacom.database.entity.ItemEntity;
import com.ravcube.betacom.database.repository.ItemEntityRepository;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final ItemEntityRepository itemRepository;

    public GlobalExceptionHandler(ItemEntityRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(InvalidCredentialsException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
        problem.setTitle("Unauthorized");
        return problem;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
        problem.setTitle("Login already taken");
        return problem;
    }

    @ExceptionHandler(AccessItemException.class)
    public ProblemDetail handleAccessItem(AccessItemException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, exception.getMessage());
        problem.setTitle("Forbidden");
        return problem;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFound(EntityNotFoundException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problem.setTitle("Not Found");
        return problem;
    }

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleUnreadableRequest(HttpMessageNotReadableException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request body is malformed or contains unsupported values");
        problem.setTitle("Bad Request");
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

    @ExceptionHandler(InvalidPermissionException.class)
    public ProblemDetail handleInvalidPermission(InvalidPermissionException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
        problem.setTitle("Invalid permission");
        return problem;
    }

    @ExceptionHandler(ItemVersionConflictException.class)
    public ProblemDetail handleVersionConflict(ItemVersionConflictException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                exception.getMessage());
        problem.setTitle("Version conflict");
        problem.setProperty("currentVersion", exception.getCurrentVersion());
        return problem;
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLockingFailure(OptimisticLockingFailureException exception) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "The item was modified by another request. Fetch the latest version and retry.");
        problem.setTitle("Version conflict");
        currentVersionFrom(exception).ifPresent(version -> problem.setProperty("currentVersion", version));
        return problem;
    }

    private Optional<Long> currentVersionFrom(OptimisticLockingFailureException exception) {
        if (exception instanceof ObjectOptimisticLockingFailureException optimisticFailure
                && optimisticFailure.getIdentifier() instanceof UUID itemId) {
            return itemRepository.findById(itemId).map(ItemEntity::getVersion);
        }
        return Optional.empty();
    }
}
