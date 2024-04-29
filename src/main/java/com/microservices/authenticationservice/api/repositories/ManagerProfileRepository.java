package com.microservices.authenticationservice.api.repositories;

import java.util.UUID;

import com.microservices.authenticationservice.api.models.entities.ManagerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerProfileRepository extends JpaRepository<ManagerProfileEntity, UUID> {
}