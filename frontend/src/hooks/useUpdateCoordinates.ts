import { useContext, useEffect } from 'react';

import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';
import useMapStore from '../store/mapInstance';

export default function useUpdateCoordinates() {
  const { coordinates } = useContext(CoordinatesContext);
  const { mapInstance } = useMapStore((state) => state);
  const {
    markers,
    createMarkers,
    removeMarkers,
    createInfowindows,
    removeInfowindows,
  } = useContext(MarkerContext);

  useEffect(() => {
    if (!mapInstance) return;

    if (markers && markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }

    if (coordinates.length > 0) {
      createMarkers(mapInstance);
      createInfowindows(mapInstance);
    }
  }, [coordinates]);
}
