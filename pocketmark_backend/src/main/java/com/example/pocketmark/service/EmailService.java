package com.example.pocketmark.service;

import com.example.pocketmark.constant.ErrorCode;
import com.example.pocketmark.domain.EmailAuthenticationCode;
import com.example.pocketmark.dto.AuthenticationEmail;
import com.example.pocketmark.dto.SendAuthenticationEmail.SendAuthenticationEmailDto;
import com.example.pocketmark.exception.GeneralException;
import com.example.pocketmark.repository.EmailAuthenticationCodeRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailAuthenticationCodeRepository emailAuthenticationCodeRepository;
    private final Random random;


    public static int leftEmailAuthenticationCodeLimit = 48;    // 0
    public static int rightEmailAuthenticationCodeLimit = 122;  // z
    public static int emailAuthenticationCodeLength = 10;
    public static Long expireSignUpEmailAuthentication = 5L;

    public EmailService(JavaMailSender emailSender, EmailAuthenticationCodeRepository emailAuthenticationCodeRepository) {
        this.emailSender = emailSender;
        this.emailAuthenticationCodeRepository = emailAuthenticationCodeRepository;
        this.random = new Random();
    }


    @Async
    public void sendSignUpAuthenticationMail(SendAuthenticationEmailDto dto){
        String authenticationCode = makeEmailAuthenticationCode();

        emailAuthenticationCodeRepository.save(EmailAuthenticationCode.builder()
                        .email(dto.getEmail())
                        .code(authenticationCode)
                        .success(false)
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

        return random.ints(leftEmailAuthenticationCodeLimit,rightEmailAuthenticationCodeLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(emailAuthenticationCodeLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

    }


    @Transactional
    public boolean authenticateEmail(AuthenticationEmail.AuthenticationEmailDto dto) {

        EmailAuthenticationCode code = emailAuthenticationCodeRepository
                .findFirstByEmailOrderByCreatedAtDesc(dto.getEmail());

        if(dto.getCode().equals(code.getCode())){
            boolean isExpired = code.getCreatedAt().plusMinutes(expireSignUpEmailAuthentication)
                    .isBefore(LocalDateTime.now());

            if(isExpired){
                throw new GeneralException(ErrorCode.EMAIL_AUTHENTICATION_CODE_EXPIRE);
            }else{
                code.successAuthenticate();
                return true;
            }
        }

        return false;
    }
}
