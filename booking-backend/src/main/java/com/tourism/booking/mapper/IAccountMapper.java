package com.tourism.booking.mapper;

import com.tourism.booking.dto.account.AccountRequest;
import com.tourism.booking.dto.account.AccountResponse;
import com.tourism.booking.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IAccountMapper {
    Account AccountRequestToAccount(AccountRequest accountRequest);
    AccountResponse AccountToAccountResponse(Account account);
}
