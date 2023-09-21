package com.mapbefine.mapbefine.oauth.domain;

import static com.mapbefine.mapbefine.oauth.exception.OauthErrorCode.OAUTH_SERVER_TYPE_NOT_FOUND;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

import com.mapbefine.mapbefine.oauth.exception.OauthException.OauthNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<OauthServerType, AuthCodeRequestUrlProvider> mapping;

    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
        mapping = providers.stream()
                .collect(toMap(
                        AuthCodeRequestUrlProvider::supportServer,
                        identity()
                ));
    }

    public String provide(OauthServerType oauthServerType) {
        return getProvider(oauthServerType).provide();
    }

    public AuthCodeRequestUrlProvider getProvider(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(() -> new OauthNotFoundException(OAUTH_SERVER_TYPE_NOT_FOUND, oauthServerType));
    }

}
