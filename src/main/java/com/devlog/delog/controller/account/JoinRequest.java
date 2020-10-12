package com.devlog.delog.controller.account;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class JoinRequest {

    private String principal;

    private String credentials;

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("principal", principal)
                .append("credentials", credentials)
                .toString();
    }
}
