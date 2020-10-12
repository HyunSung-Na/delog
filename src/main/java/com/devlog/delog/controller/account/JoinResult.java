package com.devlog.delog.controller.account;

import com.devlog.delog.domain.Account;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkNotNull;

public class JoinResult {

  private final String jwtToken;

  private final Account user;

  public JoinResult(String jwtToken, Account user) {
    checkNotNull(jwtToken, "apiToken must be provided.");
    checkNotNull(user, "user must be provided.");

    this.jwtToken = jwtToken;
    this.user = user;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public Account getUser() {
    return user;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
      .append("jwtToken", jwtToken)
      .append("user", user)
      .toString();
  }

}