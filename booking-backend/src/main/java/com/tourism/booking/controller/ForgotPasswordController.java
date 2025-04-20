package com.tourism.booking.controller;

import com.tourism.booking.dto.ChangePassword;
import com.tourism.booking.dto.MailBody;
import com.tourism.booking.exception.ApiException;
import com.tourism.booking.exception.ErrorCode;
import com.tourism.booking.model.Account;
import com.tourism.booking.model.ForgotPassword;
import com.tourism.booking.repository.IAccountRepository;
import com.tourism.booking.repository.IForgotPasswordRepository;
import com.tourism.booking.service.impl.EmailService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

//@PreAuthorize("permitAll()")
@RestController
@RequestMapping("${api.prefix}/forgotPassword")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173/")
public class ForgotPasswordController {

    IAccountRepository accountRepository;
    EmailService emailService;
    IForgotPasswordRepository forgotPasswordRepository;

    @PostMapping("/verifyMail/{email}")
    @Transactional
    public ResponseEntity<String> verifyEmail(@PathVariable String email) {
        Account acc = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_EXISTS));

        int otp = otpGenerator();
        Date expires = new Date(System.currentTimeMillis() + 5 * 60 * 1000); // 5 phút

        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("OTP để thay đổi mật khẩu")
                .text("Mã OTP của bạn là: " + otp)
                .build();
        emailService.sendSimpleMessage(mailBody);

        ForgotPassword fp = forgotPasswordRepository
                .findByAccount(acc)
                .map(existing -> {
                    existing.setOtp(otp);
                    existing.setExpirationTime(expires);
                    return existing;
                })
                .orElseGet(() -> {
                    ForgotPassword created = ForgotPassword.builder()
                            .account(acc)
                            .otp(otp)
                            .expirationTime(expires)
                            .build();
                    acc.setForgotPassword(created);
                    return created;
                });

        forgotPasswordRepository.save(fp);

        return ResponseEntity.ok("Email sent successfully!");
    }


    @PostMapping("/verifyOtp/{otp}/{email}")
    public ResponseEntity<String> verifyOTP(@PathVariable Integer otp, @PathVariable String email) {
        Account acc = accountRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException(ErrorCode.EMAIL_EXISTS));
        ForgotPassword fp = forgotPasswordRepository.findByOtpAndAccount(otp, acc)
                .orElseThrow(() -> new ApiException(ErrorCode.OTP_INVALID));
        if (fp.getExpirationTime().before(Date.from(Instant.now()))) {
            forgotPasswordRepository.deleteById(fp.getFpid());
            return new ResponseEntity<>("OTP has expired", HttpStatus.EXPECTATION_FAILED);
        }

        return ResponseEntity.ok("OTP has been verified !!");
    }

    @PostMapping("/changePassword/{email}")
    public ResponseEntity<?> changePasswordHandler(@PathVariable("email") String email,
                                                   @RequestBody ChangePassword changePassword) {
        if (!Objects.equals(changePassword.password(), changePassword.repeatPassword())) {
            return new ResponseEntity<>("Please enter the password again!", HttpStatus.BAD_REQUEST);
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String encodedPassword = passwordEncoder.encode(changePassword.password());
        accountRepository.updatePassword(email, encodedPassword);
        return ResponseEntity.ok("Password has been changed !!");
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }
}
