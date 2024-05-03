package com.microservices.userservice.api.repositories;

import java.util.UUID;

import com.microservices.userservice.api.models.entities.ManagerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerProfileRepository extends JpaRepository<ManagerProfileEntity, UUID> {
}