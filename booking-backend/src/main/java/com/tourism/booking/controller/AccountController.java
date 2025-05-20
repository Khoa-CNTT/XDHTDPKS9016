package com.tourism.booking.controller;

import com.tourism.booking.dto.account.ChangePasswordRequest;
import com.tourism.booking.model.Account;
import com.tourism.booking.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal
    ) {
        Account account = accountService.getAccountByUsername(principal.getName());
        accountService.changePassword(account.getAccount_id().intValue(), request);
        return ResponseEntity.ok("Password changed successfully");
    }
}
