package com.devlog.delog.account;

import com.devlog.delog.domain.Account;
import com.devlog.delog.error.EmailNotExistedException;
import com.devlog.delog.error.PasswordWrongException;
import com.devlog.delog.security.Jwt;
import com.devlog.delog.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AccountControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private Jwt jwtUtil;

    @MockBean
    private AccountService accountService;


    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_corrent_input() throws Exception{
        mockMvc.perform(post("/api/auth")
                .param("username", "racoon")
                .param("email", "oceana57@email.net")
                .param("password", "1234")
                .with(csrf()))
                .andExpect(authenticated().withUsername("racoon"));

        String name = "racoon";
        Account account = accountService.findByUsername(name);
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "1234");
        assertNotNull(account.getEmailCheckToken());
    }

}