package com.devlog.delog.service;

import com.devlog.delog.domain.Account;
import com.devlog.delog.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(username);
        Account user = Account.builder()
                .id(account.getId())
                .email(account.getEmail())
                .password(account.getPassword())
                .roles(account.getRoles())
                .build();
        return user;
    }
}
