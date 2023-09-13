import { useEffect } from 'react';

const useAnimateClickedPin = (map: TMap | null, markers: Marker[]) => {
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      const pinId = queryParams.get('pinDetail');

      const marker = markers.find((marker: Marker) => marker.id === pinId);
      if (marker && map) {
        map.setCenter(marker.getPosition());
        map.setZoom(17);
      }
    }
  }, [markers, map]);
};

export default useAnimateClickedPin;
