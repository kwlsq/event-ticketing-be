package com.purwafest.purwafest.event.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.purwafest.purwafest.event.domain.enums.TicketStatus;
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
@Table(name = "ticket")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Ticket {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_id_gen")
  @SequenceGenerator(name = "ticket_id_gen", sequenceName = "ticket_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "status", nullable = false)
  private TicketStatus status;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "modified_at")
  private Instant modifiedAt;

  @Column(name = "deleted_at")
  private Instant deletedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "event_ticket_type_id", referencedColumnName = "id")
  @JsonBackReference
  private EventTicketType eventTicketType;

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
