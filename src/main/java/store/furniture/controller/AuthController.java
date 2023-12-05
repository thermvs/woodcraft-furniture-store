package store.furniture.controller;

import store.furniture.controller.validator.Validator;
import store.furniture.dto.UserDTO;
import store.furniture.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/v1/auth", produces = { "application/json", "application/xml" })
@RequiredArgsConstructor
class AuthController {

  private final AuthService authService;

  @PostMapping(path = "/login")
  public ResponseEntity<?> login(@RequestBody UserDTO req) {
    AuthValidator validator = new AuthValidator();
    validator.validateLogin(req).validatePassword(req);

    return ControllerExecutor.execute(validator, () -> {
      var tokenDTO = authService.login(req);
      return ResponseEntity.ok().body(tokenDTO);
    });

  }

  @PostMapping("/refresh")
  public ResponseEntity<?> refreshToken(@RequestHeader HttpHeaders reqHeaders) {
    return ControllerExecutor.execute(null, () -> {
      var tokenDTO = authService.refreshToken(reqHeaders);
      return ResponseEntity.ok().body(tokenDTO);
    });
  }

  private static class AuthValidator extends Validator {
    public AuthValidator validateLogin(UserDTO req) {
      if (req.getLogin() == null || req.getLogin().isEmpty()) {
        this.addViolation("login", "user login in not set or empty");
      }
      return this;
    }

    public AuthValidator validatePassword(UserDTO req) {
      if (req.getPassword() == null || req.getPassword().isEmpty()) {
        this.addViolation("password", "user password in not set or empty");
      }
      return this;
    }
  }
}
