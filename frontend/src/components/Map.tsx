import { forwardRef, useContext, useEffect, useRef } from 'react';
import Flex from './common/Flex';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';

const Map = (props: any, ref: any) => {
  const { map } = props;
  const { coordinates } = useContext(CoordinatesContext);
  const { markers, createMarkers, removeMarkers } = useContext(MarkerContext);
  const bounds = useRef(new window.Tmapv2.LatLngBounds());

  useEffect(() => {
    // 마커들을 모두 지도에서 제거
    if (markers.length > 0) {
      // 이전 마커 제거
      removeMarkers();
    }
    // 새로운 마커 추가
    if (coordinates.length > 0) {
      createMarkers(map);

      // 마커들을 모두 포함하는 경계를 계산
    }
  }, [coordinates, map]);

  useEffect(() => {
    // 마커들을 모두 포함하는 경계를 계산
    if (markers.length > 0) {
      bounds.current = new window.Tmapv2.LatLngBounds();
      markers.forEach((marker: any) => {
        bounds.current.extend(marker.getPosition());
      });
      map.fitBounds(bounds.current);
    }
  }, [markers, map]);

  return <Flex flex="1" id="map" ref={ref} height="100vh" width="100%" />;
};

export default forwardRef(Map);
