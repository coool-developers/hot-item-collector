package com.sparta.hotitemcollector.global.email;

import com.sparta.hotitemcollector.domain.user.UserService;
import com.sparta.hotitemcollector.global.jwt.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    // Enum to handle email types
    public enum EmailType {
        PASSWORD_RESET, AUTH_CODE
    }

    public String sendMail(EmailMessage emailMessage, EmailType type) {
        String authNum = createCode();
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        if (type == EmailType.PASSWORD_RESET) {
            // Store the temporary password in Redis with an appropriate expiration time
            redisUtil.set(emailMessage.getTo(), authNum, 30); // Example: 30 minutes expiration
        }

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(emailMessage.getTo()); // Recipient
            mimeMessageHelper.setSubject(emailMessage.getSubject()); // Subject
            mimeMessageHelper.setText(setContext(authNum, type.name()), true); // Body with HTML

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully to {}", emailMessage.getTo());

            return authNum;

        } catch (MessagingException e) {
            log.error("Failed to send email to {}: {}", emailMessage.getTo(), e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    // Generate authentication code
    public String createCode() {
        SecureRandom random = new SecureRandom(); // Use SecureRandom for better security
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);
            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 'a')); // Lowercase letter
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 'A')); // Uppercase letter
                    break;
                default:
                    key.append(random.nextInt(10)); // Digit
            }
        }
        return key.toString();
    }

    // Placeholder for context setting
    private String setContext(String authNum, String type) {
        return String.format("<html><body><p>Your %s code is: %s</p></body></html>", type, authNum);
    }
}
