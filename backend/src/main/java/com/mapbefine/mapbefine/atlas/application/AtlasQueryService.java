package com.mapbefine.mapbefine.atlas.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AtlasQueryService {

    private final AtlasRepository atlasRepository;

    public AtlasQueryService(AtlasRepository atlasRepository) {
        this.atlasRepository = atlasRepository;
    }

    public List<TopicResponse> findAllTopicsInAtlasByMember(AuthMember authMember) {
        validateNonExistsMember(authMember);

        return atlasRepository.findAllByMemberId(authMember.getMemberId())
                .stream()
                .map(Atlas::getTopic)
                .filter(authMember::canRead)
                .map(topic -> TopicResponse.from(topic, true))
                .toList();
    }

    public void validateNonExistsMember(AuthMember authMember) {
        if (Objects.isNull(authMember.getMemberId())) {
            throw new IllegalArgumentException("존재하지 않는 유저입니다.");
        }
    }

}
