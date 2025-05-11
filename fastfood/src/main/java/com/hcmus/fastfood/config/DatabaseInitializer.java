package com.hcmus.fastfood.config;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hcmus.fastfood.model.UserRole;
import com.hcmus.fastfood.repositories.UserRoleRepository;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final UserRoleRepository roleRepository;

    @Bean
    public CommandLineRunner initRoles() {
        return args -> {
            if (roleRepository.count() == 0) {
                roleRepository.saveAll(List.of(
                    UserRole.builder()
                        .name("CUSTOMER")
                        .description("Regular customer")
                        .build(),
                        
                    UserRole.builder()
                        .name("STAFF")
                        .description("Restaurant staff")
                        .canManageOrders(true)
                        .build(),
                        
                    UserRole.builder()
                        .name("MANAGER")
                        .description("Restaurant manager")
                        .canManageOrders(true)
                        .canManageMenu(true)
                        .build(),
                        
                    UserRole.builder()
                        .name("ADMIN")
                        .description("System administrator")
                        .canManageOrders(true)
                        .canManageMenu(true)
                        .canManageUsers(true)
                        .build()
                ));
            }
        };
    }
}