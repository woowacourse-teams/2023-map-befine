package com.mapbefine.mapbefine.atlas.application;

import com.mapbefine.mapbefine.atlas.domain.Atlas;
import com.mapbefine.mapbefine.atlas.domain.AtlasRepository;
import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AtlasQueryService {

    private final AtlasRepository atlasRepository;

    public AtlasQueryService(AtlasRepository atlasRepository) {
        this.atlasRepository = atlasRepository;
    }

    public List<TopicResponse> findTopicsByMember(AuthMember member) {
        return atlasRepository.findAllByMemberId(member.getMemberId())
                .stream()
                .map(Atlas::getTopic)
                .filter(member::canRead)
                .map(TopicResponse::from)
                .toList();
    }

}
