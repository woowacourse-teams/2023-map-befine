package com.mapbefine.mapbefine.topic.domain;

import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.pin.domain.Pin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class Clusters {

    private final List<Cluster> clusters;

    private Clusters(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public static Clusters from(List<Pin> pins, double diameterInMeter) {
        List<Cluster> clusters = executeCluster(pins, diameterInMeter);

        return new Clusters(clusters);
    }

    private static List<Cluster> executeCluster(List<Pin> pins, double diameterInMeter) {
        int[] parentOfPins = getParentOfPins(pins, diameterInMeter);

        return getClustersByParentOfPins(pins, parentOfPins);
    }

    private static int[] getParentOfPins(List<Pin> pins, double diameterInMeter) {
        int[] parentOfPins = getInitializeParentOfPins(pins.size());

        for (int i = 0; i < pins.size(); i++) {
            for (int j = 0; j < pins.size(); j++) {
                if (isNotRepresentPin(parentOfPins, i)) {
                    break;
                }

                if (i == j) {
                    continue;
                }

                int firstParentPinIndex = find(parentOfPins, i);
                int secondParentPinIndex = find(parentOfPins, j);

                if (isReachTwoPin(pins.get(firstParentPinIndex), pins.get(secondParentPinIndex), diameterInMeter)) {
                    union(parentOfPins, firstParentPinIndex, secondParentPinIndex, pins);
                }
            }
        }

        return parentOfPins;
    }

    private static boolean isNotRepresentPin(int[] parentOfPins, int i) {
        return parentOfPins[i] != i;
    }

    private static int[] getInitializeParentOfPins(int pinsSize) {
        int[] parentOfPins = new int[pinsSize];

        for (int i = 0; i < parentOfPins.length; i++) {
            parentOfPins[i] = i;
        }

        return parentOfPins;
    }

    private static boolean isReachTwoPin(Pin firstPin, Pin secondPin, double diameterInMeter) {
        Coordinate firstPinCoordinate = firstPin.getLocation().getCoordinate();
        Coordinate secondPinCoordinate = secondPin.getLocation().getCoordinate();

        return firstPinCoordinate.calculateDistance(secondPinCoordinate) <= diameterInMeter;
    }

    private static int find(int[] parentOfPins, int pinIndex) {
        if (parentOfPins[pinIndex] == pinIndex) {
            return pinIndex;
        }

        parentOfPins[pinIndex] = find(parentOfPins, parentOfPins[pinIndex]);
        return parentOfPins[pinIndex];
    }

    private static void union(int[] parentOfPins, int firstPinIndex, int secondPinIndex, List<Pin> pins) {
        if (firstPinIndex == secondPinIndex) {
            return;
        }
        Pin firstPin = pins.get(firstPinIndex);
        Pin secondPin = pins.get(secondPinIndex);
        if (isFirstPinOnLeft(firstPin, secondPin)) {
            parentOfPins[secondPinIndex] = firstPinIndex;
            return;
        }
        parentOfPins[firstPinIndex] = secondPinIndex;
    }

    private static boolean isFirstPinOnLeft(Pin firstPin, Pin secondPin) {
        Coordinate firstPinCoordinate = firstPin.getLocation().getCoordinate();
        Coordinate secondPinCoordinate = secondPin.getLocation().getCoordinate();

        return firstPinCoordinate.getLongitude() < secondPinCoordinate.getLongitude();
    }

    private static List<Cluster> getClustersByParentOfPins(List<Pin> pins, int[] parentOfPins) {
        Map<Pin, List<Pin>> clusters = new HashMap<>();

        for (int pinIndex = 0; pinIndex < pins.size(); pinIndex++) {
            int parentPinIndex = find(parentOfPins, pinIndex);
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
