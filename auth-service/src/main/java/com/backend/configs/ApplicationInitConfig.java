package com.backend.configs;

import com.backend.dtos.SignUpRequest;
import com.backend.entities.Role;
import com.backend.repositories.AccountRepository;
import com.backend.repositories.RoleRepository;
import com.backend.services.AccountService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Slf4j
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationInitConfig {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    RoleRepository roleRepository;


    @Bean
    ApplicationRunner applicationRunner() {
        return args -> {
            this.createAnyRoles();
            this.createAccountAdmin();
            this.createAccountUser();
        };
    }

    private void createAnyRoles() {
        try {
            if (roleRepository.findRoleByName("ADMIN") == null) {
                Role roleAdmin = new Role();
                roleAdmin.setName("ADMIN");
                roleRepository.save(roleAdmin);
            }

            if (roleRepository.findRoleByName("USER") == null) {
                Role roleUser = new Role();
                roleUser.setName("USER");
                roleRepository.save(roleUser);
            }
            log.warn("init default roles ok!");
        } catch (Exception e) {
            log.error("Cannot init default role: " + e.getMessage());
        }
    }

    public void createAccountAdmin() {
        try {
            String defaultAdminUsername = "admin";
            String defaultAdminEmail = "admin@gmail.com";
            String defaultAdminPassword = "1234";

            if (accountRepository.count() == 0) {

                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.setUsername(defaultAdminUsername);
                signUpRequest.setPassword(defaultAdminPassword);
                signUpRequest.setEmail(defaultAdminEmail);

                Role adminRole = roleRepository.findRoleByName("ADMIN");
                signUpRequest.setRoles(Collections.singletonList(adminRole));

                accountService.signUp(signUpRequest);

                log.info("Default admin account created!");
            } else {
                log.info("Default admin account already exists.");
            }
        } catch (Exception e) {
            log.error("Cannot init default account admin: " + e.getMessage());
        }
    }

    public void createAccountUser() {
        try {
            String defaultAdminUsername = "user";
            String defaultAdminEmail = "user@gmail.com";
            String defaultAdminPassword = "1234";

            if (accountRepository.count() <= 1) {

                SignUpRequest signUpRequest = new SignUpRequest();
                signUpRequest.setUsername(defaultAdminUsername);
                signUpRequest.setPassword(defaultAdminPassword);
                signUpRequest.setEmail(defaultAdminEmail);

                Role adminRole = roleRepository.findRoleByName("USER");
                signUpRequest.setRoles(Collections.singletonList(adminRole));

                accountService.signUp(signUpRequest);

                log.info("Default user account created!");
            } else {
                log.info("Default user account already exists.");
            }
        } catch (Exception e) {
            log.error("Cannot init default account user: " + e.getMessage());
        }
    }
}
