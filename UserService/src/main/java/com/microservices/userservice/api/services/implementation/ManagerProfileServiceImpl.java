package com.microservices.userservice.api.services.implementation;

import com.microservices.userservice.api.exceptions.ResourceNotFoundException;
import com.microservices.userservice.api.models.dto.ManagerProfileDTO;
import com.microservices.userservice.api.models.entities.ManagerProfileEntity;
import com.microservices.userservice.api.repositories.ManagerProfileRepository;
import com.microservices.userservice.api.services.ManagerProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class ManagerProfileServiceImpl implements ManagerProfileService {

    private static final String MANAGER_PROFILE_NOT_FOUND_MSG = "Manager profile";

    private final ManagerProfileRepository managerProfileRepository;
    private final MessageSource messageSource;

    public ManagerProfileServiceImpl(ManagerProfileRepository managerProfileRepository, MessageSource messageSource) {
        this.managerProfileRepository = managerProfileRepository;
        this.messageSource = messageSource;
    }

    @Override
    public List<ManagerProfileDTO> getAllManagerProfiles() {
        List<ManagerProfileEntity> managerProfileEntities = managerProfileRepository.findAll();
        return managerProfileEntities.stream().map(this::convertToDTO).toList();
    }

    @Override
    public ManagerProfileDTO getManagerProfileByUserId(UUID userId) {
        ManagerProfileEntity managerProfileEntity = managerProfileRepository.findById(userId).orElseThrow(() -> {
            String message = messageSource.getMessage("manager.profile.not.found", new Object[]{userId}, LocaleContextHolder.getLocale());
            log.warn(message);
            return new ResourceNotFoundException(message, userId);
        });
        return convertToDTO(managerProfileEntity);
    }

    @Override
    public ManagerProfileDTO createManagerProfile(ManagerProfileDTO managerProfileDTO) {
        ManagerProfileEntity managerProfileEntity = convertToEntity(managerProfileDTO);
        ManagerProfileEntity savedManagerProfileEntity = managerProfileRepository.save(managerProfileEntity);
        return convertToDTO(savedManagerProfileEntity);
    }

    @Override
    public ManagerProfileDTO updateManagerProfile(UUID userId, ManagerProfileDTO managerProfileDTO) {
        ManagerProfileEntity existingManagerProfileEntity = managerProfileRepository.findById(userId).orElseThrow(() -> {
            String message = messageSource.getMessage("manager.profile.not.found", new Object[]{userId}, LocaleContextHolder.getLocale());
            log.warn(message);
            return new ResourceNotFoundException(message, userId);
        });
        existingManagerProfileEntity.setTeamSize(managerProfileDTO.getTeamSize());
        existingManagerProfileEntity.setAreaOfResponsibility(managerProfileDTO.getAreaOfResponsibility());
        ManagerProfileEntity updatedManagerProfileEntity = managerProfileRepository.save(existingManagerProfileEntity);
        return convertToDTO(updatedManagerProfileEntity);
    }

    @Override
    public void deleteManagerProfile(UUID userId) {
        ManagerProfileEntity managerProfileEntity = managerProfileRepository.findById(userId).orElseThrow(() -> {
            String message = messageSource.getMessage("manager.profile.not.found", new Object[]{userId}, LocaleContextHolder.getLocale());
            log.warn(message);
            return new ResourceNotFoundException(message, userId);
        });
        managerProfileRepository.delete(managerProfileEntity);
    }

    // Helper methods to convert between entities and DTOs
    private ManagerProfileDTO convertToDTO(ManagerProfileEntity managerProfileEntity) {
        ManagerProfileDTO managerProfileDTO = new ManagerProfileDTO();
        managerProfileDTO.setUserId(managerProfileEntity.getUserId());
        managerProfileDTO.setTeamSize(managerProfileEntity.getTeamSize());
        managerProfileDTO.setAreaOfResponsibility(managerProfileEntity.getAreaOfResponsibility());
        return managerProfileDTO;
    }

    private ManagerProfileEntity convertToEntity(ManagerProfileDTO managerProfileDTO) {
        ManagerProfileEntity managerProfileEntity = new ManagerProfileEntity();
        managerProfileEntity.setUserId(managerProfileDTO.getUserId());
        managerProfileEntity.setTeamSize(managerProfileDTO.getTeamSize());
        managerProfileEntity.setAreaOfResponsibility(managerProfileDTO.getAreaOfResponsibility());
        return managerProfileEntity;
    }
}