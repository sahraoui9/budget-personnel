package bgpersonnel.budget.exeception;

import bgpersonnel.budget.authentification.security.exeception.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
public class ErrorExceptionControllerAdvice {

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessageResponse handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessageResponse(ex.getMessage());
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessageResponse handleAlreadyExistsException(AlreadyExistsException ex, WebRequest request) {
        return new ErrorMessageResponse(ex.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessageResponse handleNotFoundException(NotFoundException ex, WebRequest request) {
        return new ErrorMessageResponse(ex.getMessage());
    }

    @ExceptionHandler(value = GenerationRapportException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessageResponse handleGenerationRapportException(GenerationRapportException ex, WebRequest request) {
        return new ErrorMessageResponse(ex.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseErrorValidation<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> errors = new HashMap<>();
        fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        ApiResponseErrorValidation<Map<String, String>> response = new ApiResponseErrorValidation<>(errors);
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessageResponse handleException(Exception ex, WebRequest request) {
        return new ErrorMessageResponse(ex.getMessage());
    }


}