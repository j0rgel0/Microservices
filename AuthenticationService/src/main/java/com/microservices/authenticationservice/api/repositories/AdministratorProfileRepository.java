package com.microservices.authenticationservice.api.repositories;

import java.util.UUID;

import com.microservices.authenticationservice.api.models.entities.AdministratorProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorProfileRepository extends JpaRepository<AdministratorProfileEntity, UUID> {
}