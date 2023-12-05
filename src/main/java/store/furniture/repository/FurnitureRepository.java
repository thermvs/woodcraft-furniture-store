package store.furniture.repository;

import store.furniture.model.FurnitureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FurnitureRepository extends JpaRepository<FurnitureEntity, Long> {
  Optional<FurnitureEntity> findById(Long uuid);
}
