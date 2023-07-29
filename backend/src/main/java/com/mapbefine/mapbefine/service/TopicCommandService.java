package com.mapbefine.mapbefine.service;

import com.mapbefine.mapbefine.config.auth.AuthMember;
import com.mapbefine.mapbefine.config.auth.AuthTopic;
import com.mapbefine.mapbefine.dto.TopicCreateRequest;
import com.mapbefine.mapbefine.dto.TopicMergeRequest;
import com.mapbefine.mapbefine.dto.TopicUpdateRequest;
import com.mapbefine.mapbefine.entity.member.Member;
import com.mapbefine.mapbefine.entity.pin.Pin;
import com.mapbefine.mapbefine.entity.topic.Topic;
import com.mapbefine.mapbefine.repository.MemberRepository;
import com.mapbefine.mapbefine.repository.PinRepository;
import com.mapbefine.mapbefine.repository.TopicRepository;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class TopicCommandService {

    private final TopicRepository topicRepository;
    private final PinRepository pinRepository;
    private final MemberRepository memberRepository;

    public TopicCommandService(
            TopicRepository topicRepository,
            PinRepository pinRepository,
            MemberRepository memberRepository
    ) {
        this.topicRepository = topicRepository;
        this.pinRepository = pinRepository;
        this.memberRepository = memberRepository;
    }

    public long createNew(AuthMember member, TopicCreateRequest request) {
        member.canTopicCreate();

        Topic topic = new Topic(
                request.name(),
                request.description(),
                request.image(),
                request.publicity(),
                request.permission(),
                findCreatorByAuthMember(member)
        );

        topicRepository.save(topic);

        List<Long> pinIds = request.pins();
        List<Pin> original = pinRepository.findAllById(pinIds);

        validateExist(pinIds.size(), original.size());
        pinRepository.saveAll(duplicateUserPins(original, topic));

        return topic.getId();
    }

    private Member findCreatorByAuthMember(AuthMember member) {
        return memberRepository.findById(member.getMemberId())
                .orElseThrow(NoSuchElementException::new);
    }

    private void validateExist(int idCount, int existCount) {
        if (idCount != existCount) {
            throw new IllegalArgumentException("찾을 수 없는 ID가 포함되어 있습니다.");
        }
    }

    private List<Pin> duplicateUserPins(List<Pin> pins, Topic topic) {
        return pins.stream()
                .map(original -> original.copy(topic))
                .collect(Collectors.toList());
    }

    public long createMerge(AuthMember member, TopicMergeRequest request) {
        List<Long> topicIds = request.topics();
        List<Topic> topics = topicRepository.findAllById(topicIds)
                .stream()
                .filter(topic -> member.canRead(AuthTopic.from(topic))) // TODO : 일단, 이렇게 Merge 하기 이전에 canRead 한 놈들만 골라낼 수 있도록 했어요.
                .toList();

        validateExist(topicIds.size(), topics.size());

        Topic topic = new Topic(
                request.name(),
                request.description(),
                request.image(),
                request.publicity(),
                request.permission(),
                findCreatorByAuthMember(member)
        );

        topicRepository.save(topic);

        List<Pin> original = topics.stream()
                .map(Topic::getPins)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        List<Pin> pins = duplicateUserPins(original, topic);
        pinRepository.saveAll(pins);

        return topic.getId();
    }

    public void update(AuthMember member, Long id, TopicUpdateRequest request) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));
        member.canTopicUpdate(AuthTopic.from(topic));

        topic.update(request.name(), request.description(), request.image());
    }

    public void delete(AuthMember member, Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Topic입니다."));
        member.canDelete(AuthTopic.from(topic));

        pinRepository.deleteAllByTopicId(id);
        topicRepository.deleteById(id);
    }

}
