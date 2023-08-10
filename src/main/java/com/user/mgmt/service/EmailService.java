package com.user.mgmt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendOtpEmail(String email, Integer otp) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("OTP for Login");
        mailMessage.setFrom("Book My Gift Team <" + email + ">");
        mailMessage.setText("Dear User,\n\n Your OTP for login is: " + otp
                + ".\n\n" + "Please enter this OTP to proceed with your login.\n\n" + "Thank you for choosing us.\n\n"
                + "Warm Regards,\n" + "Book My Gift Team");

        //javaMailSender.send(mailMessage);
    }

}

