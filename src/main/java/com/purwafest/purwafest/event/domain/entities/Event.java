package com.purwafest.purwafest.event.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.event.domain.enums.EventStatus;
import com.purwafest.purwafest.promotion.domain.entities.Promotion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@Table(name = "event")
@Filter(name = "deletedAtFilter", condition = "deleted_at IS NULL")
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_gen")
  @SequenceGenerator(name = "event_id_gen", sequenceName = "event_id_seq", allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @NotNull
  @Size(max = 255)
  @Column(name = "name", nullable = false)
  private String name;

  @NotNull
  @Column(name = "description")
  private String description;

  @Column(name = "date")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private Instant date;

  @NotNull
  @Column(name = "venue")
  private String venue;

  @NotNull
  @Column(name = "location", nullable = false)
  private String location;

  @NotNull
  @Column(name = "is_free", nullable = false)
  private boolean isEventFree;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private EventStatus status;

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  @OrderBy("id ASC")
  private Set<EventTicketType> ticketTypes;

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<Image> images;

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<Review> reviews;

  @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private Set<Promotion> promotions;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organizer_id", referencedColumnName = "id")
  @JsonBackReference
  private User user;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", referencedColumnName = "id")
  @JsonBackReference
  private Category category;

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
