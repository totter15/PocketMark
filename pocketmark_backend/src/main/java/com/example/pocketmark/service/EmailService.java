package com.example.pocketmark.service;

import com.example.pocketmark.domain.EmailAuthenticationCode;
import com.example.pocketmark.dto.SendAuthenticationEmail.SendAuthenticationEmailDto;
import com.example.pocketmark.repository.EmailAuthenticationCodeRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailAuthenticationCodeRepository emailAuthenticationCodeRepository;
    private final int leftLimit;
    private final int rightLimit;
    private final int targetStringLength;
    private final Random random;

    public EmailService(JavaMailSender emailSender, EmailAuthenticationCodeRepository emailAuthenticationCodeRepository) {
        this.emailSender = emailSender;
        this.emailAuthenticationCodeRepository = emailAuthenticationCodeRepository;
        this.leftLimit = 48;    // 0
        this.rightLimit = 122;  // z
        this.targetStringLength = 10;
        this.random = new Random();
    }


    @Async
    public void sendSignUpAuthenticationMail(SendAuthenticationEmailDto dto){
        String authenticationCode = makeEmailAuthenticationCode();

        emailAuthenticationCodeRepository.save(EmailAuthenticationCode.builder()
                        .email(dto.getEmail())
                        .code(authenticationCode)
                        .build()
        );

        final MimeMessagePreparator preparator = message -> {
            final MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(dto.getEmail());
            helper.setSubject("[PocketMark] 회원가입 이메일 인증코드");
            helper.setText(authenticationCode);
        };

        emailSender.send(preparator);

    }

    public String makeEmailAuthenticationCode(){

        return random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }

}
