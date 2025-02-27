/*
 * @(#) $(NAME).java    1.0     2/26/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.services;


import iuh.fit.se.dtos.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 26-February-2025 10:53 PM
 */
public interface AccountService extends UserDetailsService {
    public ResponseEntity<?> signUp(AccountDTO account);
}

    