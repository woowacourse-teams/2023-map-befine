import { useContext, useEffect } from 'react';

import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';
import useMapSidebarCoordinates from '../store/mapSidebarCoordinates';

export default function useUpdateCoordinates() {
  const { coordinates } = useContext(CoordinatesContext);
  const {
    markers,
    createMarkers,
    removeMarkers,
    createInfowindows,
    removeInfowindows,
  } = useContext(MarkerContext);

  const removePins = (markers: Marker[]) => {
    removeMarkers();
    removeInfowindows();
  };

  useEffect(() => {
    removePins(markers);

    if (coordinates.length > 0) {
      createMarkers();
      createInfowindows();
    }
  }, [coordinates]);
}
