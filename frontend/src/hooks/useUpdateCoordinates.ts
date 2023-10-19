import { useContext, useEffect } from 'react';

import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';

export default function useUpdateCoordinates(map: TMap | null) {
  const { coordinates } = useContext(CoordinatesContext);
  const {
    markers,
    createMarkers,
    removeMarkers,
    createInfowindows,
    removeInfowindows,
  } = useContext(MarkerContext);

  useEffect(() => {
    if (!map) return;
    if (markers && markers.length > 0) {
      removeMarkers();
      removeInfowindows();
    }
    if (coordinates.length > 0) {
      createMarkers(map);
      createInfowindows(map);
    }
  }, [coordinates]);
}
