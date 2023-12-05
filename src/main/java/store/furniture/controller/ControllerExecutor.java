package store.furniture.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import store.furniture.controller.validator.Validator;
import store.furniture.exception.DatabaseConflictException;
import store.furniture.exception.DoesNotExistException;

class ControllerExecutor {

  public static ResponseEntity<?> execute(Validator validator, ControllerRunner controllerFunc) {
    try {
      if (validator != null && validator.hasViolations()) {
        throw new ApiException(HttpStatus.UNPROCESSABLE_ENTITY, "validation isn't passed",
            new Exception(validator.getDescription()));
      }

      try {
        return controllerFunc.run();
      } catch (AuthenticationException ex) {
        throw new ApiException(HttpStatus.UNAUTHORIZED, ex.getMessage());
      } catch (DoesNotExistException ex) {
        throw new ApiException(HttpStatus.NOT_FOUND, ex.getMessage());
      } catch (DatabaseConflictException ex) {
        throw new ApiException(HttpStatus.CONFLICT, ex.getMessage());
      } catch (Exception ex) {
        throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex);
      }

    } catch (ApiException e) {
      return ResponseEntity.status(e.get().getStatus()).body(e.get());
    }
  }

  public interface ControllerRunner {
    public ResponseEntity<?> run() throws Exception;
  }
}
