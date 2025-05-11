/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package com.backend.controllers;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 11:05 PM
 */

import com.backend.dtos.SignInRequest;
import com.backend.dtos.SignUpRequest;
import com.backend.entities.Account;
import com.backend.entities.Role;
import com.backend.repositories.AccountRepository;
import com.backend.repositories.RoleRepository;
import com.backend.services.AccountService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RepositoryRestController
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/account/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignUpRequest signUpRequest, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new LinkedHashMap<String, Object>();

            bindingResult.getFieldErrors().stream().forEach(result -> {
                errors.put(result.getField(), result.getDefaultMessage());
            });

            System.out.println(bindingResult);
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("data", accountService.signUp(signUpRequest));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

    @PostMapping("/account/sign-in")
    public ResponseEntity<?> signInUser(@RequestBody @Valid SignInRequest signInRequest, BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<String, Object>();
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new LinkedHashMap<String, Object>();

            bindingResult.getFieldErrors().stream().forEach(result -> {
                errors.put(result.getField(), result.getDefaultMessage());
            });

            System.out.println(bindingResult);
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("status", HttpStatus.OK.value());
            response.put("data", accountService.signIn(signInRequest, authenticationManager));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

//    @PostMapping("change-role/{username}")
//    public ResponseEntity<?> changeRole(@PathVariable String username, @RequestParam("role") String roleName) {
//        log.error("username = " + username);
//        Account account = accountService.findByUsername(username);
//        List<Role> roles = new ArrayList<>();
//
//        Role roleFind = roleRepository.findRoleByName(roleName);
//        if(roleFind != null)
//            roles.add(roleFind);
//
//        account.setRoles(roles);
//        accountRepository.save(account);
//
//        return ResponseEntity.status(HttpStatus.OK).body(account);
//    }


}
