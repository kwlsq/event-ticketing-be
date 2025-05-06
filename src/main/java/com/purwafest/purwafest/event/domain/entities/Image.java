package com.purwafest.purwafest.event.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "image")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Image {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_gen")
  @SequenceGenerator(name = "image_id_gen", sequenceName = "image_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "url")
  private String url;

  @NotNull
  @Column(name = "alt")
  private String alt;

  @NotNull
  @Column(name = "order_image")
  private String orderImage;

  @NotNull
  @Column(name = "is_thumbnail")
  private boolean isThumbnail;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", referencedColumnName = "id")
  @JsonBackReference
  private Event event;

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
