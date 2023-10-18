package com.mapbefine.mapbefine.pin.domain;

import com.mapbefine.mapbefine.member.domain.Member;
import com.mapbefine.mapbefine.topic.domain.Topic;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    public List<Long> saveAll(List<Pin> originalPins, Member memberForCopy, Topic topicForCopy) {
        topicForCopy.increasePinCount();
        LocalDateTime createdAt = LocalDateTime.now();
        topicForCopy.updateLastPinUpdatedAt(createdAt);

        return Arrays.stream(jdbcTemplate.batchUpdate("INSERT INTO pin ("
                        + " name, description, member_id, topic_id, location_id,"
                        + " created_at, updated_at)"
                        + " VALUES ("
                        + " ?, ?, ?, ?, ?,"
                        + " ?, ?, ?)", new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        Pin pin = originalPins.get(i);

                        ps.setString(1, pin.getName());
                        ps.setString(2, pin.getDescription());
                        ps.setLong(3, memberForCopy.getId());
                        ps.setLong(4, topicForCopy.getId());
                        ps.setLong(5, pin.getLocation().getId());
                        ps.setTimestamp(6, Timestamp.valueOf(createdAt));
                        ps.setTimestamp(7, Timestamp.valueOf(createdAt));
                    }

                    @Override
                    public int getBatchSize() {
                        return originalPins.size();
                    }
                })).boxed()
                .map(Number::longValue)
                .toList();
    }

}
