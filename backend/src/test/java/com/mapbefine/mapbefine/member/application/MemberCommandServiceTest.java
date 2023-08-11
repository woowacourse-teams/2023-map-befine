package com.mapbefine.mapbefine.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.auth.domain.member.Admin;
import com.mapbefine.mapbefine.auth.domain.member.Guest;
import com.mapbefine.mapbefine.auth.domain.member.User;
import com.mapbefine.mapbefine.common.annotation.ServiceTest;
import com.mapbefine.mapbefine.member.MemberFixture;
import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.member.domain.MemberInfo;
import com.mapbefine.mapbefine.member.domain.MemberRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicBookmark;
import com.mapbefine.mapbefine.member.domain.MemberTopicBookmarkRepository;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermission;
import com.mapbefine.mapbefine.member.domain.MemberTopicPermissionRepository;
import com.mapbefine.mapbefine.member.domain.Role;
import com.mapbefine.mapbefine.member.dto.request.MemberCreateRequest;
import com.mapbefine.mapbefine.member.dto.request.MemberTopicPermissionCreateRequest;
import com.mapbefine.mapbefine.topic.TopicFixture;
import com.mapbefine.mapbefine.topic.domain.Topic;
import com.mapbefine.mapbefine.topic.domain.TopicRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class MemberCommandServiceTest {

    @Autowired
    private MemberCommandService memberCommandService;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTopicPermissionRepository memberTopicPermissionRepository;

    @Autowired
    private MemberTopicBookmarkRepository memberTopicBookmarkRepository;

    @Test
    @DisplayName("Admin 이 권한을 주는 경우 정상적으로 권한이 주어진다.")
    void saveMemberTopicPermissionByAdmin() {
        // given
        Member admin = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", admin));
        AuthMember authAdmin = new Admin(admin.getId());
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        Long savedId = memberCommandService.saveMemberTopicPermission(authAdmin, request);
        MemberTopicPermission memberTopicPermission = memberTopicPermissionRepository.findById(
                        savedId)
                .orElseThrow(NoSuchElementException::new);
        Member memberWithPermission = memberTopicPermission.getMember();

        // then
        assertThat(member).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(memberWithPermission);
    }

    @Test
    @DisplayName("Topic 의 Creator 이 권한을 주는 경우 정상적으로 권한이 주어진다.")
    void saveMemberTopicPermissionByCreator() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when
        Long savedId = memberCommandService.saveMemberTopicPermission(authCreator, request);
        MemberTopicPermission memberTopicPermission = memberTopicPermissionRepository.findById(
                        savedId)
                .orElseThrow(NoSuchElementException::new);
        Member memberWithPermission = memberTopicPermission.getMember();

        // then
        assertThat(member).usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(memberWithPermission);
    }

    @Test
    @DisplayName("Creator 가 아닌 유저가 권한을 주려는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByUser() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member notCreator = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authNotCreator = new User(
                notCreator.getId(),
                getCreatedTopics(notCreator),
                getTopicsWithPermission(notCreator)
        );
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when then
        assertThatThrownBy(
                () -> memberCommandService.saveMemberTopicPermission(authNotCreator, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Guest 가 유저에게 권한을 주려는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByGuest() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember guest = new Guest();
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when then
        assertThatThrownBy(() -> memberCommandService.saveMemberTopicPermission(guest, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("본인에게 권한을 주려하는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByCreator_whenSelf_thenFail() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                creator.getId()
        );

        // when then
        assertThatThrownBy(
                () -> memberCommandService.saveMemberTopicPermission(authCreator, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 권한을 부여 받은 사람에게 권한을 주는 경우 예외가 발생한다.")
    void saveMemberTopicPermissionByCreator_whenDuplicate_thenFail() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.USER
                )
        );
        Member member = memberRepository.save(
                MemberFixture.create(
                        "members",
                        "members@naver.com",
                        Role.USER
                )
        );
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);
        memberTopicPermissionRepository.save(memberTopicPermission);
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );
        MemberTopicPermissionCreateRequest request = new MemberTopicPermissionCreateRequest(
                topic.getId(),
                member.getId()
        );

        // when then
        assertThatThrownBy(
                () -> memberCommandService.saveMemberTopicPermission(authCreator, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Admin 이 권한을 삭제하는 경우 정상적으로 삭제가 이루어진다.")
    void deleteMemberTopicPermissionByAdmin() {
        // given
        Member admin = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.ADMIN));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", admin));
        AuthMember authAdmin = new Admin(admin.getId());

        // when
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);
        Long savedId = memberTopicPermissionRepository.save(memberTopicPermission).getId();
        memberCommandService.deleteMemberTopicPermission(authAdmin, savedId);

        // then
        assertThat(memberRepository.existsById(savedId)).isFalse();
    }

    @Test
    @DisplayName("creator 이 권한을 삭제하는 경우 정상적으로 삭제가 이루어진다.")
    void deleteMemberTopicPermissionByCreator() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );

        // when
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);
        Long savedId = memberTopicPermissionRepository.save(memberTopicPermission).getId();
        memberCommandService.deleteMemberTopicPermission(authCreator, savedId);

        // then
        assertThat(memberRepository.existsById(savedId)).isFalse();
    }

    @Test
    @DisplayName("creator 가 아닌 유저가 권한을 삭제하는 경우 예외가 발생한다.")
    void deleteMemberTopicPermissionByUser() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create("member", "member@naver.com", Role.USER));
        Member nonCreator = memberRepository.save(
                MemberFixture.create("memberss", "memberss@naver.com", Role.USER));
        Member member = memberRepository.save(
                MemberFixture.create("members", "members@naver.com", Role.USER));
        Topic topic = topicRepository.save(TopicFixture.createByName("topic", creator));
        AuthMember authNonCreator = new User(
                nonCreator.getId(),
                getCreatedTopics(nonCreator),
                getTopicsWithPermission(nonCreator)
        );

        // when
        MemberTopicPermission memberTopicPermission =
                MemberTopicPermission.createPermissionAssociatedWithTopicAndMember(topic, member);
        Long savedId = memberTopicPermissionRepository.save(memberTopicPermission).getId();

        // then
        assertThatThrownBy(
                () -> memberCommandService.deleteMemberTopicPermission(authNonCreator, savedId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 유저의 권한을 삭제하려 하려는 경우 예외가 발생한다.")
    void deleteMemberTopicPermissionByCreator_whenNoneExistsPermission_thenFail() {
        // given
        Member creator = memberRepository.save(
                MemberFixture.create(
                        "member",
                        "member@naver.com",
                        Role.USER
                )
        );
        AuthMember authCreator = new User(
                creator.getId(),
                getCreatedTopics(creator),
                getTopicsWithPermission(creator)
        );

        // when then
        assertThatThrownBy(
                () -> memberCommandService.deleteMemberTopicPermission(authCreator, Long.MAX_VALUE))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("유저를 저장한다.")
    void save() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        MemberInfo memberInfo = member.getMemberInfo();
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                memberInfo.getNickName(),
                memberInfo.getEmail(),
                memberInfo.getImageUrl(),
                memberInfo.getRole()
        );

        // when
        Long savedId = memberCommandService.save(memberCreateRequest);
        Member savedResult = memberRepository.findById(savedId)
                .orElseThrow(NoSuchElementException::new);

        // then
        assertThat(savedResult).usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt")
                .isEqualTo(member);
    }

    @Test
    @DisplayName("이미 존재하는 이름으로 유저를 저장할 때 예외가 발생한다.")
    void save_whenDuplicateName_thenFail() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        memberRepository.save(member);
        MemberInfo memberInfo = member.getMemberInfo();
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                memberInfo.getNickName(),
                "memberr@naver.com",
                memberInfo.getImageUrl(),
                memberInfo.getRole()
        );

        // when
        assertThatThrownBy(() -> memberCommandService.save(memberCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 유저를 저장할 때 예외가 발생한다.")
    void save_whenDuplicateEmail_thenFail() {
        // given
        Member member = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        memberRepository.save(member);
        MemberInfo memberInfo = member.getMemberInfo();
        MemberCreateRequest memberCreateRequest = new MemberCreateRequest(
                "memberr",
                memberInfo.getEmail(),
                memberInfo.getImageUrl(),
                memberInfo.getRole()
        );

        // when
        assertThatThrownBy(() -> memberCommandService.save(memberCreateRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("다른 유저의 토픽을 즐겨찾기에 추가할 수 있다.")
    public void addTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPublicAndAllMembersTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        //when
        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);
        Long bookmarkId = memberCommandService.addTopicInBookmark(
                MemberFixture.createUser(otherMember),
                topic.getId()
        );

        //then
        MemberTopicBookmark bookmark = memberTopicBookmarkRepository.findById(bookmarkId).get();

        assertThat(bookmark.getTopic().getId()).isEqualTo(topic.getId());
        assertThat(bookmark.getMember().getId()).isEqualTo(otherMember.getId());
    }

    @Test
    @DisplayName("권한이 없는 다른 유저의 토픽을 즐겨찾기에 추가할 수 없다.")
    public void addTopicInBookmark_Fail1() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        //when
        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        //then
        assertThatThrownBy(() -> memberCommandService.addTopicInBookmark(
                MemberFixture.createUser(otherMember),
                topic.getId()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("토픽에 대한 권한이 없어서 즐겨찾기에 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 토픽을 삭제할 수 있다.")
    public void deleteTopicInBookmark_Success() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        MemberTopicBookmark bookmark =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic, otherMember);
        memberTopicBookmarkRepository.save(bookmark);

        //when
        AuthMember user = MemberFixture.createUser(otherMember);

        assertThat(memberTopicBookmarkRepository.existsById(bookmark.getId()))
                .isTrue();

        memberCommandService.deleteTopicInBookmark(user, bookmark.getId());

        //then
        assertThat(memberTopicBookmarkRepository.existsById(bookmark.getId()))
                .isFalse();
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 권한이 없는 토픽은 삭제할 수 없다.")
    public void deleteTopicInBookmark_Fail() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic);

        MemberTopicBookmark bookmark =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic, creator);
        memberTopicBookmarkRepository.save(bookmark);

        Member otherMember = MemberFixture.create(
                "otherMember",
                "otherMember@naver.com",
                Role.USER
        );
        memberRepository.save(otherMember);

        //when then
        AuthMember otherUser = MemberFixture.createUser(otherMember);

        assertThatThrownBy(
                () -> memberCommandService.deleteTopicInBookmark(otherUser, bookmark.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("즐겨찾기 삭제에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("즐겨찾기 목록에 있는 모든 토픽을 삭제할 수 있다")
    public void deleteAllBookmarks_Success() {
        //given
        Member creator = MemberFixture.create(
                "member",
                "member@naver.com",
                Role.USER
        );
        Topic topic1 = TopicFixture.createPrivateAndGroupOnlyTopic(creator);
        Topic topic2 = TopicFixture.createPrivateAndGroupOnlyTopic(creator);

        memberRepository.save(creator);
        topicRepository.save(topic1);
        topicRepository.save(topic2);

        MemberTopicBookmark bookmark1 =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic1, creator);
        MemberTopicBookmark bookmark2 =
                MemberTopicBookmark.createWithAssociatedTopicAndMember(topic1, creator);

        memberTopicBookmarkRepository.save(bookmark1);
        memberTopicBookmarkRepository.save(bookmark2);

        //when
        assertThat(memberTopicBookmarkRepository.findAllByMemberId(creator.getId()))
                .hasSize(2);

        AuthMember user = MemberFixture.createUser(creator);
        memberCommandService.deleteAllBookmarks(user);

        //then
        assertThat(memberTopicBookmarkRepository.findAllByMemberId(creator.getId()))
                .hasSize(0);
    }

    private List<Long> getTopicsWithPermission(Member member) {
        return member.getTopicsWithPermissions()
                .stream()
                .map(Topic::getId)
                .toList();
    }

    private List<Long> getCreatedTopics(Member member) {
        return member.getCreatedTopics()
                .stream()
                .map(Topic::getId)
                .toList();
    }

}
