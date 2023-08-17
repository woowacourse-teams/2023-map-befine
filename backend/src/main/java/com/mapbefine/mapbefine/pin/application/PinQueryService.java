package com.mapbefine.mapbefine.pin.application;

import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.FORBIDDEN_PIN_READ;
import static com.mapbefine.mapbefine.pin.exception.PinErrorCode.PIN_NOT_FOUND;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinRepository;
import com.mapbefine.mapbefine.pin.dto.response.PinDetailResponse;
import com.mapbefine.mapbefine.pin.dto.response.PinResponse;
import com.mapbefine.mapbefine.pin.exception.PinException.PinForbiddenException;
import com.mapbefine.mapbefine.pin.exception.PinException.PinNotFoundException;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PinQueryService {

    private final PinRepository pinRepository;

    public PinQueryService(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    // TODO: 2023/08/08 isDeleted 제외하고 조회하기
    public List<PinResponse> findAllReadable(AuthMember member) {
        return pinRepository.findAll()
                .stream()
                .filter(pin -> member.canRead(pin.getTopic()))
                .map(PinResponse::from)
                .toList();
    }

    // TODO: 2023/08/08 isDeleted 제외하고 조회하기
    public PinDetailResponse findDetailById(AuthMember member, Long pinId) {
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new PinNotFoundException(PIN_NOT_FOUND));
        validateReadAuth(member, pin.getTopic());

        return PinDetailResponse.from(pin);
    }

    private void validateReadAuth(AuthMember member, Topic topic) {
        if (member.canRead(topic)) {
            return;
        }

        throw new PinForbiddenException(FORBIDDEN_PIN_READ);
    }

    public List<PinResponse> findAllPinsByMemberId(AuthMember authMember, Long memberId) {
        return pinRepository.findByCreatorId(memberId)
                .stream()
                .filter(pin -> authMember.canRead(pin.getTopic()))
                .map(PinResponse::from)
                .toList();
    }
}
