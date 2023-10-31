package com.mapbefine.mapbefine.common.repository;

import com.mapbefine.mapbefine.auth.domain.AuthMember;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.topic.domain.Topic;
import jakarta.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class PinBatchRepositoryCustomImpl implements PinBatchRepositoryCustom {

    private final EntityManager entityManager;
    private final JdbcTemplate jdbcTemplate;

    public PinBatchRepositoryCustomImpl(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public int[] saveAllToTopic(
            Topic topicForCopy,
            List<Pin> originalPins,
            AuthMember copiedPinsCreator
    ) {
        int[] rowCount = batchUpdatePins(topicForCopy, originalPins, copiedPinsCreator);
        Long firstIdFromBatch = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);
        validateId(firstIdFromBatch);

        List<PinImageDTO> pinImageDTOsToBatches = createPinImageDTOsToBatch(originalPins, rowCount, firstIdFromBatch);

        return batchUpdatePinImages(pinImageDTOsToBatches);
    }

    private int[] batchUpdatePins(
            Topic topicForCopy,
            List<Pin> originalPins,
            AuthMember copiedPinsCreator
    ) {
        topicForCopy.increasePinCount(originalPins.size());
        LocalDateTime createdAt = LocalDateTime.now();
        topicForCopy.updateLastPinUpdatedAt(createdAt);
        entityManager.flush();

        return jdbcTemplate.batchUpdate("INSERT INTO pin ("
                + " name, description, member_id, topic_id, location_id,"
                + " created_at, updated_at)"
                + " VALUES ("
                + " ?, ?, ?, ?, ?,"
                + " ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Pin pin = originalPins.get(i);
                ps.setString(1, pin.getName());
                ps.setString(2, pin.getDescription());
                ps.setLong(3, copiedPinsCreator.getMemberId());
                ps.setLong(4, topicForCopy.getId());
                ps.setLong(5, pin.getLocation().getId());
                ps.setTimestamp(6, Timestamp.valueOf(createdAt));
                ps.setTimestamp(7, Timestamp.valueOf(createdAt));
            }

            @Override
            public int getBatchSize() {
                return originalPins.size();
            }
        });
    }

    private void validateId(Long firstIdFromBatch) {
        if (Objects.isNull(firstIdFromBatch)) {
            throw new IllegalStateException("fail to batch update pins");
        }
    }

    private List<PinImageDTO> createPinImageDTOsToBatch(
            List<Pin> originalPins,
            int[] rowCount,
            Long firstIdFromBatch
    ) {
        List<PinImageDTO> pinImagesToBatch = new ArrayList<>();
        for (int i = 0; i < originalPins.size(); i++) {
            // TODO 인덴트 개선
            if (rowCount[i] == 0) {
                continue;
            }
            Pin pin = originalPins.get(i);
            pinImagesToBatch.addAll(PinImageDTO.of(pin.getPinImages(), firstIdFromBatch + i));
        }
        return pinImagesToBatch;
    }

    private int[] batchUpdatePinImages(List<PinImageDTO> pinImages) {
        return jdbcTemplate.batchUpdate("INSERT INTO pin_image ("
                + " image_url, pin_id)"
                + " VALUES ("
                + " ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PinImageDTO pinImage = pinImages.get(i);
                ps.setString(1, pinImage.getImageUrl());
                ps.setLong(2, pinImage.getPinId());
            }

            @Override
            public int getBatchSize() {
                return pinImages.size();
            }
        });
    }

    // TODO 네이밍 고민해보기
    private static class PinImageDTO {

        private final String imageUrl;
        private final Long pinId;
        private final boolean isDeleted;

        private PinImageDTO(String imageUrl, Long pinId, boolean isDeleted) {
            this.imageUrl = imageUrl;
            this.pinId = pinId;
            this.isDeleted = isDeleted;
        }

        public static PinImageDTO of(PinImage pinImage, Long pinId) {
            return new PinImageDTO(
                    pinImage.getImageUrl(),
                    pinId,
                    pinImage.isDeleted()
            );
        }

        private static List<PinImageDTO> of(List<PinImage> pinImages, Long pinId) {
            return pinImages.stream()
                    .map(pinImage -> PinImageDTO.of(pinImage, pinId))
                    .toList();
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public Long getPinId() {
            return pinId;
        }

        public boolean isDeleted() {
            return isDeleted;
        }
    }

}
