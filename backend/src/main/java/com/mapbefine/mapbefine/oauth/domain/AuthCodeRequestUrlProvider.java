package com.mapbefine.mapbefine.oauth.domain;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();

}
