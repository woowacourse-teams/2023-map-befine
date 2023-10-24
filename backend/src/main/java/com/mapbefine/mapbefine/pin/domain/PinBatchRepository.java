package com.mapbefine.mapbefine.pin.domain;

import com.mapbefine.mapbefine.topic.domain.Topic;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PinBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public PinBatchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] saveAll(Topic topicForCopy, List<Pin> originalPins) {
        topicForCopy.increasePinCount();
        LocalDateTime createdAt = LocalDateTime.now();
        topicForCopy.updateLastPinUpdatedAt(createdAt);
        final int[] rowCount = batchUpdatePins(topicForCopy, originalPins);

        final long firstId = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);

        List<PinImageDto> pinImagesToBatch = new ArrayList<>();
        for (int i = 0; i < originalPins.size(); i++) {
            if (rowCount[i] == 0) {
                continue;
            }
            final Pin pin = originalPins.get(i);
            /// TODO: 2023/10/25  PinImage fetch 조인으로 가져와서 N+1 없애야함
            pinImagesToBatch.addAll(PinImageDto.of(pin.getPinImages(), firstId + i));
        }

        return batchUpdatePinImages(pinImagesToBatch);
    }

    private int[] batchUpdatePins(Topic topicForCopy, List<Pin> pins) {
        topicForCopy.increasePinCount();
        LocalDateTime createdAt = LocalDateTime.now();
        topicForCopy.updateLastPinUpdatedAt(createdAt);

        return jdbcTemplate.batchUpdate("INSERT INTO pin ("
                + " name, description, member_id, topic_id, location_id,"
                + " created_at, updated_at)"
                + " VALUES ("
                + " ?, ?, ?, ?, ?,"
                + " ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final Pin pin = pins.get(i);
                ps.setString(1, pin.getName());
                ps.setString(2, pin.getDescription());
                ps.setLong(3, pin.getCreator().getId());
                ps.setLong(4, topicForCopy.getId());
                ps.setLong(5, pin.getLocation().getId());
                ps.setTimestamp(6, Timestamp.valueOf(createdAt));
                ps.setTimestamp(7, Timestamp.valueOf(createdAt));
            }

            @Override
            public int getBatchSize() {
                return pins.size();
            }
        });
    }

    private int[] batchUpdatePinImages(List<PinImageDto> pinImages) {
        return jdbcTemplate.batchUpdate("INSERT INTO pin_image ("
                + " image_url, pin_id)"
                + " VALUES ("
                + " ?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                final PinImageDto pinImage = pinImages.get(i);
                ps.setString(1, pinImage.getImageUrl());
                ps.setLong(2, pinImage.getPinId());
            }

            @Override
            public int getBatchSize() {
                return pinImages.size();
            }
        });
    }

    private static class PinImageDto {

        private final String imageUrl;
        private final Long pinId;
        private final boolean isDeleted;

        private PinImageDto(String imageUrl, Long pinId, boolean isDeleted) {
            this.imageUrl = imageUrl;
            this.pinId = pinId;
            this.isDeleted = isDeleted;
        }

        public static PinImageDto of(PinImage pinImage, Long pinId) {
            return new PinImageDto(
                    pinImage.getImageUrl(),
                    pinId,
                    pinImage.isDeleted()
            );
        }

        private static List<PinImageDto> of(List<PinImage> pinImages, Long pinId) {
            return pinImages.stream()
                    .map(pinImage -> PinImageDto.of(pinImage, pinId))
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
