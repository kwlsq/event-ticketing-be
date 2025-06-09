package com.purwafest.purwafest.promotion.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.purwafest.purwafest.discount.domain.entities.Discount;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.enums.PromotionType;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "promotion")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Promotion {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promotion_id_gen")
  @SequenceGenerator(name = "promotion_id_gen", sequenceName = "promotion_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description")
  private String description;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private PromotionType type;

  @NotNull
  @Column(name = "value", nullable = false)
  private BigInteger value;

  @Column(name = "period")
  private Integer period;

  @Column(name = "max_usage")
  private Integer maxUsage;

  @Column(name = "usage_count", nullable = false, columnDefinition = "int4 default 0")
  private Integer usageCount = 0;

  @Column(name = "is_referral_promotion", nullable = false, columnDefinition = "boolean default false")
  private boolean isReferralPromotion = false;

  @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<Discount> discounts;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", referencedColumnName = "id")
  @JsonBackReference
  private Event event;

  @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Invoice> invoice;

  @Column(name = "expired_date")
  private Instant expiredDate;

  @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp default now()")
  private Instant createdAt;

  @Column(name = "modified_at", nullable = false, columnDefinition = "timestamp default now()")
  private Instant modifiedAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @PrePersist
  public void prePersist() {
    createdAt = Instant.now();
    modifiedAt = Instant.now();
  }

  @PreUpdate
  public void preUpdate() {
    modifiedAt = Instant.now();
  }

  @PreRemove
  public void preRemove() {
    deletedAt = Instant.now();
  }
}
