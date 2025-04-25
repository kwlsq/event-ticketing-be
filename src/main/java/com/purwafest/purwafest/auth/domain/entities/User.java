package com.purwafest.purwafest.auth.domain.entities;

import com.purwafest.purwafest.auth.domain.enums.UserType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Filter(name = "deletedAtFilter", condition = "deleted_at is null")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name="users_id_gen",sequenceName = "users_id_seq",allocationSize = 1)
    @Column(name="id",nullable = false)
    private Integer id;

    @Column(name="name",nullable = false,length = 255)
    private String name;

    @Column(name="email",nullable = false,length = 255)
    private String email;

    @Column(name="password",nullable = false,length = 255)
    private String password;

    @Column(name="msisdn",nullable = true,length = 20)
    private String msisdn;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    @JdbcType(value = PostgreSQLEnumJdbcType.class)
    private UserType userType;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("now()")
    @Column(name = "modified_at")
    private Instant modifiedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

//   one ticket one user

//   one user many invoice

//   one referrer one referral

//   one referral many referee

//   one user many point

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        modifiedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedAt = Instant.now();
    }

    @PreRemove
    public void preRemove() {
        deletedAt = Instant.now();
    }
}
