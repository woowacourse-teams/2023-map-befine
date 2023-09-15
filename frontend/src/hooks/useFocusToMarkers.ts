import { useEffect, useRef } from 'react';

const useFocusToMarker = (map: TMap | null, markers: Marker[]) => {
  const { Tmapv3 } = window;
  const bounds = useRef(new Tmapv3.LatLngBounds());

  useEffect(() => {
    if (map && markers && markers.length === 1) {
      map.panTo(markers[0].getPosition());
    }

    if (map && markers && markers.length > 1) {
      bounds.current = new Tmapv3.LatLngBounds();
      markers.forEach((marker: Marker) => {
        bounds.current.extend(marker.getPosition());
      });

      map.fitBounds(bounds.current);
    }
  }, [markers]);
};

export default useFocusToMarker;
