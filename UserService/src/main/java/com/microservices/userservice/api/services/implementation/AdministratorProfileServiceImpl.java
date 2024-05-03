package com.microservices.userservice.api.services.implementation;

import com.microservices.userservice.api.exceptions.ResourceNotFoundException;
import com.microservices.userservice.api.models.dto.AdministratorProfileDTO;
import com.microservices.userservice.api.models.entities.AdministratorProfileEntity;
import com.microservices.userservice.api.repositories.AdministratorProfileRepository;
import com.microservices.userservice.api.services.AdministratorProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class AdministratorProfileServiceImpl implements AdministratorProfileService {

    private static final String ADMIN_PROFILE_NOT_FOUND_MSG = "Administrator profile";
    private static final String RESOURCE_NOT_FOUND_KEY = "resource.not.found.admin.profile";

    private final AdministratorProfileRepository adminProfileRepository;
    private final MessageSource messageSource;

    public AdministratorProfileServiceImpl(AdministratorProfileRepository adminProfileRepository, MessageSource messageSource) {
        this.adminProfileRepository = adminProfileRepository;
        this.messageSource = messageSource;
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
                .orElseThrow(() -> {
                    String message = messageSource.getMessage("resource.not.found.admin.profile", new Object[]{userId}, LocaleContextHolder.getLocale());
                    log.warn(message);
                    return new ResourceNotFoundException(message, userId);
                });
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
                .orElseThrow(() -> {
                    String message = messageSource.getMessage(RESOURCE_NOT_FOUND_KEY, new Object[]{userId}, LocaleContextHolder.getLocale());
                    log.warn(message);
                    return new ResourceNotFoundException(message, userId);
                });
        existingAdminProfile.setDepartment(adminProfileDTO.getDepartment());
        existingAdminProfile.setPermissionsLevel(adminProfileDTO.getPermissionsLevel());
        AdministratorProfileEntity updatedAdminProfile = adminProfileRepository.save(existingAdminProfile);
        return convertToDTO(updatedAdminProfile);
    }

    @Override
    public void deleteAdminProfile(UUID userId) {
        AdministratorProfileEntity adminProfile = adminProfileRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = messageSource.getMessage("resource.not.found.admin.profile", new Object[]{userId}, LocaleContextHolder.getLocale());
                    log.warn(message);
                    return new ResourceNotFoundException(message, userId);
                });
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