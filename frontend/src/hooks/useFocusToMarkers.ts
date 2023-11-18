import { useEffect, useRef, useState } from 'react';

const useFocusToMarker = (map: TMap | null, markers: Marker[]) => {
  const { Tmapv3 } = window;
  const bounds = useRef(new Tmapv3.LatLngBounds());
  const [markersLength, setMarkersLength] = useState<Number>(0);

  useEffect(() => {
    if (map && markers && markers.length === 1) {
      map.panTo(markers[0].getPosition());
    }
    if (map && markers && markers.length > 1) {
      bounds.current = new Tmapv3.LatLngBounds();
      markers.forEach((marker: Marker) => {
        bounds.current.extend(marker.getPosition());
      });

      if (markersLength === 0) {
        setMarkersLength(markers.length);
        map.fitBounds(bounds.current);
        return;
      }

      if (markersLength !== markers.length) map.fitBounds(bounds.current);
    }
    return () => {
      setMarkersLength(0);
    };
  }, [markers]);
};
export default useFocusToMarker;
