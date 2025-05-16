package com.purwafest.purwafest.invoice.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import com.purwafest.purwafest.invoice.domain.enums.PaymentStatus;
import com.purwafest.purwafest.invoice.presentation.converter.PaymentStatusConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.math.BigInteger;
import java.time.Instant;
import java.util.Set;

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
  @SequenceGenerator(name = "invoice_id_gen", sequenceName = "invoice_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "status", columnDefinition = "payment_status")
  @Enumerated(EnumType.STRING)
  @JdbcType(PostgreSQLEnumJdbcType.class)
  private PaymentStatus status;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "payment_date")
  private Instant paymentDate;

  @Column(name = "amount")
  private BigInteger amount;

  @Column(name = "fees")
  private BigInteger fees;

  @Column(name = "final_amount")
  private BigInteger finalAmount;

  @Column(name = "row_amount_point_usage")
  private BigInteger rowAmountPointUsage;

  @Column(name = "value_point_usage")
  private BigInteger valuePointUsage;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "modified_at")
  private Instant modifiedAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<InvoiceItems> invoiceItems;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  @JsonBackReference
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", referencedColumnName = "id")
  @JsonBackReference
  private Event event;

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
