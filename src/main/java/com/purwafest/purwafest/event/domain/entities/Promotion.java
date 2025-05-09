package com.purwafest.purwafest.event.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.purwafest.purwafest.event.domain.enums.PromotionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_id_gen")
  @SequenceGenerator(name = "image_id_gen", sequenceName = "image_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "name")
  private String name;

  @NotNull
  @Column(name = "description")
  private String description;

  @NotNull
  @Column(name = "type")
  private PromotionType type;

  @NotNull
  @Column(name = "value")
  private Integer value;

  @NotNull
  @Column(name = "period")
  private Integer period;

  @Column(name = "usage_count")
  private Integer usageCount;

  @Column(name = "is_referral_promotion")
  private boolean isReferralPromotion;

  @OneToMany(mappedBy = "promotion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<Discount> discounts;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", referencedColumnName = "id")
  @JsonBackReference
  private Event event;
}
