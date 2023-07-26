import { useEffect, useRef } from 'react';
import Flex from './common/Flex';

declare global {
  interface Window {
    Tmapv3: any;
  }
}

const Map = () => {
  const mapContainer = useRef<HTMLDivElement>(null);
  useEffect(() => {
    // map 생성, zoom level 설정, center 좌표 설정
    const map = new window.Tmapv3.Map(mapContainer.current, {
      center: new window.Tmapv3.LatLng(37.5055, 127.0509),
      zoom: 13,
    });

    // 현재 위치 마커
    // const marker = new window.Tmapv3.Marker({
    //   position: new window.Tmapv3.LatLng(37.5055, 127.0509),
    //   map,
    // });

    return () => {
      map.destroy();
    };
  }, []);

  return (
    <Flex
      flex="1"
      id="map"
      ref={mapContainer}
      style={{
        height: '100vh',
        width: '100%',
      }}
    />
  );
};

export default Map;
