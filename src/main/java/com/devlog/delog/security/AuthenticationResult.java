package com.devlog.delog.security;

import com.devlog.delog.domain.Account;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkNotNull;

public class AuthenticationResult {

  private final String jwtToken;

  private final Account account;

  public AuthenticationResult(String jwtToken, Account account) {
    checkNotNull(jwtToken, "jwtToken must be provided.");
    checkNotNull(account, "account must be provided.");

    this.jwtToken = jwtToken;
    this.account = account;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public Account getAccount() {
    return account;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
            .append("jwtToken", jwtToken)
            .append("account", account)
            .toString();
  }
}