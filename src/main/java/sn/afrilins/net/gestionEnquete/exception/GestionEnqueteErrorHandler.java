package sn.afrilins.net.gestionEnquete.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import sn.afrilins.net.gestionEnquete.common.error.GestionEnqueteError;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class GestionEnqueteErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(GestionEnqueteErrorHandler.class);

//    private HttpStatus getStatus(GestionImmeubleRapportException exception) {
//        ErrorStatus status = exception.getStatus();
//        if (status == null) {
//            return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//        switch (status) {
//            case BAD_REQUEST:
//                return HttpStatus.BAD_REQUEST;
//            case UNAUTHORIZED:
//                return HttpStatus.UNAUTHORIZED;
//            case FORBIDDEN:
//                return HttpStatus.FORBIDDEN;
//            case FOUND:
//                return HttpStatus.FOUND;
//            case INVENTAIRE_INVALID:
//                return HttpStatus.NOT_ACCEPTABLE;
//            case INVALID_QUANTITE_FACTURE:
//                return HttpStatus.GONE;
//            case  NOT_FOUND:
//                return  HttpStatus.NOT_FOUND;
//            default:
//                return HttpStatus.INTERNAL_SERVER_ERROR;
//        }
//    }


    @ExceptionHandler
    public ResponseEntity<Object> handleBeanValidationError(ConstraintViolationException exception) {
        logger.debug("Bean validation error {}", exception.getMessage(), exception);

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler
    public ResponseEntity<Object> handleBeanValidationError(ResourceNotFoundException exception) {
        logger.debug("Bean validation error {}", exception.getMessage(), exception);

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public GestionEnqueteError handleRuntimeException(Throwable throwable) {
        logger.error("An unhandled error occurs: {}", throwable.getMessage(), throwable);
        return GestionEnqueteError.builder().status(HttpStatus.INTERNAL_SERVER_ERROR).code(throwable.getMessage()).build();
    }

//    @ExceptionHandler
//    public ResponseEntity<Object> handleTresorerieException(CodeAlreadyUsedException exception) {
//
//        HttpStatus status = getStatus(exception);
//
//        return new ResponseEntity<>(
//                exception.getCode(), status);
//    }



}
