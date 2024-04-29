package com.microservices.authenticationservice.api.services.implementation;

import com.microservices.authenticationservice.api.exceptions.ResourceNotFoundException;
import com.microservices.authenticationservice.api.models.dto.AdministratorProfileDTO;
import com.microservices.authenticationservice.api.models.entities.AdministratorProfileEntity;
import com.microservices.authenticationservice.api.repositories.AdministratorProfileRepository;
import com.microservices.authenticationservice.api.services.AdministratorProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdministratorProfileServiceImpl implements AdministratorProfileService {

    private static final String ADMIN_PROFILE_NOT_FOUND_MSG = "Administrator profile";

    private final AdministratorProfileRepository adminProfileRepository;

    public AdministratorProfileServiceImpl(AdministratorProfileRepository adminProfileRepository) {
        this.adminProfileRepository = adminProfileRepository;
    }

    @Override
    public List<AdministratorProfileDTO> getAllAdminProfiles() {
        List<AdministratorProfileEntity> adminProfiles = adminProfileRepository.findAll();
        return adminProfiles.stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public AdministratorProfileDTO getAdminProfileByUserId(UUID userId) {
        AdministratorProfileEntity adminProfile = adminProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ADMIN_PROFILE_NOT_FOUND_MSG, userId));
        return convertToDTO(adminProfile);
    }

    @Override
    public AdministratorProfileDTO createAdminProfile(AdministratorProfileDTO adminProfileDTO) {
        AdministratorProfileEntity adminProfile = convertToEntity(adminProfileDTO);
        AdministratorProfileEntity savedAdminProfile = adminProfileRepository.save(adminProfile);
        return convertToDTO(savedAdminProfile);
    }

    @Override
    public AdministratorProfileDTO updateAdminProfile(UUID userId, AdministratorProfileDTO adminProfileDTO) {
        AdministratorProfileEntity existingAdminProfile = adminProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ADMIN_PROFILE_NOT_FOUND_MSG, userId));
        existingAdminProfile.setDepartment(adminProfileDTO.getDepartment());
        existingAdminProfile.setPermissionsLevel(adminProfileDTO.getPermissionsLevel());
        AdministratorProfileEntity updatedAdminProfile = adminProfileRepository.save(existingAdminProfile);
        return convertToDTO(updatedAdminProfile);
    }

    @Override
    public void deleteAdminProfile(UUID userId) {
        AdministratorProfileEntity adminProfile = adminProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ADMIN_PROFILE_NOT_FOUND_MSG, userId));
        adminProfileRepository.delete(adminProfile);
    }

    // Helper methods to convert between entities and DTOs
    private AdministratorProfileDTO convertToDTO(AdministratorProfileEntity adminProfile) {
        AdministratorProfileDTO adminProfileDTO = new AdministratorProfileDTO();
        adminProfileDTO.setUserId(adminProfile.getUserId());
        adminProfileDTO.setDepartment(adminProfile.getDepartment());
        adminProfileDTO.setPermissionsLevel(adminProfile.getPermissionsLevel());
        return adminProfileDTO;
    }

    private AdministratorProfileEntity convertToEntity(AdministratorProfileDTO adminProfileDTO) {
        AdministratorProfileEntity adminProfile = new AdministratorProfileEntity();
        adminProfile.setUserId(adminProfileDTO.getUserId());
        adminProfile.setDepartment(adminProfileDTO.getDepartment());
        adminProfile.setPermissionsLevel(adminProfileDTO.getPermissionsLevel());
        return adminProfile;
    }
}