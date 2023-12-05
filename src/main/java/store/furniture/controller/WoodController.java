package store.furniture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.furniture.controller.validator.Validator;
import store.furniture.dto.WoodDTO;
import store.furniture.service.WoodService;

@RestController
@RequestMapping("/api/v1/woods")
@RequiredArgsConstructor
public class WoodController {
  private final WoodService woodService;

  @GetMapping("/")
  public ResponseEntity<?> getAllWoods(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "size", defaultValue = "10") int size) {
    WoodValidator validator = new WoodValidator();
    validator.validateSize(size);
    return ControllerExecutor.execute(validator, () -> {
        return ResponseEntity.ok().body(woodService.getAllWoods(page, size));
      });
  }


  @GetMapping("/{id}")
  public ResponseEntity<?> getWoodById(@PathVariable("id") long id) {
    WoodValidator validator = new WoodValidator();
    return ControllerExecutor.execute(validator, () -> {
      return ResponseEntity.ok().body(woodService.getWoodById(id));
    });
  }


  @PostMapping
  public ResponseEntity<?> createWood(@RequestBody WoodDTO req) {
    WoodValidator validator = new WoodValidator();
    validator.validateWood(req);
    return ControllerExecutor.execute(validator, () -> {
      return ResponseEntity.status(HttpStatus.CREATED).body(woodService.createWood(req));
    });
  }


  @PutMapping("/{id}")
  public ResponseEntity<?> updateWood(@PathVariable("id") long id, @RequestBody WoodDTO req)  {
    WoodValidator validator = new WoodValidator();
    validator.validateWood(req);
    return ControllerExecutor.execute(validator, () -> {
      woodService.updateWood(id, req);
      return ResponseEntity.ok().body("ok");
    });
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteWood(@PathVariable("id") long id) {
    WoodValidator validator = new WoodValidator();
    return ControllerExecutor.execute(validator, () -> {
      woodService.deleteWood(id);
      return ResponseEntity.noContent().build();
    });
  }


  private static class WoodValidator extends Validator {
    public WoodValidator validateSize(int size){
      if (size > 50) {
        this.addViolation("size", "size must be <= 50");
      } return this;
    }

    public WoodValidator validateWood(WoodDTO req) {
      if (req == null) {
        this.addViolation("Wood", "Wood is null");
      }
      if (req.getName() == null || req.getDoneDate() == null) {
        this.addViolation("Wood", "Wood is not set or empty");
      }
      return this;
    }
  }
}
