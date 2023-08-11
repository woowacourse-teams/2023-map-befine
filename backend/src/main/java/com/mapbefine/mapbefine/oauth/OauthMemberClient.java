package com.mapbefine.mapbefine.oauth;

import com.mapbefine.mapbefine.member.domain.Member;

public interface OauthMemberClient {

    OauthServerType supportServer();

    Member fetch(String authCode);

}
