package com.devlog.delog.service;

import com.devlog.delog.controller.account.SignUpRequest;
import com.devlog.delog.domain.Account;
import com.devlog.delog.error.EmailExistedException;
import com.devlog.delog.error.EmailNotExistedException;
import com.devlog.delog.error.UnauthorizedException;
import com.devlog.delog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    public Account processNewAccount(SignUpRequest signUpRequest) {
        Account newAccount = saveNewAccount(signUpRequest);
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
                .roles("ROLE_USER")
                .joinedAt(LocalDateTime.now())
                .build();
        account.generateEmailCheckToken();
        return accountRepository.save(account);
    }

    public Account authenticate(String email, String password) {
        Account existed = accountRepository.findByEmail(email);
        if (existed == null) {
            throw new EmailExistedException(email);
        }
        Account user = accountRepository.findByEmail(email);

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Password is wrong");
        }

        return user;
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

    public Optional<Boolean> findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            return Optional.empty();
        }
        return Optional.of(true);
    }


    public Account findByUsername(String name) {
        return accountRepository.findByUsername(name);
    }
}
