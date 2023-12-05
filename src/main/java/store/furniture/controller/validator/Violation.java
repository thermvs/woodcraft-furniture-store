package store.furniture.controller.validator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Violation {
  public String field;
  public String violation;

  @Override
  public String toString() {
    return "{field:" + field + ", violation:" + violation + "}";
  }
}
