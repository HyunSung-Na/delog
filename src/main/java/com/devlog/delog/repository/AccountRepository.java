package com.devlog.delog.repository;

import com.devlog.delog.domain.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAll();

    Optional<Account> findById(Long id);

    Account findByEmail(String email);

    @Override
    Page<Account> findAll(Pageable pageable);
}
