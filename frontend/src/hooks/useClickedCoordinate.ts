import { useContext, useEffect } from 'react';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';

export default function useClickedCoordinate(map: TMap) {
  const { Tmapv3 } = window;
  const { clickedCoordinate } = useContext(CoordinatesContext);
  const { displayClickedMarker } = useContext(MarkerContext);

  useEffect(() => {
    if (!map) return;
    if (clickedCoordinate.address) displayClickedMarker(map);

    // 선택된 좌표가 있으면 해당 좌표로 지도의 중심을 이동
    if (clickedCoordinate.latitude && clickedCoordinate.longitude) {
      map.panTo(
        new Tmapv3.LatLng(
          clickedCoordinate.latitude,
          clickedCoordinate.longitude,
        ),
      );
    }
  }, [clickedCoordinate]);
}
