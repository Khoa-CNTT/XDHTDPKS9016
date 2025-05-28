package com.tourism.booking.repository;

import com.tourism.booking.dto.authentication.UserAuthResponse;
import com.tourism.booking.model.Account;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<Account> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Account set password = :password where email = :email")
    void updatePassword(String email, String password);

    @Query(value = """
            select acc.account_id, acc.username, r.role_name from booking_management.account acc
            join booking_management.role_account ra on acc.account_id = ra.account_id
            join booking_management.role r on ra.role_id = r.role_id
            where acc.username = :username
            """, nativeQuery = true)
    UserAuthResponse getUserAuthResponse(String username);
}
