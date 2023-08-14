package com.mapbefine.mapbefine.oauth.domain;

public interface OauthMemberClient {

    OauthServerType supportServer();

    OauthMember fetch(String authCode);

}
