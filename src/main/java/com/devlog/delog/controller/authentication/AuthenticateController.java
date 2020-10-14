package com.devlog.delog.controller.authentication;

import com.devlog.delog.controller.ApiResult;
import com.devlog.delog.domain.Account;
import com.devlog.delog.security.AuthenticationRequest;
import com.devlog.delog.security.AuthenticationResult;
import com.devlog.delog.security.Jwt;
import com.devlog.delog.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthenticateController {

    private final AccountService accountService;

    private final Jwt jwt;

    public AuthenticateController(AccountService accountService, Jwt jwt) {
        this.accountService = accountService;
        this.jwt = jwt;
    }

    @GetMapping("check-email-token/{token}{email}")
    public ApiResult<AuthenticationResultDto> checkEmailToken(@PathVariable String token, @PathVariable String email) {
        Account account = accountService.findByEmail(email);
        accountService.completeSignUp(account, token);

        String jwtToken = jwt.createToken(
                account.getId(),
                account.getUsername(), account.getEmail(), account.getRoleList().toArray(new String[0]));

        return ApiResult.OK(
                new AuthenticationResultDto(
                        new AuthenticationResult(jwtToken, account))
        );
    }

    @PostMapping
    public ApiResult<AuthenticationResultDto> authenticate (@RequestBody AuthenticationRequest request) {

        String email = request.getPrincipal();
        String password = request.getCredentials();

        Account account = accountService.authenticate(email, password);

        String jwtToken = jwt.createToken(
                account.getId(),
                account.getUsername(), account.getEmail(), account.getRoleList().toArray(new String[0]));

        return ApiResult.OK(
                new AuthenticationResultDto(
                        new AuthenticationResult(jwtToken, account))
        );
    }
}
