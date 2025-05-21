package com.tourism.booking.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.tourism.booking.dto.MailBody;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String sender;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom("tranvulam2392003@gmail.com");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        mailSender.send(message);
    }

    public boolean sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true); // true để hỗ trợ HTML

            mailSender.send(message);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendAccountCreationEmail(String toEmail, String username, String password) {
        String subject = "Thông tin đăng nhập hệ thống Elie Booking";

        String content = "<p>Xin chào,</p>"
                + "<p>Tài khoản của bạn đã được tạo thành công trên hệ thống Elie Booking.</p>"
                + "<p>Thông tin đăng nhập:</p>"
                + "<p>- Tên đăng nhập: <strong>" + username + "</strong></p>"
                + "<p>- Mật khẩu: <strong>" + password + "</strong></p>"
                + "<p>Vui lòng đổi mật khẩu sau khi đăng nhập lần đầu.</p>"
                + "<p>Trân trọng,</p>"
                + "<p>Đội ngũ Elie Booking</p>";

        return sendEmail(toEmail, subject, content);
    }
}
