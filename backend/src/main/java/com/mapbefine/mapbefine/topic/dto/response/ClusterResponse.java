package com.mapbefine.mapbefine.topic.dto.response;

import com.mapbefine.mapbefine.topic.domain.Cluster;
import java.util.List;

public record ClusterResponse(
        double latitude,
        double longitude,
        List<RenderPinResponse> pins
) {

    public static ClusterResponse from(Cluster cluster) {
        return new ClusterResponse(
                cluster.getLatitude(),
                cluster.getLongitude(),
                cluster.getPins()
                        .stream()
                        .map(RenderPinResponse::from)
                        .toList()
        );
    }

}
