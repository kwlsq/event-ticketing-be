package com.purwafest.purwafest.referral.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.purwafest.purwafest.auth.domain.entities.User;
import com.purwafest.purwafest.event.domain.entities.EventTicketType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Filter;

import java.time.Instant;
import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="referral")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "referral_id_gen")
    @SequenceGenerator(name="referral_id_gen",sequenceName = "referral_id_gen",allocationSize = 1)
    @Column(name="id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referrer_id", nullable = false)
    private User referrer;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "referee_id", nullable = false)
    private User referee;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    @PreRemove
    public void preRemove() {
        deletedAt = Instant.now();
    }
}
