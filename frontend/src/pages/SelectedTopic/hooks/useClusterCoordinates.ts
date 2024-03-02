import { useContext } from 'react';

import { getClusteredCoordinates } from '../../../apis/new';
import { CoordinatesContext } from '../../../context/CoordinatesContext';
import useRealDistanceOfPin from '../../../hooks/useRealDistanceOfPin';
import useMapStore from '../../../store/mapInstance';
import { ClusteredCoordinates } from '../types';

interface ClusteredCoordinatesWithTopicId extends ClusteredCoordinates {
  topicId: string;
  id: string;
  pinName: string;
}

const useClusterCoordinates = (topicId: string) => {
  const { mapInstance } = useMapStore((state) => state);
  const { getDistanceOfPin } = useRealDistanceOfPin();
  const { setCoordinates } = useContext(CoordinatesContext);

  const setClusteredCoordinates = async () => {
    if (!mapInstance) return;

    const newCoordinates: ClusteredCoordinatesWithTopicId[] = [];
    const diameterOfPinSize = getDistanceOfPin(mapInstance);

    const responseData = await getClusteredCoordinates(
      topicId,
      diameterOfPinSize,
    );

    responseData.forEach((clusterOrPin: any, idx: number) => {
      newCoordinates.push({
        topicId,
        id: clusterOrPin.pins[0].id || `cluster ${idx}`,
        pinName: clusterOrPin.pins[0].name,
        latitude: clusterOrPin.latitude,
        longitude: clusterOrPin.longitude,
        pins: clusterOrPin.pins,
      });
    });

    setCoordinates(newCoordinates);
  };

  return setClusteredCoordinates;
};

export default useClusterCoordinates;
