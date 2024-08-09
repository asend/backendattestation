package com.fonctionpublique.handlers;

import com.fonctionpublique.exception.*;
import com.fonctionpublique.handleException.EmailAlreadyExistException;
import com.fonctionpublique.handleException.NinAlreadyExistException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ObjectValidationException.class})
    public ResponseEntity<ExceptionRepresentation> handleException(ObjectValidationException exception) {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("Object not valid exception has occurred")
                .errorSource(exception.getViolationSource())
                .validationErrors(exception.getViolations())
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(representation);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionRepresentation> handleException(EntityNotFoundException exception) {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("NOT_FOUND")
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(representation);
    }

    @ExceptionHandler(OperationNonPermittedException.class)
    public ResponseEntity<ExceptionRepresentation> handleException(OperationNonPermittedException exception) {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage(exception.getErrorMsg())
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_ACCEPTABLE)
                .body(representation);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionRepresentation> handleException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("A user already exists with the provided Email")
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(representation);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionRepresentation> handleDisabledException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("You cannot access your account because it is not yet activated")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionRepresentation> handleBadCredentialsException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("Your email and / or password is incorrect")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(EmailNotExistException.class)
    public ResponseEntity<ExceptionRepresentation> handleEmailNotExistException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("EMAIL_NOT_EXIT")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }
    @ExceptionHandler(NotMatchPasswordException.class)
    public ResponseEntity<ExceptionRepresentation> handleNotMatchException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("NOT_MATCH_PASSWORD")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(NotMatchPasswordOldException.class)
    public ResponseEntity<ExceptionRepresentation> handleNotMatchOldException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("NOT_MATCH_PASSWORD_OLD")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionRepresentation> handleInvalidTokendException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("TOKEN_INVALID")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(ExpirationTokenException.class)
    public ResponseEntity<ExceptionRepresentation> handleTokenExpiredException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("TOKEN_EXPIRED")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionRepresentation> handleEmailExistException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("EMAIL_EXIST")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(NinAlreadyExistException.class)
    public ResponseEntity<ExceptionRepresentation> handleNinException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("NIN_EXIST")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(EmailAndNinAlreadyExistException.class)
    public ResponseEntity<ExceptionRepresentation> handleEmailNinException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("EMAIL_NIN_EXIST")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }

    @ExceptionHandler(EntityNotMatchException.class)
    public ResponseEntity<ExceptionRepresentation> handleDataMistakeException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("MISSING_DATA")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }
    @ExceptionHandler(InvalidExtensionException.class)
    public ResponseEntity<ExceptionRepresentation> handleInvaliExtensionException() {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .errorMessage("INVALID_EXTENTION")
                .build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }


}
