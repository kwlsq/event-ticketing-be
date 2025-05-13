package com.purwafest.purwafest.invoice.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.purwafest.purwafest.event.domain.entities.Event;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.math.BigInteger;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "invoice_items")
public class InvoiceItems {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_items_id_gen")
  @SequenceGenerator(name = "invoice_items_id_gen", sequenceName = "invoice_items_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Column(name = "qty")
  private Integer qty;

  @NotNull
  @Column(name = "subtotal")
  private BigInteger subtotal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "invoice_id", referencedColumnName = "id")
  @JsonBackReference
  private Invoice invoice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ticket_type_id", referencedColumnName = "id")
  @JsonBackReference
  private EventTicketType eventTicketType;
}
