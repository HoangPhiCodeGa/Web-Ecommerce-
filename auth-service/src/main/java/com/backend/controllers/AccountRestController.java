package com.backend.controllers;

import com.backend.entities.Account;
import com.backend.entities.Role;
import com.backend.repositories.AccountRepository;
import com.backend.repositories.RoleRepository;
import com.backend.services.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountRestController {
    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/api/account/change-role/{username}")
    public ResponseEntity<?> changeRole(@PathVariable String username,
                                        @RequestBody List<String> roleName) {
        Account account = accountService.findByUsername(username);

        List<Role> roles = new ArrayList<>();

        if (roleName != null) {
            for (String name : roleName) {
                Role roleFind = roleRepository.findRoleByName(name);
                if (roleFind != null)
                    roles.add(roleFind);

            }
        }
        account.setRoles(roles);
        accountRepository.save(account);

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

}
