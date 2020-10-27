package com.devlog.delog.account;

import com.devlog.delog.controller.account.SignUpRequest;
import com.devlog.delog.domain.Account;
import com.devlog.delog.mail.EmailMessage;
import com.devlog.delog.mail.EmailService;
import com.devlog.delog.service.AccountService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;



import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class AccountRestControllerTests {

    @MockBean
    EmailService emailService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private AccountService accountService;


    @DisplayName("회원 가입 처리 - 입력값 정상")
    @Test
    void signUpSubmit_with_corrent_input() throws Exception{
        mockMvc.perform(post("/api/join")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "racoon")
                .param("principal", "test1111@email.com")
                .param("credentials", "1234"))
                .andDo(print())
                .andExpect(status().isOk());

        String email = "test1111@email.com";
        Account account = accountService.findByEmail(email);
        assertNotNull(account);
        assertNotEquals(account.getPassword(), "1234");
        assertNotNull(account.getEmailCheckToken());
        then(emailService).should().sendEmail(any(EmailMessage.class));

    }

    @DisplayName("회원 가입 처리 - 입력값 오류")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception{
        mockMvc.perform(post("/api/join")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "hyun")
                .param("email", "email..")
                .param("password", "12345"))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(unauthenticated());
    }

    @DisplayName("인증메일 확인 - 입력값 오류")
    @Test
    void checkEmailToken_with_wrong_input() throws Exception{
        SignUpRequest accountDto = new SignUpRequest();
        accountDto.setName("hyun");
        accountDto.setPrincipal("test1234@email.com");
        accountDto.setCredentials("12345678");
        Account newAccount = accountService.processNewAccount(accountDto);

        mockMvc.perform(get("/check-email-token")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", "adfadfasf")
                .param("email", newAccount.getEmail()))
                .andExpect(status().is(401))
                .andDo(print());
    }

    @DisplayName("인증메일 확인 - 입력값 정상")
    @Test
    void checkEmailToken() throws Exception {
        SignUpRequest accountDto = new SignUpRequest();
        accountDto.setName("hyun");
        accountDto.setPrincipal("test1234@email.com");
        accountDto.setCredentials("12345678");
        Account newAccount = accountService.processNewAccount(accountDto);
        newAccount.generateEmailCheckToken();

        mockMvc.perform(get("/check-email-token")
                .contentType(MediaType.APPLICATION_JSON)
                .param("token", newAccount.getEmailCheckToken())
                .param("email", newAccount.getEmail()))
                .andExpect(status().isOk())
                .andDo(print());
    }

}