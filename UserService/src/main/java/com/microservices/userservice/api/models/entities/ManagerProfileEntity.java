package com.microservices.userservice.api.models.entities;

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
@Table(name = "manager_profiles")
public class ManagerProfileEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "team_size")
    private Integer teamSize;

    @Column(name = "area_of_responsibility")
    private String areaOfResponsibility;

}