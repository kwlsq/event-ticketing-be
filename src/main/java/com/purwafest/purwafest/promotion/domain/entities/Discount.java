package com.purwafest.purwafest.promotion.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.purwafest.purwafest.auth.domain.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "discount")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Discount {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_id_gen")
  @SequenceGenerator(name = "discount_id_gen", sequenceName = "discount_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "is_used", nullable = false)
  private boolean isUsed;

  @NotNull
  @Column(name = "code_voucher")
  private UUID codeVoucher;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonBackReference
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "promotion_id", referencedColumnName = "id")
  @JsonBackReference
  private Promotion promotion;

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
