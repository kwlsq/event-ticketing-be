package com.purwafest.purwafest.point.domain.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.invoice.domain.entities.Invoice;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.time.Instant;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="point_history")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_history_id_gen")
    @SequenceGenerator(name="point_history_id_gen",sequenceName = "point_history_id_seq",allocationSize = 1)
    @Column(name="id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", referencedColumnName = "id")
    private Invoice invoice;

    @NotNull
    @Column(name = "usage_count", nullable = false)
    private Integer usageCount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false)
    private Instant modifiedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.modifiedAt = now;
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
