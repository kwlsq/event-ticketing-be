package com.purwafest.purwafest.event.domain.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "category")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_gen")
  @SequenceGenerator(name = "category_id_gen", sequenceName = "category_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "name")
  private String name;

}
