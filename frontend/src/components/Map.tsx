import { useEffect, useRef } from 'react';
import Flex from './common/Flex';

declare global {
  interface Window {
    Tmapv2: any;
  }
}

const Map = () => {
  const mapContainer = useRef<HTMLDivElement>(null);
  useEffect(() => {
    const map = new window.Tmapv2.Map(mapContainer.current, {
      center: new window.Tmapv2.LatLng(37.5055, 127.0509),
      zoom: 15,
    });

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
