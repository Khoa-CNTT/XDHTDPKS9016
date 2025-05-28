package com.tourism.booking.service.impl;

import com.tourism.booking.dto.account.AccountRequest;
import com.tourism.booking.dto.account.ChangePasswordRequest;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.Role;
import com.tourism.booking.model.UserProfile;
import com.tourism.booking.repository.IAccountRepository;
import com.tourism.booking.repository.IRoleRepository;
import com.tourism.booking.repository.IUserProfileRepository;
import com.tourism.booking.service.IAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {
    IAccountRepository accountRepository;
    IRoleRepository roleRepository;
    IUserProfileRepository userProfileRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public Account register(AccountRequest accountRequest) {
        if (accountRepository.existsByUsername(accountRequest.getUsername())) {
            throw new RuntimeException("Username is exists");
        }
        if (userProfileRepository.existsByEmail(accountRequest.getEmail())) {
            throw new RuntimeException("Email is exists");
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(accountRequest.getPassword());

        Account account = new Account();
        account.setUsername(accountRequest.getUsername());
        account.setPassword(encodedPassword);
        account.setEmail(accountRequest.getEmail());
        account.setCreated_at(LocalDateTime.now());

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        account.setRoles(Set.of(userRole));

        Account savedAccount = accountRepository.save(account);

        UserProfile userProfile = new UserProfile();
        userProfile.setEmail(accountRequest.getEmail());
        userProfile.setAccount(savedAccount);
        userProfileRepository.save(userProfile);

        return savedAccount;
    }

    @Override
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username);
    }


    @Override
    public void changePassword(Integer accountId, ChangePasswordRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation do not match");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }


}
