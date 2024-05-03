package com.microservices.authenticationservice.api.controllers;

import com.microservices.authenticationservice.api.models.dto.ManagerProfileDTO;
import com.microservices.authenticationservice.api.services.ManagerProfileService;
import com.microservices.authenticationservice.api.util.ApiConstants;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiConstants.MANAGER_PROFILES_BASE_URL)
public class ManagerProfileController {

    private final ManagerProfileService managerProfileService;

    public ManagerProfileController(final ManagerProfileService managerProfileService) {
        this.managerProfileService = managerProfileService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<EntityModel<ManagerProfileDTO>>> getAllManagerProfiles() {
        List<ManagerProfileDTO> managerProfiles = managerProfileService.getAllManagerProfiles();
        List<EntityModel<ManagerProfileDTO>> entityModels = managerProfiles.stream()
                .map(profile -> EntityModel.of(profile,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerProfileController.class).getManagerProfileByUserId(profile.getUserId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(entityModels);
    }

    /**
     * Retrieves a manager profile by the user ID with HATEOAS links.
     * GET /api/v1/manager-profiles/{userId}
     *
     * @param userId the UUID of the user associated with the profile
     * @return ResponseEntity containing ManagerProfileDTO and HATEOAS links
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<EntityModel<ManagerProfileDTO>> getManagerProfileByUserId(@PathVariable UUID userId) {
        ManagerProfileDTO managerProfile = managerProfileService.getManagerProfileByUserId(userId);
        EntityModel<ManagerProfileDTO> resource = EntityModel.of(managerProfile,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerProfileController.class).getManagerProfileByUserId(userId)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerProfileController.class).getAllManagerProfiles()).withRel("allManagerProfiles"));
        return ResponseEntity.ok(resource);
    }

    /**
     * Creates a new manager profile and includes HATEOAS links to the newly created profile.
     * POST /api/v1/manager-profiles
     *
     * @param managerProfileDTO the manager profile data transfer object containing profile details
     * @return ResponseEntity containing created ManagerProfileDTO and a link to the profile
     */
    @PostMapping("/")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<EntityModel<ManagerProfileDTO>> createManagerProfile(@RequestBody ManagerProfileDTO managerProfileDTO) {
        ManagerProfileDTO createdProfile = managerProfileService.createManagerProfile(managerProfileDTO);
        EntityModel<ManagerProfileDTO> resource = EntityModel.of(createdProfile,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerProfileController.class).getManagerProfileByUserId(createdProfile.getUserId())).withSelfRel());
        return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerProfileController.class).getManagerProfileByUserId(createdProfile.getUserId())).toUri()).body(resource);
    }

    /**
     * Updates an existing manager profile identified by the user ID with the new data provided and includes HATEOAS links.
     * PUT /api/v1/manager-profiles/{userId}
     *
     * @param userId            the UUID of the user associated with the profile to update
     * @param managerProfileDTO the manager profile data transfer object containing updated profile details
     * @return ResponseEntity containing updated ManagerProfileDTO and links
     */
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<EntityModel<ManagerProfileDTO>> updateManagerProfile(@PathVariable UUID userId, @RequestBody ManagerProfileDTO managerProfileDTO) {
        ManagerProfileDTO updatedProfile = managerProfileService.updateManagerProfile(userId, managerProfileDTO);
        EntityModel<ManagerProfileDTO> resource = EntityModel.of(updatedProfile,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerProfileController.class).getManagerProfileByUserId(userId)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerProfileController.class).getAllManagerProfiles()).withRel("allManagerProfiles"));
        return ResponseEntity.ok(resource);
    }

    /**
     * Deletes a manager profile by the user ID.
     * DELETE /api/v1/manager-profiles/{userId}
     *
     * @param userId the UUID of the user associated with the profile to delete
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void deleteManagerProfile(@PathVariable UUID userId) {
        managerProfileService.deleteManagerProfile(userId);
    }
}