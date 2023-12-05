package store.furniture.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.furniture.controller.validator.Validator;
import store.furniture.dto.FurnitureDTO;
import store.furniture.service.FurnitureService;

@RestController
@RequestMapping("/api/v1/furniture")
@RequiredArgsConstructor
public class FurnitureController {
    private final FurnitureService FurnitureService;

    @GetMapping("/")
    public ResponseEntity<?> getAllFurniture(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "10") int size) {
        FurnitureValidator validator = new FurnitureValidator();
        validator.validateSize(size);
        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(FurnitureService.getAllFurniture(page, size));
        });
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getFurnitureById(@PathVariable("id") long id) {
        FurnitureValidator validator = new FurnitureValidator();
        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.ok().body(FurnitureService.getFurnitureById(id));
        });
    }


    @PostMapping
    public ResponseEntity<?> createFurniture(@RequestBody FurnitureDTO req) {
        FurnitureValidator validator = new FurnitureValidator();
        validator.validateFurniture(req);
        return ControllerExecutor.execute(validator, () -> {
            return ResponseEntity.status(HttpStatus.CREATED).body(FurnitureService.createFurniture(req));
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFurniture(@PathVariable("id") long id, @RequestBody FurnitureDTO req)  {
        FurnitureValidator validator = new FurnitureValidator();
        validator.validateFurniture(req);
        return ControllerExecutor.execute(validator, () -> {
            FurnitureService.updateFurniture(id, req);
            return ResponseEntity.ok().body("ok");
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFurniture(@PathVariable("id") long id) {
        FurnitureValidator validator = new FurnitureValidator();

        return ControllerExecutor.execute(validator, () -> {
            FurnitureService.deleteFurniture(id);
            return ResponseEntity.noContent().build();
        });
    }

    private static class FurnitureValidator extends Validator {
        public FurnitureValidator validateSize(int size){
            if (size > 50) {
                this.addViolation("size", "size must be <= 50");
            } return this;
        }

        public FurnitureValidator validateFurniture(FurnitureDTO req) {
            if (req == null) {
                this.addViolation("Furniture", "Furniture is null");
            }
            if (req.getPrice() == null || req.getPrice() < 0) {
                this.addViolation("price", "price is not set or empty");
            }
            return this;
        }
    }
}

