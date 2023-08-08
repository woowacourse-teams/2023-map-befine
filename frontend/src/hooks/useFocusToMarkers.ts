import { useEffect, useRef } from 'react';

const useFocusToMarker = (map: any, markers: any) => {
  const bounds = useRef(new window.Tmapv3.LatLngBounds());

  useEffect(() => {
    if (markers.length === 1) {
      map.panTo(markers[0].getPosition());
    }

    if (markers.length > 1) {
      bounds.current = new window.Tmapv3.LatLngBounds();
      markers.forEach((marker: any) => {
        bounds.current.extend(marker.getPosition());
      });

      map.fitBounds(bounds.current);
    }
  }, [markers]);
};

export default useFocusToMarker;
