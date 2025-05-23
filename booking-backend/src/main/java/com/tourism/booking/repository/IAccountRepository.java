package com.tourism.booking.repository;

import com.tourism.booking.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {
    Account findByUsername(String username);

    @Query(value = """
            SELECT r.role_name FROM role r JOIN role_account ra ON r.role_id = ra.role_id 
                        JOIN Account a ON ra.account_id = a.account_id 
                        WHERE a.username = :username
                        """, nativeQuery = true)
    List<String> findRolesByUsername(@Param("username") String username);

    boolean existsByUsername(String username);

}
