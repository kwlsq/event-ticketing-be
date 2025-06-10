package com.purwafest.purwafest.category.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.purwafest.purwafest.event.domain.entities.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.time.Instant;
import java.util.Set;

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

  @NotNull
  @Column(name = "icon_url")
  private String iconUrl;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonIgnore
  private Set<Event> events;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "modified_at")
  private Instant modifiedAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now();
    this.modifiedAt = Instant.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.modifiedAt = Instant.now();
  }

  @PreRemove
  public void preRemove() {
    this.deletedAt = Instant.now();
  }
}
