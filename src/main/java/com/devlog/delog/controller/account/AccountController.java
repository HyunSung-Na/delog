package com.devlog.delog.controller.account;

import com.devlog.delog.controller.ApiResult;
import com.devlog.delog.domain.Account;
import com.devlog.delog.security.Jwt;
import com.devlog.delog.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api/")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    private final Jwt jwt;

    @PostMapping("join")
    public ApiResult<JoinResult> join(@ModelAttribute SignUpRequest signUpRequest) {
        Account newAccount = accountService.processNewAccount(signUpRequest);
        String accessToken = jwt.createToken(
                newAccount.getId(),
                newAccount.getUsername(), newAccount.getEmail());
        return ApiResult.OK(
                new JoinResult(accessToken, newAccount)
        );
    }

    @GetMapping("info")
    public ApiResult<List<AccountDto>> info() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        return ApiResult.OK(
            accountService.accounts(pageRequest).stream()
                .map(AccountDto::new)
                .collect(toList())
        );
    }
}
