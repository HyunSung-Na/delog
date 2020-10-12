package com.devlog.delog.controller.authentication;

import com.devlog.delog.controller.account.AccountDto;
import com.devlog.delog.security.AuthenticationResult;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static org.springframework.beans.BeanUtils.copyProperties;

public class AuthenticationResultDto {

  private String jwtToken;

  private AccountDto accountDto;

  public AuthenticationResultDto(AuthenticationResult source) {
    copyProperties(source, this);

    this.accountDto = new AccountDto(source.getAccount());
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public AccountDto getAccountDto() {
    return accountDto;
  }

  public void setAccountDto(AccountDto accountDto) {
    this.accountDto = accountDto;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("jwtToken", jwtToken)
      .append("accountDto", accountDto)
      .toString();
  }

}