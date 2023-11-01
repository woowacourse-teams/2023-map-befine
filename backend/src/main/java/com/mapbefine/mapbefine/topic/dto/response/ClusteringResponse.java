package com.mapbefine.mapbefine.topic.dto.response;

import com.mapbefine.mapbefine.topic.domain.Cluster;
import java.util.List;

public record ClusteringResponse(
        double latitude,
        double longitude,
        List<RenderPinResponse> pins
) {

    public static ClusteringResponse from(Cluster cluster) {
        return new ClusteringResponse(
                cluster.getLatitude(),
                cluster.getLongitude(),
                cluster.getPins()
                        .stream()
                        .map(RenderPinResponse::from)
                        .toList()
        );
    }

}
