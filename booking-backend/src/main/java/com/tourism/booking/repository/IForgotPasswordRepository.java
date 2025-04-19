package com.tourism.booking.repository;

import com.tourism.booking.model.Account;
import com.tourism.booking.model.ForgotPassword;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("SELECT f FROM ForgotPassword f WHERE f.otp = :otp AND f.account = :account")
    Optional<ForgotPassword> findByOtpAndAccount(@Param("otp") Integer otp, @Param("account") Account account);

    Optional<ForgotPassword> findByAccount(Account account);

}
