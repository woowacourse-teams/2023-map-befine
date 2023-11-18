import { useContext, useEffect } from 'react';

import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';
import useMapStore from '../store/mapInstance';

export default function useClickedCoordinate() {
  const { Tmapv3 } = window;
  const { mapInstance } = useMapStore((state) => state);
  const { clickedCoordinate } = useContext(CoordinatesContext);
  const { displayClickedMarker } = useContext(MarkerContext);

  useEffect(() => {
    if (!mapInstance) return;
    const currentZoom = mapInstance.getZoom();
    if (clickedCoordinate.address) displayClickedMarker();

    // 선택된 좌표가 있으면 해당 좌표로 지도의 중심을 이동
    if (clickedCoordinate.latitude && clickedCoordinate.longitude) {
      mapInstance.panTo(
        new Tmapv3.LatLng(
          clickedCoordinate.latitude,
          clickedCoordinate.longitude,
        ),
      );
    }
  }, [clickedCoordinate]);
}
