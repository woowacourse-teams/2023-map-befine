package com.mapbefine.mapbefine.admin.dto;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberInfo;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.dto.response.TopicResponse;

import java.time.LocalDateTime;
import java.util.List;

public record AdminMemberDetailResponse(
        Long id,
        String nickName,
        String email,
        String imageUrl,
        List<TopicResponse> topics,
        List<PinResponse> pins,
        LocalDateTime updatedAt
) {

    // TODO: 2023/09/12 topics, pins 모두 member를 통해 얻어올 수 있다. Service에서 꺼내서 넘겨줄 것인가 ? 아니면 DTO에서 꺼내올 것인가 ?
    public static AdminMemberDetailResponse of(
            Member member,
            List<Topic> topics,
            List<Pin> pins
    ) {
        MemberInfo memberInfo = member.getMemberInfo();
        List<TopicResponse> topicResponses = topics.stream()
                .map(TopicResponse::fromGuestQuery)
                .toList();
        List<PinResponse> pinResponses = pins.stream()
                .map(PinResponse::from)
                .toList();

        return new AdminMemberDetailResponse(
                member.getId(),
                memberInfo.getNickName(),
                memberInfo.getEmail(),
                memberInfo.getImageUrl(),
                topicResponses,
                pinResponses,
                member.getUpdatedAt()
        );
    }


}
