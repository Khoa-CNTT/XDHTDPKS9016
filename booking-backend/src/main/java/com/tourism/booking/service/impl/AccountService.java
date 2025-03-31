package com.tourism.booking.service.impl;

import com.tourism.booking.model.Account;
import com.tourism.booking.repository.IAccountRepository;
import com.tourism.booking.service.IAccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService implements IAccountService {
    IAccountRepository accountRepository;


    @Override
    public Account findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }
}
