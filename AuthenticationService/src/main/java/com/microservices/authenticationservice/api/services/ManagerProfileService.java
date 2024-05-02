package com.microservices.authenticationservice.api.services;

import com.microservices.authenticationservice.api.models.dto.ManagerProfileDTO;

import java.util.List;
import java.util.UUID;

public interface ManagerProfileService {
    List<ManagerProfileDTO> getAllManagerProfiles();

    ManagerProfileDTO getManagerProfileByUserId(UUID userId);

    ManagerProfileDTO createManagerProfile(ManagerProfileDTO managerProfileDTO);

    ManagerProfileDTO updateManagerProfile(UUID userId, ManagerProfileDTO managerProfileDTO);

    void deleteManagerProfile(UUID userId);
}