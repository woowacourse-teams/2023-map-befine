package com.mapbefine.mapbefine.pin.application;

import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.FORBIDDEN_PIN_READ;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.PIN_NOT_FOUND;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinComment;
import com.mapbefine.mapbefine.pin.domain.PinCommentRepository;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinCommentResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinNotFoundException;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PinQueryService {

    private final PinRepository pinRepository;
    private final PinCommentRepository pinCommentRepository;

    public PinQueryService(PinRepository pinRepository, PinCommentRepository pinCommentRepository) {
        this.pinCommentRepository = pinCommentRepository;
        this.pinRepository = pinRepository;
    }

    public List<PinResponse> findAllReadable(AuthMember member) {
        return pinRepository.findAll()
                .stream()
                .filter(pin -> member.canRead(pin.getTopic()))
                .map(PinResponse::from)
                .toList();
    }

    public PinDetailResponse findDetailById(AuthMember member, Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new PinNotFoundException(PIN_NOT_FOUND, pinId));
        validateReadAuth(member, pin.getTopic());

        return PinDetailResponse.of(pin, member.canPinCreateOrUpdate(pin.getTopic()));
    }

    private void validateReadAuth(AuthMember member, Topic topic) {
        if (member.canRead(topic)) {
            return;
        }

        throw new PinForbiddenException(FORBIDDEN_PIN_READ);
    }

    public List<PinResponse> findAllPinsByMemberId(AuthMember authMember, Long memberId) {
        return pinRepository.findAllByCreatorId(memberId)
                .stream()
                .filter(pin -> authMember.canRead(pin.getTopic()))
                .map(PinResponse::from)
                .toList();
    }

    public List<PinCommentResponse> findAllPinCommentByPinId(AuthMember member, Long pinId) {
        List<PinComment> pinComments = pinCommentRepository.findAllByPinId(pinId);

        return pinComments.stream()
                .map(pinComment -> pinCommentToResponse(member, pinComment))
                .toList();
    }

    private PinCommentResponse pinCommentToResponse(AuthMember member, PinComment pinComment) {
        Long creatorId = pinComment.getCreator().getId();

        boolean canChange = (Objects.nonNull(member.getMemberId())
                && member.isSameMember(creatorId))
                || member.isAdmin();

        if (pinComment.isParentComment()) {
            return PinCommentResponse.ofParentComment(pinComment, canChange);
        }

        return PinCommentResponse.ofChildComment(pinComment, canChange);
    }

}
