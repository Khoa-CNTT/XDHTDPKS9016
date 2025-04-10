package com.tourism.booking.service;

import com.tourism.booking.dto.account.AccountRequest;
import com.tourism.booking.model.Account;

public interface IAccountService {
    Account register(AccountRequest accountRequest);

    Account getAccountByUsername(String username);

}
