package store.furniture.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "furniture")
public class FurnitureEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "description")
  private String description;

  @NotNull(message = "price must be not null")
  @Column(name = "price")
  private Integer price;

  @ManyToOne()
  @JoinColumn(name = "wood_id")
  @ToString.Exclude
  private WoodEntity wood;

  @ManyToOne
  @JoinColumn(name = "order_id")
  @ToString.Exclude
  private OrderEntity order;
}
