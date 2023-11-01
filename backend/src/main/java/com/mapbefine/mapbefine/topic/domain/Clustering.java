package com.mapbefine.mapbefine.topic.domain;

import com.mapbefine.mapbefine.location.domain.Coordinate;
import com.mapbefine.mapbefine.pin.domain.Pin;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Clustering {

    private final List<Cluster> clusters;

    private Clustering(List<Cluster> clusters) {
        this.clusters = clusters;
    }

    public static Clustering from(List<Pin> pins, double diameter) {
        List<Cluster> clusters = executeCluster(pins, diameter);

        return new Clustering(clusters);
    }

    private static List<Cluster> executeCluster(List<Pin> pins, double diameter) {
        int[] parent = getParentOfPins(pins, diameter);
        Map<Integer, List<Pin>> clustering = getClusteringByParentOfPins(pins, parent);

        return clustering.values().stream()
                .map(Cluster::from)
                .toList();
    }

    private static int[] getParentOfPins(List<Pin> pins, double diameter) {
        int[] parent = getInitializeParent(pins.size());

        for (int i = 0; i < pins.size(); i++) {
            for (int j = 0; j < pins.size(); j++) {
                if (i == j) {
                    continue;
                }

                if (isReachTwoPin(pins.get(i), pins.get(j), diameter)) {
                    union(parent, find(parent, i), find(parent, j));
                }
            }
        }

        return parent;
    }

    private static int[] getInitializeParent(int pinsSize) {
        int[] parent = new int[pinsSize];

        for (int i = 0; i < parent.length; i++) {
            parent[i] = i;
        }

        return parent;
    }

    private static boolean isReachTwoPin(Pin firstPin, Pin secondPin, double diameter) {
        Coordinate firstPinCoordinate = firstPin.getLocation().getCoordinate();
        Coordinate secondPinCoordinate = secondPin.getLocation().getCoordinate();

        return firstPinCoordinate.calculateDistance(secondPinCoordinate) <= diameter;
    }

    private static int find(int[] parent, int pinIndex) {
        if (parent[pinIndex] == pinIndex) {
            return pinIndex;
        }

        parent[pinIndex] = find(parent, parent[pinIndex]);
        return parent[pinIndex];
    }

    private static void union(int[] parent, int parentPinIndex, int childPinIndex) {
        parent[childPinIndex] = parentPinIndex;
    }

    private static Map<Integer, List<Pin>> getClusteringByParentOfPins(List<Pin> pins, int[] parent) {
        Map<Integer, List<Pin>> clusterings = new HashMap<>();

        for (int pinIndex = 0; pinIndex < pins.size(); pinIndex++) {
            int parentPinIndex = find(parent, pinIndex);
            clusterings.computeIfAbsent(parentPinIndex, ArrayList::new);
            clusterings.get(parentPinIndex).add(pins.get(pinIndex));
        }

        return clusterings;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

}
