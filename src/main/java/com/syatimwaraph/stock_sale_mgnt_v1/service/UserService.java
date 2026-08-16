package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.AdminCreateUserRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.UserResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.EmailAlreadyExistsException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponse> getAllUsers() {

        return appUserRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public UserResponse createUser(
            AdminCreateUserRequest request
    ) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (appUserRepository.existsByEmail(email)) {

            throw new EmailAlreadyExistsException(
                    "Email is already registered"
            );
        }

        AppUser user = new AppUser();

        user.setFirstName(
                request.getFirstName().trim()
        );

        user.setLastName(
                request.getLastName().trim()
        );

        user.setEmail(email);

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(request.getRole());
        user.setEnabled(true);

        AppUser saved =
                appUserRepository.save(user);

        return mapToResponse(saved);
    }

    public UserResponse updateStatus(
            Long userId,
            boolean enabled
    ) {

        AppUser user =
                appUserRepository.findById(userId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        user.setEnabled(enabled);

        AppUser saved =
                appUserRepository.save(user);

        return mapToResponse(saved);
    }

    private UserResponse mapToResponse(
            AppUser user
    ) {

        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled()
        );
    }
}