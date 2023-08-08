import { useContext, useEffect } from 'react';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';

interface UseUpdateCoordinatesProps {
  map: any;
}

export default function useUpdateCoordinates(map: UseUpdateCoordinatesProps) {
  const { coordinates } = useContext(CoordinatesContext);
  const { markers, createMarkers, removeMarkers } = useContext(MarkerContext);

  useEffect(() => {
    if (!map) return;
    if (markers.length > 0) {
      removeMarkers();
    }
    if (coordinates.length > 0) {
      createMarkers(map);
    }
  }, [coordinates]);
}
