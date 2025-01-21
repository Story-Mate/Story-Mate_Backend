package store.storymate.storymatebackend.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import store.storymate.storymatebackend.global.error.dto.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
        log.warn("BAD_REQUEST_EXCEPTION :: message = {}", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(exception.getMessage());
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception) {
        log.warn("FORBIDDEN_EXCEPTION :: message = {}", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        log.warn("NOT_FOUND_EXCEPTION :: message = {}", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception) {
        log.warn("UNAUTHORIZED_EXCEPTION :: message = {}", exception.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getFieldError();
        String message = String.format("%s (필드: %s)", fieldError.getDefaultMessage(), fieldError.getField());

        log.error("METHOD_ARGUMENT_NOT_VALID_EXCEPTION :: message = {}", message);
        ErrorResponse response = ErrorResponse.of(message);
        return ResponseEntity.badRequest()
                .body(response);
    }
}
