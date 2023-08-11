package com.mapbefine.mapbefine.oauth;

public interface AuthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();

}
