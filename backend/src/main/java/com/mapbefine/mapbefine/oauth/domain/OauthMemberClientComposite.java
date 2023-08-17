package com.mapbefine.mapbefine.oauth.domain;

import static com.mapbefine.mapbefine.oauth.exception.OauthErrorCode.OAUTH_SERVER_TYPE_NOT_FOUND;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

import com.mapbefine.mapbefine.oauth.exception.OathException.OauthNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OauthMemberClientComposite {

    private final Map<OauthServerType, OauthMemberClient> mapping;

    public OauthMemberClientComposite(Set<OauthMemberClient> clients) {
        mapping = clients.stream()
                .collect(toMap(
                        OauthMemberClient::supportServer,
                        identity()
                ));
    }

    public OauthMember fetch(OauthServerType oauthServerType, String authCode) {
        return getClient(oauthServerType).fetch(authCode);
    }

    private OauthMemberClient getClient(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(() -> new OauthNotFoundException(OAUTH_SERVER_TYPE_NOT_FOUND));
    }

}
