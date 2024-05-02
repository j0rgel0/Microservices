package com.microservices.authenticationservice.api.repositories;

import com.microservices.authenticationservice.api.models.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * Finds users by part of their email, first name, or last name.
     * This method supports pagination.
     *
     * @param email     the email to search for.
     * @param firstName the first name to search for.
     * @param lastName  the last name to search for.
     * @param pageable  the pagination information.
     * @return a page of User entities that match the search criteria.
     */
    Page<UserEntity> findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String email, String firstName, String lastName, Pageable pageable);

}