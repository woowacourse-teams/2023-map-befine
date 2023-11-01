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

    public static Clusters from(List<Pin> pins, double diameter) {
        List<Cluster> clusters = executeCluster(pins, diameter);

        return new Clusters(clusters);
    }

    private static List<Cluster> executeCluster(List<Pin> pins, double diameter) {
        int[] parentOfPins = getParentOfPins(pins, diameter);

        return getClustersByParentOfPins(pins, parentOfPins);
    }

    private static int[] getParentOfPins(List<Pin> pins, double diameter) {
        int[] parentOfPins = getInitializeParentOfPins(pins.size());

        for (int i = 0; i < pins.size(); i++) {
            for (int j = 0; j < pins.size(); j++) {
                if (i == j) {
                    continue;
                }

                if (isReachTwoPin(pins.get(i), pins.get(j), diameter)) {
                    union(parentOfPins, find(parentOfPins, i), find(parentOfPins, j));
                }
            }
        }

        return parentOfPins;
    }

    private static int[] getInitializeParentOfPins(int pinsSize) {
        int[] parentOfPins = new int[pinsSize];

        for (int i = 0; i < parentOfPins.length; i++) {
            parentOfPins[i] = i;
        }

        return parentOfPins;
    }

    private static boolean isReachTwoPin(Pin firstPin, Pin secondPin, double diameter) {
        Coordinate firstPinCoordinate = firstPin.getLocation().getCoordinate();
        Coordinate secondPinCoordinate = secondPin.getLocation().getCoordinate();

        return firstPinCoordinate.calculateDistance(secondPinCoordinate) <= diameter;
    }

    private static int find(int[] parentOfPins, int pinIndex) {
        if (parentOfPins[pinIndex] == pinIndex) {
            return pinIndex;
        }

        parentOfPins[pinIndex] = find(parentOfPins, parentOfPins[pinIndex]);
        return parentOfPins[pinIndex];
    }

    private static void union(int[] parentOfPins, int parentPinIndex, int childPinIndex) {
        parentOfPins[childPinIndex] = parentPinIndex;
    }

    private static List<Cluster> getClustersByParentOfPins(List<Pin> pins, int[] parentOfPins) {
        Map<Integer, List<Pin>> clusters = new HashMap<>();

        for (int pinIndex = 0; pinIndex < pins.size(); pinIndex++) {
            int parentPinIndex = find(parentOfPins, pinIndex);
            clusters.computeIfAbsent(parentPinIndex, ArrayList::new);
            clusters.get(parentPinIndex).add(pins.get(pinIndex));
        }

        return clusters.values()
                .stream()
                .map(Cluster::from)
                .toList();
    }

}
