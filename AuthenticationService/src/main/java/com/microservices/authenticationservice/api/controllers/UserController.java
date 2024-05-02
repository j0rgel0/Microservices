package com.microservices.authenticationservice.api.controllers;

import com.microservices.authenticationservice.api.models.dto.UserDTO;
import com.microservices.authenticationservice.api.responses.ApiResponse;
import com.microservices.authenticationservice.api.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    public static final String ALL_USERS_REL = "allUsers";

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a paginated and optionally filtered list of users.
     * GET /api/v1/users
     *
     * @param page   the page number of the pagination
     * @param size   the size of each page
     * @param search optional search query to filter users
     * @return ResponseEntity containing a page of UserDTO objects with HATEOAS links
     */
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<Page<EntityModel<UserDTO>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userPage = userService.getAllUsers(pageable, search);
        return ResponseEntity.ok(userPage.map(user -> EntityModel.of(user,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers(page, size, search)).withRel(ALL_USERS_REL))));
    }

    /**
     * Retrieves a user by their ID with HATEOAS links.
     * GET /api/v1/users/{id}
     *
     * @param id the UUID of the user
     * @return ResponseEntity containing UserDTO and HATEOAS links
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<EntityModel<UserDTO>> getUserById(@PathVariable UUID id) {
        UserDTO userDTO = userService.getUserById(id);
        EntityModel<UserDTO> resource = EntityModel.of(userDTO,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers(0, 10, null)).withRel(ALL_USERS_REL));
        return ResponseEntity.ok(resource);
    }

    /**
     * Creates a new user and includes HATEOAS links to the newly created user.
     * POST /api/v1/users
     *
     * @param userDTO the user data transfer object containing user details
     * @return ResponseEntity containing created UserDTO and a link to the user
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<EntityModel<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        EntityModel<UserDTO> resource = EntityModel.of(createdUser,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel());
        return ResponseEntity.created(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(createdUser.getId())).toUri()).body(resource);
    }

    /**
     * Updates an existing user identified by the UUID with the new data provided and includes HATEOAS links.
     * PUT /api/v1/users/{id}
     *
     * @param id      the UUID of the user to update
     * @param userDTO the user data transfer object containing updated user details
     * @return ResponseEntity containing updated UserDTO and links
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<EntityModel<UserDTO>> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        EntityModel<UserDTO> resource = EntityModel.of(updatedUser,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserById(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAllUsers(0, 10, null)).withRel(ALL_USERS_REL));
        return ResponseEntity.ok(resource);
    }

    /**
     * Deletes a user identified by UUID.
     * DELETE /api/v1/users/{id}
     *
     * @param id the UUID of the user to delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR') or hasRole('ROLE_MANAGER')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable UUID id) {
        ApiResponse apiResponse = userService.deleteUser(id);
        return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
    }

}