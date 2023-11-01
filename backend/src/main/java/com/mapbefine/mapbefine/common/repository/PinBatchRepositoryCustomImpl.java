package com.mapbefine.mapbefine.common.repository;

import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.pin.domain.PinImage;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class PinBatchRepositoryCustomImpl implements PinBatchRepositoryCustom {

    private final JdbcTemplate jdbcTemplate;

    public PinBatchRepositoryCustomImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int[] saveAllToTopic(Topic topicForCopy, List<Pin> originalPins) {
        int[] rowCount = batchUpdatePins(topicForCopy, originalPins);
        
        Long firstIdFromBatch = jdbcTemplate.queryForObject("SELECT last_insert_id()", Long.class);
        validateId(firstIdFromBatch);
        List<PinImageInsertDto> pinImageInsertDtos = createPinImageDTOsToBatch(originalPins, rowCount,
                firstIdFromBatch);

        if (pinImageInsertDtos.isEmpty()) {
            return rowCount;
        }
        return batchUpdatePinImages(pinImageInsertDtos);
    }

    private int[] batchUpdatePins(
            Topic topicForCopy,
            List<Pin> originalPins
    ) {
        LocalDateTime createdAt = topicForCopy.getLastPinUpdatedAt();

        String bulkInsertSql = "INSERT INTO pin ("
                + "name, description, member_id, topic_id, location_id,"
                + " created_at, updated_at)"
                + " VALUES ("
                + " ?, ?, ?, ?, ?,"
                + " ?, ?)";
        log.debug("[Query] bulk insert size {} : {}", originalPins.size(), bulkInsertSql);

        Long topicId = topicForCopy.getId();
        Long creatorId = topicForCopy.getCreator().getId();

        return jdbcTemplate.batchUpdate(bulkInsertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Pin pin = originalPins.get(i);
                ps.setString(1, pin.getName());
                ps.setString(2, pin.getDescription());
                ps.setLong(3, creatorId);
                ps.setLong(4, topicId);
                ps.setLong(5, pin.getLocation().getId());
                ps.setTimestamp(6, Timestamp.valueOf(createdAt));
                ps.setTimestamp(7, Timestamp.valueOf(createdAt));
                log.trace("[Parameter Binding] {} : "
                                + "name={}, description={}, member_id={}, topic_id={}, location_id={}, "
                                + "created_at={}, updated_at={}",
                        i, pin.getName(), pin.getDescription(), creatorId, topicId, pin.getLocation().getId(),
                        createdAt, createdAt);
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

    private List<PinImageInsertDto> createPinImageDTOsToBatch(
            List<Pin> originalPins,
            int[] rowCount,
            Long firstIdFromBatch
    ) {
        return IntStream.range(0, originalPins.size())
                .filter(index -> rowCount[index] != -3)
                .mapToObj(index -> {
                    Pin pin = originalPins.get(index);
                    return PinImageInsertDto.of(pin.getPinImages(), firstIdFromBatch + index);
                }).flatMap(Collection::stream)
                .toList();
    }

    private int[] batchUpdatePinImages(List<PinImageInsertDto> pinImages) {
        String bulkInsertSql = "INSERT INTO pin_image "
                + "(image_url, pin_id) "
                + "VALUES "
                + "(?, ?)";
        log.debug("[Query] bulk insert size {} : {}", pinImages.size(), bulkInsertSql);

        return jdbcTemplate.batchUpdate(bulkInsertSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PinImageInsertDto pinImage = pinImages.get(i);
                ps.setString(1, pinImage.getImageUrl());
                ps.setLong(2, pinImage.getPinId());
                log.trace("[Parameter Binding] {} : imageUrl={}, pinImage={} ",
                        i, pinImage.getImageUrl(), pinImage.getPinId());
            }

            @Override
            public int getBatchSize() {
                return pinImages.size();
            }
        });
    }

    private static class PinImageInsertDto {

        private final String imageUrl;
        private final Long pinId;
        private final boolean isDeleted;

        private PinImageInsertDto(String imageUrl, Long pinId, boolean isDeleted) {
            this.imageUrl = imageUrl;
            this.pinId = pinId;
            this.isDeleted = isDeleted;
        }

        public static PinImageInsertDto of(PinImage pinImage, Long pinId) {
            return new PinImageInsertDto(
                    pinImage.getImageUrl(),
                    pinId,
                    pinImage.isDeleted()
            );
        }

        private static List<PinImageInsertDto> of(List<PinImage> pinImages, Long pinId) {
            return pinImages.stream()
                    .map(pinImage -> PinImageInsertDto.of(pinImage, pinId))
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
