package AppointToDoctorRestService.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<CustomErrorMessage> badRequestException(BadRequestException e, WebRequest request) {
        CustomErrorMessage customErrorMessage = new CustomErrorMessage(LocalDateTime.now(),
                HttpServletResponse.SC_BAD_REQUEST,
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.toString().substring(request.toString().indexOf("/"), request.toString().indexOf(";")));
        return new ResponseEntity<>(customErrorMessage, HttpStatus.BAD_REQUEST);
    }
}
