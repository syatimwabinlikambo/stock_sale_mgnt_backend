package com.syatimwaraph.stock_sale_mgnt_v1.config;

import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;
import com.syatimwaraph.stock_sale_mgnt_v1.enums.Role;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAdminInitializer
        implements CommandLineRunner {


    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;


    // ============================================================
    // CONFIGURATION
    // ============================================================

    @Value("${app.default-admin.enabled:true}")
    private boolean enabled;


    @Value("${app.default-admin.email:admin@bonaccueil.com}")
    private String email;


    @Value("${app.default-admin.password:Admin@123}")
    private String password;


    @Value("${app.default-admin.first-name:System}")
    private String firstName;


    @Value("${app.default-admin.last-name:Administrator}")
    private String lastName;


    // ============================================================
    // INITIALIZE
    // ============================================================

    @Override
    public void run(String... args) {

        if (!enabled) {

            return;
        }


        createDefaultAdmin();
    }


    // ============================================================
    // CREATE DEFAULT ADMIN
    // ============================================================

    private void createDefaultAdmin() {

        String normalizedEmail =
                email.trim().toLowerCase();


        /*
         * Check whether the administrator already exists.
         */

        if (appUserRepository.existsByEmail(
                normalizedEmail
        )) {

            System.out.println(
                    "Default admin already exists: "
                            + normalizedEmail
            );

            return;
        }


        /*
         * Create administrator.
         */

        AppUser admin =
                new AppUser();


        admin.setFirstName(
                firstName.trim()
        );


        admin.setLastName(
                lastName.trim()
        );


        admin.setEmail(
                normalizedEmail
        );


        /*
         * NEVER store the password as plain text.
         */

        admin.setPassword(
                passwordEncoder.encode(password)
        );


        admin.setRole(
                Role.ADMIN
        );


        admin.setEnabled(true);


        /*
         * Save administrator.
         */

        appUserRepository.save(admin);


        System.out.println(
                "================================================="
        );

        System.out.println(
                "DEFAULT ADMIN CREATED"
        );

        System.out.println(
                "Email: " + normalizedEmail
        );

        System.out.println(
                "Role: ADMIN"
        );

        System.out.println(
                "================================================="
        );
    }
}