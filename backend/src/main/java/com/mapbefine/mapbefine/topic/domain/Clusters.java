package com.mapbefine.mapbefine.topic.domain;

import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.pin.domain.Pin;
import com.mapbefine.mapbefine.topic.exception.TopicErrorCode;
import com.mapbefine.mapbefine.topic.exception.TopicException.TopicBadRequestException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Clusters {

    private final List<Cluster> clusters;

    private Clusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public static Clusters from(List<Pin> pins, Double diameterInMeter) {
        validateDiameter(diameterInMeter);
        List<Cluster> clusters = executeCluster(pins, diameterInMeter);

        return new Clusters(clusters);
    }

    private static void validateDiameter(Double diameterInMeter) {
        if (Objects.nonNull(diameterInMeter)) {
            return;
        }

        throw new TopicBadRequestException(TopicErrorCode.ILLEGAL_DIAMETER_NULL);
    }

    private static List<Cluster> executeCluster(List<Pin> pins, double diameterInMeter) {
        List<Integer> parentOfPins = getParentOfPins(pins, diameterInMeter);

        return getClustersByParentOfPins(pins, parentOfPins);
    }

    private static List<Integer> getParentOfPins(List<Pin> pins, double diameterInMeter) {
        List<Integer> parentOfPins = getInitializeParentOfPins(pins.size());

        for (int i = 0; i < pins.size(); i++) {
            for (int j = 0; j < pins.size(); j++) {
                if (isNotRepresentPin(parentOfPins, i)) {
                    break;
                }

                if (i == j) {
                    continue;
                }

                int firstParentPinIndex = findParentOfSet(parentOfPins, i);
                int secondParentPinIndex = findParentOfSet(parentOfPins, j);

                if (isReachTwoPin(pins.get(firstParentPinIndex), pins.get(secondParentPinIndex), diameterInMeter)) {
                    union(parentOfPins, firstParentPinIndex, secondParentPinIndex, pins);
                }
            }
        }

        return parentOfPins;
    }

    private static boolean isNotRepresentPin(List<Integer> parentOfPins, int i) {
        return parentOfPins.get(i) != i;
    }

    private static List<Integer> getInitializeParentOfPins(int pinsSize) {
        List<Integer> parentOfPins = new ArrayList<>();

        for (int i = 0; i < pinsSize; i++) {
            parentOfPins.add(i);
        }

        return parentOfPins;
    }

    private static boolean isReachTwoPin(Pin firstPin, Pin secondPin, double diameterInMeter) {
        Coordinate firstPinCoordinate = firstPin.getLocation().getCoordinate();
        Coordinate secondPinCoordinate = secondPin.getLocation().getCoordinate();

        return firstPinCoordinate.calculateDistance(secondPinCoordinate) <= diameterInMeter;
    }

    private static int findParentOfSet(List<Integer> parentOfPins, int pinIndex) {
        if (parentOfPins.get(pinIndex) == pinIndex) {
            return pinIndex;
        }

        parentOfPins.set(pinIndex, findParentOfSet(parentOfPins, parentOfPins.get(pinIndex)));
        return parentOfPins.get(pinIndex);
    }

    private static void union(List<Integer> parentOfPins, int firstPinIndex, int secondPinIndex, List<Pin> pins) {
        if (firstPinIndex == secondPinIndex) {
            return;
        }
        Pin firstPin = pins.get(firstPinIndex);
        Pin secondPin = pins.get(secondPinIndex);
        if (isFirstPinOnLeft(firstPin, secondPin)) {
            parentOfPins.set(secondPinIndex, firstPinIndex);
            return;
        }
        parentOfPins.set(firstPinIndex, secondPinIndex);
    }

    private static boolean isFirstPinOnLeft(Pin firstPin, Pin secondPin) {
        Coordinate firstPinCoordinate = firstPin.getLocation().getCoordinate();
        Coordinate secondPinCoordinate = secondPin.getLocation().getCoordinate();

        return firstPinCoordinate.getLongitude() < secondPinCoordinate.getLongitude();
    }

    private static List<Cluster> getClustersByParentOfPins(List<Pin> pins, List<Integer> parentOfPins) {
        Map<Pin, List<Pin>> clusters = new HashMap<>();

        for (int pinIndex = 0; pinIndex < pins.size(); pinIndex++) {
            int parentPinIndex = findParentOfSet(parentOfPins, pinIndex);
            Pin parentPin = pins.get(parentPinIndex);
            clusters.computeIfAbsent(parentPin, ignored -> new ArrayList<>());
            clusters.get(parentPin).add(pins.get(pinIndex));
        }

        return clusters.entrySet()
                .stream()
                .map(clustersEntry -> Cluster.from(clustersEntry.getKey(), clustersEntry.getValue()))
                .toList();
    }

}
