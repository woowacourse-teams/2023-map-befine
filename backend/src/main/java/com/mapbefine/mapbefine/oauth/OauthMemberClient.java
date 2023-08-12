package com.mapbefine.mapbefine.oauth;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthMember fetch(String authCode);

}
