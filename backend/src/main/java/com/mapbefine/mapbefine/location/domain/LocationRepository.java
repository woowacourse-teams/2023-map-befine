package com.mapbefine.mapbefine.location.domain;

import java.util.List;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query(
            "SELECT l FROM Location l "
                    + "WHERE ST_Contains(ST_Buffer(l.coordinate.coordinate, :distance), :coordinate)"
    )
    List<Location> findAllByCoordinateAndDistanceInMeters(
            @Param("coordinate") Point coordinate,
            @Param("distance") double distance);


}
