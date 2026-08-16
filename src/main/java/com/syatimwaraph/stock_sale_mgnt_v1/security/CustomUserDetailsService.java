package com.syatimwaraph.stock_sale_mgnt_v1.security;


import com.syatimwaraph.stock_sale_mgnt_v1.entity.AppUser;
import com.syatimwaraph.stock_sale_mgnt_v1.repositories.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    public CustomUserDetailsService(
            AppUserRepository appUserRepository
    ) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        AppUser user = appUserRepository.findByEmail(
                email.toLowerCase()
        ).orElseThrow(() ->
                new UsernameNotFoundException(
                        "User not found"
                )
        );

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .disabled(!user.isEnabled())
                .build();
    }
}