package com.user.mgmt.request;

import lombok.Data;

@Data
public class GoogleOAuth2Request {
    private String access_token;
    private String token_type;
    private int expires_in;
    private String scope;
    private String prompt;

}
