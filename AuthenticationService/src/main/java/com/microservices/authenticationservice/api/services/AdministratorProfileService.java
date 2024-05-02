package com.microservices.authenticationservice.api.services;

import com.microservices.authenticationservice.api.models.dto.AdministratorProfileDTO;

import java.util.List;
import java.util.UUID;

public interface AdministratorProfileService {
    List<AdministratorProfileDTO> getAllAdminProfiles();

    AdministratorProfileDTO getAdminProfileByUserId(UUID userId);

    AdministratorProfileDTO createAdminProfile(AdministratorProfileDTO adminProfileDTO);

    AdministratorProfileDTO updateAdminProfile(UUID userId, AdministratorProfileDTO adminProfileDTO);

    void deleteAdminProfile(UUID userId);
}