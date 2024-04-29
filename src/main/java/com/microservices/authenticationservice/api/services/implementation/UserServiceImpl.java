package com.microservices.authenticationservice.api.services.implementation;

import com.microservices.authenticationservice.api.exceptions.*;
import com.microservices.authenticationservice.api.models.dto.UserDTO;
import com.microservices.authenticationservice.api.models.entities.UserEntity;
import com.microservices.authenticationservice.api.repositories.UserRepository;
import com.microservices.authenticationservice.api.responses.ApiResponse;
import com.microservices.authenticationservice.api.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private MessageSource messageSource;

    private static final String EMAIL_REQUIRED_MSG = "email.required";
    private static final String INVALID_EMAIL_MSG = "invalid.email";
    private static final String PASSWORD_REQUIRED_MSG = "password.required";
    private static final String PASSWORD_INVALID_MSG = "password.invalid";
    private static final String RESOURCE_NOT_FOUND_MSG = "resource.not.found";
    private static final String EMAIL_EXISTS_MSG = "email.exists";

    public UserServiceImpl(UserRepository userRepository, MessageSource messageSource) {
        this.userRepository = userRepository;
        this.messageSource = messageSource;
    }

    /**
     * Gets all users with pagination and optional filtering.
     *
     * @param pageable the pagination and sorting information.
     * @param search   the filter criteria (optional).
     * @return a page of UserDTO.
     */
    public Page<UserDTO> getAllUsers(Pageable pageable, String search) {
        Page<UserEntity> users;
        if (search != null && !search.isEmpty()) {
            users = userRepository.findByEmailContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search, search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(this::convertToDTO);
    }

    /**
     * Retrieves a user by its ID.
     *
     * @param id The ID of the user to retrieve.
     * @return A UserDTO object representing the user.
     * @throws ResourceNotFoundException If the user with the given ID is not found.
     */
    @Override
    public UserDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> {
                    String message = messageSource.getMessage(RESOURCE_NOT_FOUND_MSG, new Object[]{"User"}, LocaleContextHolder.getLocale());
                    String details = "The requested User with ID: " + id + " was not found.";
                    log.warn(details);
                    return new ResourceNotFoundException(details, id);
                });
    }

    /**
     * Creates a new user.
     *
     * @param userDTO The UserDTO object containing the user data.
     * @return A UserDTO object representing the newly created user.
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        validateUserDTO(userDTO);
        checkEmailExists(userDTO.getEmail());
        UserEntity userEntity = convertToEntity(userDTO);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return convertToDTO(savedUserEntity);
    }

    /**
     * Updates an existing user.
     *
     * @param id      The ID of the user to update.
     * @param userDTO The UserDTO object containing the updated user data.
     * @return A UserDTO object representing the updated user.
     * @throws ResourceNotFoundException If the user with the given ID is not found.
     */
    @Override
    public UserDTO updateUser(UUID id, UserDTO userDTO) throws ResourceNotFoundException {
        return userRepository.findById(id)
                .map(existingUserEntity -> {
                    if (!existingUserEntity.getEmail().equals(userDTO.getEmail())) {
                        checkEmailExists(userDTO.getEmail());
                    }
                    validateUserDTO(userDTO);
                    updateUserEntity(existingUserEntity, userDTO);
                    UserEntity updatedUserEntity = userRepository.save(existingUserEntity);
                    return convertToDTO(updatedUserEntity);
                })
                .orElseThrow(() -> createResourceNotFoundException("User", id));
    }

    /**
     * Deletes a user.
     *
     * @param id The ID of the user to delete.
     * @throws ResourceNotFoundException If the user with the given ID is not found.
     */
    @Override
    public ApiResponse deleteUser(UUID id) {
        try {
            if (id == null) {
                throw new IllegalArgumentException("User ID cannot be null");
            }

            Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
            if (optionalUserEntity.isPresent()) {
                UserEntity userEntity = optionalUserEntity.get();
                userRepository.delete(userEntity);
                String message = messageSource.getMessage("user.deleted", new Object[]{id}, LocaleContextHolder.getLocale());
                log.info(message);
                return new ApiResponse(HttpStatus.OK, message, null);
            } else {
                String message = messageSource.getMessage("user.not.found", new Object[]{id}, LocaleContextHolder.getLocale());
                log.warn(message);
                throw new ResourceNotFoundException(message, id);
            }
        } catch (IllegalArgumentException e) {
            String message = messageSource.getMessage("invalid.uuid", new Object[]{id}, LocaleContextHolder.getLocale());
            log.warn(message);
            throw new IllegalArgumentException(message);
        }
    }
    /**
     * Converts a User entity to a UserDTO object.
     *
     * @param userEntity The User entity to convert.
     * @return A UserDTO object representing the User entity.
     */
    private UserDTO convertToDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setFirstName(userEntity.getFirstName());
        userDTO.setLastName(userEntity.getLastName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setCreationDate(userEntity.getCreationDate());
        userDTO.setLastUpdate(userEntity.getLastUpdate());
        userDTO.setSoftDelete(userEntity.isSoftDelete());
        userDTO.setUserType(userEntity.getUserType());
        return userDTO;
    }

    /**
     * Converts a UserDTO object to a User entity.
     *
     * @param userDTO The UserDTO object to convert.
     * @return A User entity representing the UserDTO object.
     */
    private UserEntity convertToEntity(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setCreationDate(userDTO.getCreationDate());
        userEntity.setLastUpdate(userDTO.getLastUpdate());
        userEntity.setSoftDelete(userDTO.isSoftDelete());
        userEntity.setUserType(userDTO.getUserType());
        return userEntity;
    }

    public String authenticateUser(String email, String password) {
        UserEntity user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return "generated-token-string";  // Placeholder for a session token or similar authentication token
        }
        return null;  // Return null if authentication fails
    }

    private void validateUserDTO(UserDTO userDTO) {
        validateEmail(userDTO.getEmail());
        validatePassword(userDTO.getPassword());
    }

    private void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw createEmptyEmailException(email);
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw createInvalidEmailException(email);
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw createEmptyPasswordException();
        }
        if (password.length() < 8 || password.length() > 20) {
            throw createInvalidPasswordException(password);
        }
    }

    private void checkEmailExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw createDuplicateEmailException(email);
        }
    }

    private void updateUserEntity(UserEntity userEntity, UserDTO userDTO) {
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
    }

    private EmptyEmailException createEmptyEmailException(String email) {
        String message = messageSource.getMessage(EMAIL_REQUIRED_MSG, null, LocaleContextHolder.getLocale());
        log.warn("The email field is required.");
        return new EmptyEmailException(message, email);
    }

    private InvalidEmailException createInvalidEmailException(String email) {
        String message = messageSource.getMessage(INVALID_EMAIL_MSG, new Object[]{email}, LocaleContextHolder.getLocale());
        log.warn("The provided email address '{}' is invalid.", email);
        return new InvalidEmailException(message, email);
    }

    private EmptyPasswordException createEmptyPasswordException() {
        String message = messageSource.getMessage(PASSWORD_REQUIRED_MSG, null, LocaleContextHolder.getLocale());
        log.warn("The password field is required.");
        return new EmptyPasswordException(message);
    }

    private InvalidPasswordException createInvalidPasswordException(String password) {
        String message = messageSource.getMessage(PASSWORD_INVALID_MSG, new Object[]{password.length(), 8, 20}, LocaleContextHolder.getLocale());
        log.warn("The provided password is invalid. Password must be between 8 and 20 characters long.");
        return new InvalidPasswordException(message, password, password.length());
    }

    private DuplicateEmailException createDuplicateEmailException(String email) {
        String message = messageSource.getMessage(EMAIL_EXISTS_MSG, new Object[]{email}, LocaleContextHolder.getLocale());
        log.warn("A user with the email '{}' already exists.", email);
        return new DuplicateEmailException(message, email);
    }

    private ResourceNotFoundException createResourceNotFoundException(String resourceName, UUID id) {
        String message = messageSource.getMessage(RESOURCE_NOT_FOUND_MSG, new Object[]{resourceName, id}, LocaleContextHolder.getLocale());
        log.warn("The requested {} with ID: {} was not found.", resourceName, id);
        return new ResourceNotFoundException(message, id);
    }

}