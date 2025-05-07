package com.purwafest.purwafest.event.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "event_ticket_type")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class EventTicketType {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_ticket_type_id_gen")
  @SequenceGenerator(name = "event_ticket_type_id_gen", sequenceName = "event_ticket_type_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "price", nullable = false)
  private BigInteger price;

  @NotNull
  @Column(name = "stock", nullable = false)
  private Integer stock;

  @Column(name = "available_qty")
  private Integer availableQty;

  @Column(name = "sell_date")
  private Instant sellDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_id", referencedColumnName = "id")
  @JsonBackReference
  private Event event;

  @OneToMany(mappedBy = "eventTicketType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  @OrderBy("id ASC")
  private Set<Ticket> tickets;

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
