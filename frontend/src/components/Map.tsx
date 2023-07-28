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
      removeMarkers();
    }

    // 새로운 마커 추가
    if (coordinates.length > 0) {
      createMarkers(map);
    }
  }, [coordinates]);

  useEffect(() => {
    // 마커가 하나만 있으면 해당 마커로 지도의 중심을 이동
    if (markers.length === 1) {
      map.panTo(markers[0].getPosition());
    }

    // 마커들을 모두 포함하는 경계를 계산
    if (markers.length > 1) {
      bounds.current = new window.Tmapv2.LatLngBounds();
      markers.forEach((marker: any) => {
        bounds.current.extend(marker.getPosition());
      });
      map.fitBounds(bounds.current);
    }
  }, [markers]);

  // url 파라미터에 pinDetail이 있으면 해당 pin으로 지도의 중심을 이동
  useEffect(() => {
    const queryParams = new URLSearchParams(location.search);
    if (queryParams.has('pinDetail')) {
      const pinId = queryParams.get('pinDetail');

      const marker = markers.find((marker: any) => marker.id === pinId);
      if (marker) {
        map.panTo(marker.getPosition());
        //marker 객체의 animation 속성을 변경
        marker._marker_data.options.animation =
          window.Tmapv2.MarkerOptions.ANIMATE_BOUNCE;
        marker._marker_data.options.animationLength = 500;
      }
    }
  }, [markers]);

  return <Flex flex="1" id="map" ref={ref} height="100vh" width="100%" />;
};

export default forwardRef(Map);
