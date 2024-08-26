package vn.huyl.service.auth.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import vn.huyl.service.auth.dto.ResponseDto;
import vn.huyl.service.auth.util.ConstantUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ResponseDto> handleAccessDeniedException(AuthorizationDeniedException ex) {
        ResponseDto responseDto = new ResponseDto(HttpStatus.FORBIDDEN.value(),
                ConstantUtil.ErrorMessage.ACCESS_DENIED.getValue(), null);
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ResponseDto> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        ResponseDto responseDto = new ResponseDto(HttpStatus.NOT_FOUND.value(),
                String.format(ConstantUtil.ErrorMessage.NOT_FOUND_URL.getValue(), request.getRequestURI()), null);
        return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ResponseDto> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String method = ex.getMethod();
        String detailedMessage = String.format(ConstantUtil.ErrorMessage.HTTP_METHOD.getValue(), method);
        ResponseDto responseDto = new ResponseDto(HttpStatus.METHOD_NOT_ALLOWED.value(), detailedMessage, null);
        return new ResponseEntity<>(responseDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        String errorMessage = errors.entrySet().stream()
                .map(entry -> "Trường '" + entry.getKey() + "' " + entry.getValue())
                .collect(Collectors.joining(", "));
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(),
                errorMessage, null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = ex.getMessage();
        String detailedMessage = ConstantUtil.ErrorMessage.MALFORMED_JSON.getValue();
        if (message != null && !message.isEmpty()) {
            if (message.contains("JSON parse error")) {
                detailedMessage = String.format(ConstantUtil.ErrorMessage.INVALID_JSON.getValue(), message);
            }
        }
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(),
                detailedMessage, null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ResponseDto> handleMissingParams(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String detailedMessage = String.format(ConstantUtil.ErrorMessage.INVALID_REQUEST.getValue(), parameterName);
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), detailedMessage, null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getName();
        String expectedType = Objects.requireNonNull(ex.getRequiredType()).getSimpleName();
        String actualType = Objects.requireNonNull(ex.getValue()).getClass().getSimpleName();
        String detailedMessage = String.format(ConstantUtil.ErrorMessage.TYPE_MISMATCH.getValue(),
                parameterName, expectedType, actualType);
        ResponseDto responseDto = new ResponseDto(HttpStatus.BAD_REQUEST.value(), detailedMessage, null);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleGeneralException(Exception ex) {
        ResponseDto responseDto = new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ConstantUtil.ErrorMessage.INTERNAL_SEVER_ERROR.getValue(), null);
        return new ResponseEntity<>(responseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
