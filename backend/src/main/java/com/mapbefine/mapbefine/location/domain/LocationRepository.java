package com.mapbefine.mapbefine.location.domain;

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
            @Param("currentLatitude") double currentLatitude,
            @Param("currentLongitude") double currentLongitude,
            @Param("distance") double distance
    );

    @Query(
            value = "SELECT l FROM Location l "
                    + "WHERE ( 6371000 * acos( cos( radians(:#{#current_coordinate.latitude}) ) "
                    + "      * cos( radians( l.coordinate.latitude ) ) "
                    + "      * cos( radians( l.coordinate.longitude ) - radians(:#{#current_coordinate.longitude}) ) "
                    + "      + sin( radians(:#{#current_coordinate.latitude}) ) "
                    + "      * sin( radians( l.coordinate.latitude ) ) ) ) <= :distance",
            nativeQuery = true
    )
    List<Location> findAllByCoordinateAndDistanceInMeters(
            @Param("current_coordinate") Coordinate coordinate,
            @Param("distance") double distance
    );

}
