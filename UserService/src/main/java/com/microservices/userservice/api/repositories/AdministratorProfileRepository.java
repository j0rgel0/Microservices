package com.microservices.userservice.api.repositories;

import java.util.UUID;

import com.microservices.userservice.api.models.entities.AdministratorProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministratorProfileRepository extends JpaRepository<AdministratorProfileEntity, UUID> {
}