import { useEffect, useRef, useState } from 'react';

import useMapStore from '../store/mapInstance';

const useFocusToMarker = (markers: Marker[]) => {
  const { Tmapv3 } = window;
  const { mapInstance } = useMapStore((state) => state);
  const bounds = useRef(new Tmapv3.LatLngBounds());
  const [markersLength, setMarkersLength] = useState<Number>(0);

  useEffect(() => {
    if (mapInstance && markers && markers.length === 1) {
      mapInstance.panTo(markers[0].getPosition());
    }
    if (mapInstance && markers && markers.length > 1) {
      bounds.current = new Tmapv3.LatLngBounds();
      markers.forEach((marker: Marker) => {
        bounds.current.extend(marker.getPosition());
      });

      if (markersLength === 0) {
        setMarkersLength(markers.length);

        const mapBounds = mapInstance.getBounds();

        const leftWidth = new Tmapv3.LatLng(
          mapBounds._ne._lat,
          mapBounds._sw._lng,
        );
        const rightWidth = new Tmapv3.LatLng(
          mapBounds._ne._lat,
          mapBounds._ne._lng,
        );
        console.log(leftWidth.distanceTo(rightWidth));

        mapInstance.setCenter(bounds.current.getCenter());
        // map.fitBounds(bounds.current, {
        //   left: 100, // 지도의 왼쪽과의 간격(단위 : px)
        //   top: 100, // 지도의 위쪽과의 간격(단위 : px)
        //   right: 100, // 지도의 오른쪽과의 간격(단위 : px)
        //   bottom: 20, // 지도의 아래쪽과의 간격(단위 : px)
        // });
        return;
      }

      if (markersLength !== markers.length) {
        mapInstance.fitBounds(bounds.current);
      }
    }
    return () => {
      setMarkersLength(0);
    };
  }, [markers]);
};
export default useFocusToMarker;
