package com.syatimwaraph.stock_sale_mgnt_v1.service;

import com.syatimwaraph.stock_sale_mgnt_v1.dto.AdminCreateUserRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.AuthResponse;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.LoginRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.dto.SignupRequest;
import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.Role;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.AccountDisabledException;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.EmailAlreadyExistsException;
import com.syatimwaraph.stock_sale_mgnt_v1.exception.InvalidCredentialsException;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.AppUserRepository;
import com.syatimwaraph.stock_sale_mgnt_v1.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public AuthResponse signup(SignupRequest request) {

        String email =
                request.getEmail()
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

        user.setRole(Role.USER);
        user.setEnabled(true);

        AppUser savedUser =
                appUserRepository.save(user);

        String token =
                jwtService.generateToken(savedUser);

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthResponse createUser(
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

        AppUser savedUser =
                appUserRepository.save(user);

        return new AuthResponse(
                null,
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    public AuthResponse login(LoginRequest request) {

        String email =
                request.getEmail()
                        .trim()
                        .toLowerCase();

        AppUser user =
                appUserRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new InvalidCredentialsException(
                                        "Invalid email or password"
                                )
                        );

        if (!user.isEnabled()) {

            throw new AccountDisabledException(
                    "User account is disabled"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token =
                jwtService.generateToken(user);

        return new AuthResponse(
                token,
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }
}