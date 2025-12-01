package com.user.core;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table(name = "User")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_NAME", nullable = false, unique = true, updatable = false)
    private String username;

    @Column(name = "PWD", nullable = false)
    private String password;

    @Column(name = "IS_ENABLED", nullable = false)
    private boolean enabled;

    @Column(name = "ROLES")
    private String roles;

    @Column(name = "USER_EMAIL")
    private String email;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @CreationTimestamp
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @UpdateTimestamp
    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
}
