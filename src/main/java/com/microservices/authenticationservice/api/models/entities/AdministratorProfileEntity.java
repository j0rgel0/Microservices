package com.microservices.authenticationservice.api.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "administrator_profiles")
public class AdministratorProfileEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column
    private String department;

    @Column(name = "permissions_level")
    private String permissionsLevel;

}