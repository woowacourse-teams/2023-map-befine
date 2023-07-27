import { forwardRef, useContext, useEffect } from 'react';
import Flex from './common/Flex';
import { CoordinatesContext } from '../context/CoordinatesContext';
import { MarkerContext } from '../context/MarkerContext';

const Map = (props: any, ref: any) => {
  const { map } = props;
  const { coordinates } = useContext(CoordinatesContext);
  const { markers, createMarkers, removeMarkers } = useContext(MarkerContext);

  useEffect(() => {
    // 마커들을 모두 지도에서 제거
    if (markers.length > 0) {
      // 이전 마커 제거
      removeMarkers();
    }
    // 새로운 마커 추가
    if (coordinates.length > 0) {
      createMarkers(map);
    }
  }, [coordinates, map]);

  return <Flex flex="1" id="map" ref={ref} height="100vh" width="100%" />;
};

export default forwardRef(Map);
