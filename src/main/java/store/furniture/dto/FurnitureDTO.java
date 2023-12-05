package store.furniture.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FurnitureDTO {
  private long id;
  private String description;
  private Integer price;
  private Long woodId;
}
