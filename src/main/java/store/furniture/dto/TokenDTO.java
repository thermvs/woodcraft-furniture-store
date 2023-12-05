package store.furniture.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
  private String jwtToken;
  private String refreshToken;
}
