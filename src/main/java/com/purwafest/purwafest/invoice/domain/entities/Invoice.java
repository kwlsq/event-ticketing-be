package com.purwafest.purwafest.invoice.domain.entities;

import com.purwafest.purwafest.invoice.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.math.BigInteger;
import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoice")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Invoice {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_gen")
  @SequenceGenerator(name = "invoiceid_gen", sequenceName = "invoice_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "status")
  private PaymentStatus status;

  @NotNull
  @Column(name = "payment_method")
  private String paymentMethod;

  @NotNull
  @Column(name = "payment_date")
  private Instant paymentDate;

  @NotNull
  @Column(name = "amount")
  private BigInteger amount;

  @NotNull
  @Column(name = "fees")
  private BigInteger fees;

  @NotNull
  @Column(name = "final_amount")
  private BigInteger finalAmount;

  @NotNull
  @Column(name = "row_amount_point_usage")
  private BigInteger rowAmountPointUsage;

  @NotNull
  @Column(name = "value_point_usage")
  private BigInteger valuePointUsage;

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
