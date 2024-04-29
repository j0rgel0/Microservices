package com.microservices.authenticationservice.api.controllers;

import com.microservices.authenticationservice.api.models.dto.AdministratorProfileDTO;
import com.microservices.authenticationservice.api.services.AdministratorProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin-profiles")
@Slf4j
public class AdministratorProfileController {

    private final AdministratorProfileService adminProfileService;

    public AdministratorProfileController(final AdministratorProfileService adminProfileService) {
        this.adminProfileService = adminProfileService;
    }

    /**
     * Retrieves a paginated and optionally filtered list of administrator profiles.
     * GET /api/v1/admin-profiles
     * @param page the page number of the pagination (optional)
     * @param size the size of each page (optional)
     * @param search optional search query to filter profiles (optional)
     * @return ResponseEntity containing a list of AdministratorProfileDTO objects with HATEOAS links
     */
    @GetMapping
    public ResponseEntity<List<EntityModel<AdministratorProfileDTO>>> getAllAdminProfiles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        List<AdministratorProfileDTO> adminProfiles = adminProfileService.getAllAdminProfiles();
        List<EntityModel<AdministratorProfileDTO>> entityModels = adminProfiles.stream()
                .map(profile -> EntityModel.of(profile,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdministratorProfileController.class).getAdminProfileByUserId(profile.getUserId())).withSelfRel()))
                .toList();
        return ResponseEntity.ok(entityModels);
    }

    /**
     * Retrieves an administrator profile by the user ID with HATEOAS links.
     * GET /api/v1/admin-profiles/{userId}
     * @param userId the UUID of the user associated with the profile
     * @return ResponseEntity containing AdministratorProfileDTO and HATEOAS links
     */
    @GetMapping("/{userId}")
    public ResponseEntity<EntityModel<AdministratorProfileDTO>> getAdminProfileByUserId(@PathVariable UUID userId) {
        AdministratorProfileDTO adminProfile = adminProfileService.getAdminProfileByUserId(userId);
        if (adminProfile == null) {
            return ResponseEntity.notFound().build();
        }
        EntityModel<AdministratorProfileDTO> resource = EntityModel.of(adminProfile,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdministratorProfileController.class).getAdminProfileByUserId(userId)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdministratorProfileController.class).getAllAdminProfiles(0, 10, null)).withRel("allAdminProfiles"));
        return ResponseEntity.ok(resource);
    }

    /**
     * Creates a new administrator profile and includes HATEOAS links to the newly created profile.
     * POST /api/v1/admin-profiles
     * @param adminProfileDTO the administrator profile data transfer object containing profile details
     * @return ResponseEntity containing created AdministratorProfileDTO and a link to the profile
     */
    @PostMapping
    public ResponseEntity<EntityModel<AdministratorProfileDTO>> createAdminProfile(@RequestBody AdministratorProfileDTO adminProfileDTO) {
        AdministratorProfileDTO createdProfile = adminProfileService.createAdminProfile(adminProfileDTO);
        EntityModel<AdministratorProfileDTO> resource = EntityModel.of(createdProfile,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdministratorProfileController.class).getAdminProfileByUserId(createdProfile.getUserId())).withSelfRel());
        return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdministratorProfileController.class).getAdminProfileByUserId(createdProfile.getUserId())).toUri()).body(resource);
    }

    /**
     * Updates an existing administrator profile identified by the user ID with the new data provided and includes HATEOAS links.
     * PUT /api/v1/admin-profiles/{userId}
     * @param userId the UUID of the user associated with the profile to update
     * @param adminProfileDTO the administrator profile data transfer object containing updated profile details
     * @return ResponseEntity containing updated AdministratorProfileDTO and links
     */
    @PutMapping("/{userId}")
    public ResponseEntity<EntityModel<AdministratorProfileDTO>> updateAdminProfile(@PathVariable UUID userId, @RequestBody AdministratorProfileDTO adminProfileDTO) {
        AdministratorProfileDTO updatedProfile = adminProfileService.updateAdminProfile(userId, adminProfileDTO);
        EntityModel<AdministratorProfileDTO> resource = EntityModel.of(updatedProfile,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdministratorProfileController.class).getAdminProfileByUserId(userId)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AdministratorProfileController.class).getAllAdminProfiles(0, 10, null)).withRel("allAdminProfiles"));
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{userId}")
    public void deleteAdminProfile(@PathVariable UUID userId) {
        adminProfileService.deleteAdminProfile(userId);
    }
}