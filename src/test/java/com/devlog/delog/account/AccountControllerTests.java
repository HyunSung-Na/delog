package com.devlog.delog.account;

import com.devlog.delog.domain.Account;
import com.devlog.delog.error.EmailNotExistedException;
import com.devlog.delog.error.PasswordWrongException;
import com.devlog.delog.security.Jwt;
import com.devlog.delog.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class AccountControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    private Jwt jwtUtil;

    @MockBean
    private AccountService userService;

    @Test
    public void createWithValidAttributes() throws Exception {
        Long id = 1004L;
        String email = "tester@example.com";
        String name = "Tester";
        String password = "test";

        Account mockUser = Account.builder().id(id).username(name).email(email).build();

        given(userService.authenticate(email, password)).willReturn(mockUser);

        given(jwtUtil.createToken(id, name, email))
                .willReturn("header.payload.signature");

        mvc.perform(post("/api/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"test\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/api/join"))
                .andExpect(content().string(
                        containsString("{\"jwtToken\":\"header.payload.signature\"}")
                ));

        verify(userService).authenticate(eq(email), eq(password));
    }


    @Test
    public void createWithNotExistedEmail() throws Exception {
        given(userService.authenticate("x@example.com", "test"))
                .willThrow(EmailNotExistedException.class);

        mvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"x@example.com\",\"password\":\"test\"}"))
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq("x@example.com"), eq("test"));
    }

    @Test
    public void createWithWrongPassword() throws Exception {
        given(userService.authenticate("tester@example.com", "x"))
                .willThrow(PasswordWrongException.class);

        mvc.perform(post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"x\"}"))
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq("tester@example.com"), eq("x"));
    }

}
