package store.furniture.repository;

import store.furniture.model.WoodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WoodRepository extends JpaRepository<WoodEntity, Long> {
}
