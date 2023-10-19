import { useEffect, useRef } from 'react';
import { useLocation, useParams } from 'react-router-dom';

const useFocusToMarker = (map: TMap | null, markers: Marker[]) => {
  const { Tmapv3 } = window;
  const bounds = useRef(new Tmapv3.LatLngBounds());
  const params = useLocation();
  const queryParams = new URLSearchParams(window.location.search);
  const pinDetail = queryParams.get('pinDetail');
  useEffect(() => {
    if (map && markers && markers.length === 1) {
      map.panTo(markers[0].getPosition());
    }
    if (map && pinDetail) {
      const marker = markers.find((marker: Marker) => marker.id === pinDetail);
      if (marker) {
        map.setCenter(marker.getPosition());
        map.setZoom(17);
      }
    }
    if (
      map &&
      markers &&
      markers.length > 1 &&
      params.pathname.includes('see-together') &&
      !pinDetail
    ) {
      bounds.current = new Tmapv3.LatLngBounds();
      markers.forEach((marker: Marker) => {
        bounds.current.extend(marker.getPosition());
      });

      map.fitBounds(bounds.current);
    }
  }, [markers]);
};

export default useFocusToMarker;
