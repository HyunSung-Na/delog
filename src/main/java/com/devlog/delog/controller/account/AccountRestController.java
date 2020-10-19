package com.devlog.delog.controller.account;

import com.devlog.delog.controller.ApiResult;
import com.devlog.delog.domain.Account;
import com.devlog.delog.error.EmailNotExistedException;
import com.devlog.delog.error.NotFoundException;
import com.devlog.delog.error.UnauthorizedException;
import com.devlog.delog.security.Jwt;
import com.devlog.delog.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    private final Jwt jwt;

    @PostMapping("join")
    public ApiResult<JoinResult> join(@ModelAttribute SignUpRequest signUpRequest) {
        Account newAccount = accountService.processNewAccount(signUpRequest);
        String accessToken = jwt.createToken(
                newAccount.getId(),
                newAccount.getUsername(), newAccount.getEmail(), newAccount.getRoles().toArray(new String[0]));
        return ApiResult.OK(
                new JoinResult(accessToken, newAccount)
        );
    }

    @GetMapping("user/info/{accountId}")
    public ApiResult<AccountDto> info(@PathVariable Long accountId) {
        return ApiResult.OK(
            accountService.findById(accountId)
                .map(AccountDto::new)
                    .orElseThrow(() -> new NotFoundException(Account.class, accountId))
        );
    }

    @GetMapping("user/info/list")
    public ApiResult<List<AccountDto>> infoList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ApiResult.OK(
            accountService.accounts(pageRequest).stream()
                .map(AccountDto::new)
                .collect(toList())
        );
    }

    @PostMapping("user/exists")
    public ApiResult<Boolean> checkEmail(Map<String, String> request) {
        String email = request.get("email");
        return ApiResult.OK(
                accountService.checkFindByEmail(email).isPresent()
        );
    }

    @PostMapping("user/settings/password")
    public ResponseEntity updatePassword(@AuthenticationPrincipal Account account, String newPassword) {
        accountService.updatePassword(account, newPassword);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("user/remove/{accountId}")
    public ResponseEntity deleteAccount(@PathVariable Long accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok().build();
    }
}
