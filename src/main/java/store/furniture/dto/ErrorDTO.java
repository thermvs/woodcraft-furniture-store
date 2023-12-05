package store.furniture.dto;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
public class ErrorDTO {
  public HttpStatusCode code;
  public String error;
}
