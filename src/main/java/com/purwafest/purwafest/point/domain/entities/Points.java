package com.purwafest.purwafest.point.domain.entities;

import com.purwafest.purwafest.auth.domain.entities.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.Filter;

import java.math.BigInteger;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="point")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Points {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "point_id_gen")
    @SequenceGenerator(name="point_id_gen",sequenceName = "point_id_gen",allocationSize = 1)
    @Column(name="id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @NotNull
    @Column(name = "amount", nullable = false)
    private BigInteger amount;

    @NotNull
    @Column(name = "is_redeemed", nullable = false)
    private boolean isRedeemed;

    @NotNull
    @Column(name = "point_resource", nullable = false)
    private String pointResource;

    @NotNull
    @Column(name = "expired_at", nullable = false)
    private Instant expiredAt;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "modified_at")
    private Instant modifiedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }
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
