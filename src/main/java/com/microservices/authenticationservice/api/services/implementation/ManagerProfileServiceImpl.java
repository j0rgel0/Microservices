package com.microservices.authenticationservice.api.services.implementation;

import com.microservices.authenticationservice.api.exceptions.ResourceNotFoundException;
import com.microservices.authenticationservice.api.models.dto.ManagerProfileDTO;
import com.microservices.authenticationservice.api.models.entities.ManagerProfileEntity;
import com.microservices.authenticationservice.api.repositories.ManagerProfileRepository;
import com.microservices.authenticationservice.api.services.ManagerProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ManagerProfileServiceImpl implements ManagerProfileService {

    private static final String MANAGER_PROFILE_NOT_FOUND_MSG = "Manager profile";

    private final ManagerProfileRepository managerProfileRepository;

    public ManagerProfileServiceImpl(ManagerProfileRepository managerProfileRepository) {
        this.managerProfileRepository = managerProfileRepository;
    }

    @Override
    public List<ManagerProfileDTO> getAllManagerProfiles() {
        List<ManagerProfileEntity> managerProfileEntities = managerProfileRepository.findAll();
        return managerProfileEntities.stream().map(this::convertToDTO).toList();
    }

    @Override
    public ManagerProfileDTO getManagerProfileByUserId(UUID userId) {
        ManagerProfileEntity managerProfileEntity = managerProfileRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(MANAGER_PROFILE_NOT_FOUND_MSG, userId));
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
        ManagerProfileEntity existingManagerProfileEntity = managerProfileRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(MANAGER_PROFILE_NOT_FOUND_MSG, userId));
        existingManagerProfileEntity.setTeamSize(managerProfileDTO.getTeamSize());
        // Update other fields as needed
        ManagerProfileEntity updatedManagerProfileEntity = managerProfileRepository.save(existingManagerProfileEntity);
        return convertToDTO(updatedManagerProfileEntity);
    }

    @Override
    public void deleteManagerProfile(UUID userId) {
        ManagerProfileEntity managerProfileEntity = managerProfileRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(MANAGER_PROFILE_NOT_FOUND_MSG, userId));
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