package com.mapbefine.mapbefine.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController { // Mypage 내용, Member 들 리스트

//    @GetMapping("/topics")
//    public List<TopicResponse> topicResponseList(AuthMember member) { // 현재 Login 한 User 의 정보가 넘어와야함
//        // Member member -> MyTopic -> .get ->
////        memberService.findMyTopic(member); // member.getCreateTopics().stream(TopicResponse::From)
//
//        // memberService TopicId -> 다 각각 조회해서 TopicResponse 로 감싸서 보내준다.
//    }


}
