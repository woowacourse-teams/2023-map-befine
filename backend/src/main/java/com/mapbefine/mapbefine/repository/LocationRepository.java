package com.mapbefine.mapbefine.repository;

import com.mapbefine.mapbefine.entity.Location;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query(
            "select l "
                    + "from Location l "
                    + "where l.coordinate.latitude BETWEEN :currentLatitude - :distance AND :currentLatitude + :distance "
                    + "AND l.coordinate.longitude BETWEEN :currentLongitude - :distance AND :currentLongitude + :distance"
    )
    List<Location> findAllByRectangle(
            @Param("currentLatitude") BigDecimal currentLatitude,
            @Param("currentLongitude") BigDecimal currentLongitude,
            @Param("distance") BigDecimal distance
    );
}
