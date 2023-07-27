import { forwardRef, useContext, useEffect, useState } from 'react';
import Flex from './common/Flex';
import { Coordinate, CoordinatesContext } from '../context/CoordinatesContext';

const Map = (props: any, ref: any) => {
  // props로 전달받을 때마다 marker의 배열들을 tmap api를 사용해 지도위에 표시
  const { map } = props;
  const [markers, setMarkers] = useState<any>([]); //Marker[] = window.Tmapv2.Marker[
  const { coordinates } = useContext(CoordinatesContext);

  useEffect(() => {
    // 마커들을 모두 지도에서 제거
    markers.forEach((marker: any) => {
      marker.setMap(null);
    });
    setMarkers([]);
  }, [coordinates]);

  useEffect(() => {
    //전달받은 위치에 마커들이 모두 나오도록 지도의 중심과 확대 정도를 조절
    if (map && coordinates.length > 0) {
      const bounds = new window.Tmapv2.LatLngBounds();
      coordinates.forEach((coordinate: Coordinate) => {
        const marker = new window.Tmapv2.Marker({
          position: new window.Tmapv2.LatLng(
            coordinate.latitude,
            coordinate.longitude,
          ),
          icon: 'http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_1.png',
          map,
        });
        // marker를 markers 배열에 추가
        setMarkers((prevMarkers: any) => [...prevMarkers, marker]);

        bounds.extend(
          new window.Tmapv2.LatLng(coordinate.latitude, coordinate.longitude),
        );
      });
      map.fitBounds(bounds);
    }
  }, [coordinates, map]);

  return <Flex flex="1" id="map" ref={ref} height="100vh" width="100%" />;
};

export default forwardRef(Map);
