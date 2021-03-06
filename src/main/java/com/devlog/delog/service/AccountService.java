package com.devlog.delog.service;

import com.devlog.delog.config.AppProperties;
import com.devlog.delog.controller.account.SignUpRequest;
import com.devlog.delog.domain.Account;
import com.devlog.delog.error.EmailExistedException;
import com.devlog.delog.error.EmailNotExistedException;
import com.devlog.delog.error.NotFoundException;
import com.devlog.delog.error.UnauthorizedException;
import com.devlog.delog.mail.EmailMessage;
import com.devlog.delog.mail.EmailService;
import com.devlog.delog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    private final EmailService emailService;

    private final TemplateEngine templateEngine;

    private final AppProperties appProperties;



    public Account processNewAccount(SignUpRequest signUpRequest) {
        checkArgument(isNotEmpty(signUpRequest.getName()), "name must be provided.");
        checkArgument(
                signUpRequest.getName().length() >= 1 && signUpRequest.getName().length() <= 20,
                "name length must be between 1 and 10 characters."
        );
        checkArgument(isNotEmpty(signUpRequest.getPrincipal()), "email must be provided.");
        checkArgument(
                signUpRequest.getPrincipal().length() >= 5 && signUpRequest.getPrincipal().length() <= 50,
                "email length must be between 1 and 10 characters."
        );

        checkArgument(isNotEmpty(signUpRequest.getCredentials()), "password must be provided.");
        checkArgument(
                signUpRequest.getCredentials().length() >= 1 && signUpRequest.getCredentials().length() <= 1000,
                "password length must be between 1 and 100 characters."
        );
        Account newAccount = saveNewAccount(signUpRequest);
        sendSignUpConfirmEmail(newAccount);
        return newAccount;
    }

    private Account saveNewAccount(SignUpRequest signUpRequest) {

        Account existed = accountRepository.findByEmail(signUpRequest.getPrincipal());

        if (existed != null) {
            throw new EmailNotExistedException(signUpRequest.getPrincipal());
        }

        signUpRequest.setCredentials(bCryptPasswordEncoder.encode(signUpRequest.getCredentials()));
        Account account = Account.builder()
                .username(signUpRequest.getName())
                .email(signUpRequest.getPrincipal())
                .password(signUpRequest.getCredentials())
                .joinedAt(LocalDateTime.now())
                .build();
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    public Account authenticate(String email, String password) {
        Account user = accountRepository.findByEmail(email);
        if (user == null) {
            throw new NotFoundException(email);
        }

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Password is wrong");
        }

        return user;
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        Context context = new Context();
        context.setVariable("link", "/check-email-token?token=" + newAccount.getEmailCheckToken() +
                "&email=" + newAccount.getEmail());
        context.setVariable("username", newAccount.getUsername());
        context.setVariable("linkName", "이메일 인증하기");
        context.setVariable("message", "delog 서비스를 사용하려면 링크를 클릭하세요");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(newAccount.getEmail())
                .subject("delog, 회원 가입 인증")
                .message(message)
                .build();

        emailService.sendEmail(emailMessage);
    }

    public void sendLoginLink(Account account) {
        Context context = new Context();
        context.setVariable("link", "/login-by-email?token=" + account.getEmailCheckToken()
                + "&email" + account.getEmail());
        context.setVariable("username", account.getUsername());
        context.setVariable("linkName", "delog 로그인하기");
        context.setVariable("message", "로그인을 하려면 아래 링크를 클릭하세요");
        context.setVariable("host", appProperties.getHost());
        String message = templateEngine.process("mail/simple-link", context);

        EmailMessage emailMessage = EmailMessage.builder()
                .to(account.getEmail())
                .subject("delog, 로그인 링크")
                .message(message)
                .build();
        emailService.sendEmail(emailMessage);
    }

    public void updatePassword(Account account, String newPassword) {
        account.setPassword(bCryptPasswordEncoder.encode(newPassword));
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> accounts(Pageable pageable) {
        return accountRepository.findAll(pageable).getContent();
    }


    public Optional<Account> findById(Long accountId) {
        checkNotNull(accountId, "userId must be provided.");
        return accountRepository.findById(accountId);
    }

    public Optional<Boolean> checkFindByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            return Optional.empty();
        }
        return Optional.of(true);
    }

    public Account findByEmail(String email) {
        checkNotNull(email, "email must be provided.");
        return accountRepository.findByEmail(email);
    }


    public Account findByUsername(String name) {
        return accountRepository.findByUsername(name);
    }

    public void completeSignUp(Account account, String token) {
        checkNotNull(account, "account must be provided.");
        checkNotNull(token, "token must be provided.");
        if (!account.isValidToken(token)) {
            throw new UnauthorizedException(token);
        }

        account.completeSignUp();
    }

    public void deleteAccount(Long accountId) {
        checkNotNull(accountId, "accountId must be provided.");
        accountRepository.deleteById(accountId);
    }
}
