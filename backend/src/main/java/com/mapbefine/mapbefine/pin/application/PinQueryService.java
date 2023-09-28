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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class PinQueryService {

    private final PinRepository pinRepository;

    public PinQueryService(PinRepository pinRepository) {
        this.pinRepository = pinRepository;
    }

    public List<PinResponse> findAllReadable(AuthMember member) {
        return pinRepository.findAllByIsDeletedFalse()
                .stream()
                .filter(pin -> member.canRead(pin.getTopic()))
                .map(PinResponse::from)
                .toList();
    }

    public PinDetailResponse findDetailById(AuthMember member, Long pinId) {
        Pin pin = pinRepository.findByIdAndIsDeletedFalse(pinId)
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
        return pinRepository.findAllByCreatorIdAndIsDeletedFalse(memberId)
                .stream()
                .filter(pin -> authMember.canRead(pin.getTopic()))
                .map(PinResponse::from)
                .toList();
    }
}
