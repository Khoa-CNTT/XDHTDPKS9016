package com.tourism.booking.service.impl;

import com.tourism.booking.dto.account.AccountRequest;
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

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {
    IAccountRepository accountRepository;
    IRoleRepository roleRepository;
    IUserProfileRepository userProfileRepository;

    @Override
    public Account register(AccountRequest accountRequest) {
        if (accountRepository.existsByUsername(accountRequest.getUsername())) {
            throw new RuntimeException("Username is exists");
        }
        if (userProfileRepository.existsByEmail(accountRequest.getEmail())) {
            throw new RuntimeException("Email is exists");
        }
        // Mã hóa password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(accountRequest.getPassword());

        // Tạo account
        Account account = new Account();
        account.setUsername(accountRequest.getUsername());
        account.setPassword(encodedPassword);
        account.setEmail(accountRequest.getEmail());

        // Set vai trò mặc định USER
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        account.setRoles(Set.of(userRole));

        // Lưu account để có ID
        Account savedAccount = accountRepository.save(account);

        // Tạo và lưu UserProfile
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

}
