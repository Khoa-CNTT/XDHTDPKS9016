package com.tourism.booking.service;

import com.tourism.booking.model.Account;

public interface IAccountService {
    Account findByUsername(String username);

}
